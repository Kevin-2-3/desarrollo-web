const categorias = { MOTO: "Motocicletas", ACCESORIO: "Accesorios", REFACCION: "Refacciones" };
const clavesFiltro = ["busqueda", "categoria", "precioMin", "precioMax"];
const sinImagen = "/sin-imagen.svg";

function parametros(fuente, pagina = 0) {
    const filtros = new URLSearchParams();
    for (const clave of clavesFiltro) {
        const valor = fuente.get(clave)?.trim();
        if (valor) filtros.set(clave, valor);
    }
    if (Number.isSafeInteger(pagina) && pagina > 0) filtros.set("page", pagina);
    return filtros;
}

function rutaCatalogo(filtros) {
    return filtros.size ? "catalogo.html?" + filtros : "catalogo.html";
}

function variantesEnRango(producto, filtros) {
    const min = Number(filtros.get("precioMin") || 0);
    const max = Number(filtros.get("precioMax") || Infinity);
    return producto.variantes.filter(v => v.precioMxn >= min && v.precioMxn <= max);
}

function cargarImagen(img, ruta, alt) {
    let url = sinImagen;
    try {
        const origen = new URL(ruta || sinImagen, location.origin);
        if (origen.origin === location.origin || origen.protocol === "https:") url = origen.href;
    } catch { /* Conservar la imagen de sustitución. */ }
    img.alt = alt;
    img.onerror = () => { img.onerror = null; img.src = sinImagen; };
    img.src = url;
}

function tarjeta(producto, filtros, dinero) {
    const nodo = document.querySelector("#tarjeta").content.cloneNode(true);
    cargarImagen(nodo.querySelector("img"), producto.imagenes[0], producto.nombre);
    nodo.querySelector(".categoria").textContent = producto.marca + " · " + categorias[producto.categoria];
    nodo.querySelector("h3").textContent = producto.nombre;
    const variantes = variantesEnRango(producto, filtros);
    const precios = variantes.map(v => Number(v.precioMxn));
    const desde = new Set(precios).size > 1 ? "Desde " : "";
    nodo.querySelector(".precio").textContent = precios.length ? desde + dinero(Math.min(...precios)) : "Sin precio disponible";
    const disponible = variantes.some(v => v.stock > 0);
    const stock = nodo.querySelector("[data-stock]");
    stock.className = disponible ? "disponible" : "agotado";
    stock.textContent = disponible ? "Disponible" : "Agotado";
    const enlace = nodo.querySelector("[data-detalle]");
    const consulta = new URLSearchParams({ id: producto.id });
    for (const [clave, valor] of filtros) consulta.set(clave, valor);
    enlace.hidden = true;
    enlace.setAttribute("aria-label", "Ver detalle de " + producto.nombre);
    return nodo;
}

function prepararFiltros(form, filtros) {
    for (const clave of clavesFiltro) form.elements[clave].value = filtros.get(clave) || "";
    const { precioMin, precioMax } = form.elements;
    function validar() {
        const invertido = precioMin.valueAsNumber > precioMax.valueAsNumber;
        precioMax.setCustomValidity(invertido ? "El precio máximo debe ser igual o mayor que el mínimo." : "");
    }
    precioMin.oninput = precioMax.oninput = validar;
    validar();
    form.onsubmit = evento => {
        evento.preventDefault();
        if (form.reportValidity()) location.assign(rutaCatalogo(parametros(new FormData(form))));
    };
}

function paginacion(filtros, numero, paginas) {
    const nav = document.querySelector(".paginacion");
    nav.hidden = paginas < 2;
    nav.querySelector("#numero-pagina").textContent = "Página " + (numero + 1) + " de " + paginas;
    for (const [selector, destino] of [["#anterior", numero - 1], ["#siguiente", numero + 1]]) {
        const enlace = nav.querySelector(selector);
        enlace.hidden = destino < 0 || destino >= paginas;
        enlace.href = rutaCatalogo(parametros(filtros, destino));
    }
}

async function listar(filtros, ctx) {
    prepararFiltros(document.querySelector("#filtros-catalogo"), filtros);
    const consulta = new URLSearchParams(filtros);
    consulta.set("size", 12);
    const lista = document.querySelector("#lista-productos");
    const estado = lista.querySelector("[role=status]");
    const reintentar = lista.querySelector("#reintentar");
    lista.setAttribute("aria-busy", "true");
    reintentar.hidden = true;
    try {
        const pagina = await ctx.api("/productos?" + consulta);
        if (!ctx.activo()) return;
        if (pagina.number > 0 && !pagina.content.length) {
            location.replace(rutaCatalogo(parametros(filtros, Math.max(0, pagina.totalPages - 1))));
            return;
        }
        estado.textContent = pagina.content.length ? pagina.totalElements + (pagina.totalElements === 1 ? " producto" : " productos")
            : "Sin resultados. Prueba otra búsqueda o ajusta los filtros.";
        lista.querySelector("#restablecer").hidden = pagina.content.length > 0;
        lista.querySelector(".productos-grid").replaceChildren(...pagina.content.map(p => tarjeta(p, filtros, ctx.dinero)));
        paginacion(filtros, pagina.number, pagina.totalPages);
    } catch (error) {
        estado.textContent = error.message;
        reintentar.hidden = false;
        reintentar.onclick = () => listar(filtros, ctx);
    } finally { lista.removeAttribute("aria-busy"); }
}

export async function catalogo(vista, id, ctx) {
    const consulta = new URLSearchParams(location.search);
    await listar(parametros(consulta, Number(consulta.get("page"))), ctx);
}
