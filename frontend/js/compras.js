const fechas = new Intl.DateTimeFormat('es-MX', { dateStyle: 'long', timeStyle: 'short' });

function estado(valor) {
  return valor === 'CONFIRMADO' ? 'Confirmado' : valor;
}

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

function destino(entrega, main) {
  for (const dato of main.querySelectorAll('[data-campo]')) dato.textContent = entrega[dato.dataset.campo];
}

function mensaje(ctx, texto) {
  if (ctx.activo()) ctx.main.querySelector('[data-mensaje]').textContent = texto;
}

function pintarCarrito(datos, ctx) {
  const lleno = datos.items.length > 0;
  ctx.main.querySelector('[data-vacio]').hidden = lleno;
  ctx.main.querySelector('[data-carrito]').hidden = !lleno;
  const listo = lleno && datos.items.every(i => i.disponible);
  ctx.main.querySelector('[data-continuar]').hidden = !listo;
  ctx.main.querySelector('[data-ajustar]').hidden = listo;
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

// El resumen sólo viaja entre los documentos de la misma pestaña y cliente.
function leerCompra(clave) {
  try {
    return JSON.parse(sessionStorage.getItem(clave));
  } catch {
    sessionStorage.removeItem(clave);
    return null;
  }
}

async function entrega(ctx, clave) {
  const datos = await ctx.api('/carrito');
  if (!ctx.activo()) return;
  const borrador = leerCompra(clave);
  if (borrador?.intento) {
    ctx.ir('resumen.html', 'Revisa el resultado de la confirmación anterior antes de continuar.');
    return;
  }
  if (!datos.items.length || !datos.items.every(i => i.disponible)) {
    ctx.ir('carrito.html', 'Revisa tu carrito antes de continuar.');
    return;
  }
  const form = ctx.main.querySelector('[data-entrega]');
  for (const campo of form.querySelectorAll('input')) campo.value = borrador?.resumen?.entrega?.[campo.name] ?? '';
  form.hidden = false;
  form.oninput = event => event.target.setCustomValidity('');
  form.onsubmit = async event => {
    event.preventDefault();
    const boton = form.querySelector('button');
    if (boton.disabled) return;
    const valores = Object.fromEntries(new FormData(form));
    for (const [campo, valor] of Object.entries(valores)) {
      valores[campo] = valor.trim();
      if (!valores[campo]) form.elements[campo].setCustomValidity('Completa este campo.');
    }
    if (!/^[0-9+ ()-]{7,20}$/.test(valores.telefono)) {
      form.elements.telefono.setCustomValidity('Usa entre 7 y 20 caracteres, con números, espacios, +, paréntesis o guiones.');
    }
    if (!form.reportValidity()) return;
    boton.disabled = true;
    try {
      const resumen = await ctx.api('/checkout/resumen', { method:'POST', body:valores });
      if (!ctx.activo()) return;
      sessionStorage.setItem(clave, JSON.stringify({ resumen }));
      ctx.ir('resumen.html');
    } catch (error) {
      for (const campo of Object.keys(error.campos ?? {})) {
        form.elements.namedItem(campo)?.setCustomValidity('Revisa este dato de entrega.');
      }
      if (ctx.activo()) form.reportValidity();
      mensaje(ctx, error.message);
    } finally { boton.disabled = false; }
  };
}

function pintarResumen(resumen, ctx) {
  articulos(resumen.carrito, ctx);
  destino(resumen.entrega, ctx.main);
  ctx.main.querySelector('[data-resumen]').hidden = false;
}

async function revision(ctx, clave) {
  const compra = leerCompra(clave);
  if (!compra?.resumen) {
    ctx.ir('compra.html', 'Completa los datos de entrega para revisar tu compra.');
    return;
  }
  const form = ctx.main.querySelector('[data-confirmar]');
  const boton = form.querySelector('button');
  const editar = ctx.main.querySelector('[data-editar]');
  function controles() {
    editar.hidden = Boolean(compra.intento);
    form.elements.resultado.disabled = Boolean(compra.intento);
    if (compra.intento) form.elements.resultado.value = compra.intento.aprobado ? 'aprobado' : 'rechazado';
    boton.textContent = compra.intento ? 'Reintentar confirmación' : 'Confirmar compra';
    boton.disabled = false;
  }
  // Una respuesta perdida se recupera con la referencia original incluso si el carrito ya está vacío.
  if (!compra.intento) {
    try {
      const actual = await ctx.api('/checkout/resumen', { method:'POST', body:compra.resumen.entrega });
      if (!ctx.activo()) return;
      if (actual.resumenHash !== compra.resumen.resumenHash) mensaje(ctx, 'La compra cambió. Revisa el nuevo resumen.');
      compra.resumen = actual;
      sessionStorage.setItem(clave, JSON.stringify(compra));
    } catch (error) {
      if (error.status !== 409) throw error;
      ctx.ir('carrito.html', error.message);
      return;
    }
  }
  pintarResumen(compra.resumen, ctx);
  controles();
  form.onsubmit = async event => {
    event.preventDefault();
    if (boton.disabled) return;
    boton.disabled = true;
    try {
      if (!compra.intento) {
        const bytes = crypto.getRandomValues(new Uint8Array(16));
        const referenciaConfirmacion = Array.from(bytes, byte => byte.toString(16).padStart(2, '0')).join('');
        compra.intento = { referenciaConfirmacion, aprobado:form.elements.resultado.value === 'aprobado' };
        sessionStorage.setItem(clave, JSON.stringify(compra));
      }
      editar.hidden = true;
      form.elements.resultado.disabled = true;
      mensaje(ctx, 'Procesando el pago simulado…');
      const respuesta = await ctx.api('/pedidos', { method:'POST', body:{
        entrega:compra.resumen.entrega, resumenHash:compra.resumen.resumenHash, ...compra.intento
      }});
      if (!ctx.activo()) return;
      if (respuesta.resultado === 'APROBADO') {
        sessionStorage.removeItem(clave);
        ctx.ir('pedido.html?id=' + encodeURIComponent(respuesta.pedido.id), 'Compra confirmada. Folio ' + respuesta.pedido.folio);
        return;
      }
      delete compra.intento;
      sessionStorage.setItem(clave, JSON.stringify(compra));
      mensaje(ctx, 'Pago rechazado. Tu carrito se conserva. Puedes elegir otro resultado e intentarlo de nuevo.');
    } catch (error) {
      if (!ctx.activo() || error.status === 401) return;
      if (error.status === 409) {
        delete compra.intento;
        sessionStorage.setItem(clave, JSON.stringify(compra));
        ctx.ir('resumen.html', 'La compra cambió. Revisa los datos antes de confirmar de nuevo.');
        return;
      }
      mensaje(ctx, error.message + ' Puedes consultar tus pedidos o reintentar la misma confirmación.');
    }
    controles();
  };
}

async function historial(ctx) {
  const pedidos = await ctx.api('/pedidos');
  if (!ctx.activo()) return;
  ctx.main.querySelector('[data-vacio]').hidden = pedidos.length > 0;
  ctx.main.querySelector('[data-historial]').hidden = pedidos.length === 0;
  ctx.main.querySelector('[data-filas]').innerHTML = pedidos.map(p =>
    `<tr><th scope="row"><a href="pedido.html?id=${encodeURIComponent(p.id)}">${ctx.esc(p.folio)}</a></th>
      <td>${fechas.format(new Date(p.fecha))}</td><td>${ctx.esc(estado(p.estado))}</td><td>${ctx.dinero(p.totalMxn)}</td></tr>`
  ).join('');
}

async function detalle(id, ctx) {
  if (!/^\d+$/.test(id)) throw new Error('Selecciona un pedido desde Mis pedidos.');
  const pedido = await ctx.api('/pedidos/' + encodeURIComponent(id));
  if (!ctx.activo()) return;
  ctx.main.querySelector('[data-folio]').textContent = pedido.folio;
  ctx.main.querySelector('[data-fecha]').textContent = fechas.format(new Date(pedido.fecha)) + ' · ' + estado(pedido.estado);
  articulos(pedido, ctx);
  destino(pedido.entrega, ctx.main);
  ctx.main.querySelector('[data-pedido]').hidden = false;
}

export async function compras(vista, id, ctx) {
  const clave = 'motostock.compra.' + ctx.usuario().id;
  switch (vista) {
    case 'carrito': return carrito(ctx);
    case 'compra': return entrega(ctx, clave);
    case 'resumen': return revision(ctx, clave);
    case 'pedidos': return historial(ctx);
    case 'pedido': return detalle(id, ctx);
  }
}
