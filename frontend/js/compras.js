function filaArticulo(item, ctx, editable) {
  const { esc, dinero } = ctx;
  const sku = esc(item.skuId);
  const etiqueta = esc(item.nombre + ' (' + item.codigoSku + ')');
  const variante = [item.marca, item.codigoSku, item.color, item.talla && 'Talla ' + item.talla]
    .filter(Boolean).map(esc).join(' · ');
  const descripcion = item.descripcion ? `<br><small>${esc(item.descripcion)}</small>` : '';
  const existencias = editable && !item.disponible
    ? `<br><strong>Revisa la cantidad o quita el artículo. Existencias: ${esc(item.stock)}.</strong>` : '';
  const cantidad = editable
    ? `<form class="acciones" data-cantidad="${sku}">
         <input aria-label="Cantidad de ${etiqueta}" type="number" name="cantidad" required min="1"
           max="${esc(item.stock)}" step="1" value="${esc(item.cantidad)}">
         <button type="submit" class="secundario">Actualizar</button></form>`
    : esc(item.cantidad);
  const quitar = editable
    ? `<td><button class="secundario" type="button" data-quitar="${sku}" aria-label="Quitar ${etiqueta}">Quitar</button></td>` : '';
  return `<tr><th scope="row">${esc(item.nombre)}<br><small>${variante}</small>${descripcion}${existencias}</th>
    <td>${dinero(item.precioUnitarioMxn)}</td><td>${cantidad}</td><td>${dinero(item.subtotalMxn)}</td>${quitar}</tr>`;
}

function articulos(datos, ctx, editable = false) {
  ctx.main.querySelector('[data-filas]').innerHTML = datos.items.map(i => filaArticulo(i, ctx, editable)).join('');
  for (const total of ctx.main.querySelectorAll('[data-total]')) total.textContent = ctx.dinero(datos.totalMxn);
}

function mensaje(ctx, texto) {
  if (ctx.activo()) ctx.main.querySelector('[data-mensaje]').textContent = texto;
}

function pintarCarrito(datos, ctx) {
  const lleno = datos.items.length > 0;
  ctx.main.querySelector('[data-vacio]').hidden = lleno;
  ctx.main.querySelector('[data-carrito]').hidden = !lleno;
  articulos(datos, ctx, true);
}

async function carrito(ctx) {
  const datos = await ctx.api('/carrito');
  if (!ctx.activo()) return;
  pintarCarrito(datos, ctx);
  const panel = ctx.main.querySelector('[data-carrito]');
  async function cambiar(ruta, opciones, exito) {
    if (panel.inert) return;
    panel.inert = true;
    try {
      const respuesta = await ctx.api(ruta, opciones);
      const actual = respuesta ?? await ctx.api('/carrito');
      if (ctx.activo()) {
        pintarCarrito(actual, ctx);
        mensaje(ctx, '');
        ctx.aviso(exito);
      }
    } catch (error) {
      mensaje(ctx, error.message);
    } finally { panel.inert = false; }
  }
  panel.onsubmit = event => {
    event.preventDefault();
    const form = event.target;
    cambiar('/carrito/items/' + form.dataset.cantidad, {
      method: 'PUT', body: { cantidad: Number(form.elements.cantidad.value) }
    }, 'Cantidad actualizada.');
  };
  panel.onclick = event => {
    const boton = event.target.closest('button');
    if (boton?.hasAttribute('data-quitar')) {
      cambiar('/carrito/items/' + boton.dataset.quitar, { method:'DELETE' }, 'Artículo eliminado.');
    }
    if (boton?.hasAttribute('data-vaciar') && window.confirm('¿Vaciar todos los artículos de tu carrito?')) {
      cambiar('/carrito', { method:'DELETE' }, 'Carrito vacío.');
    }
  };
}

export async function compras(vista, id, ctx) {
  if (vista === 'carrito') await carrito(ctx);
}
