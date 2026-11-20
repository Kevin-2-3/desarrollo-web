import { catalogo } from './catalogo.js';

const main = document.querySelector('main');
const salida = document.querySelector('#salir');
const cartel = document.querySelector('#aviso');
const vista = document.body.dataset.vista;
const parametros = new URLSearchParams(location.search);
const privadas = ['carrito', 'compra', 'resumen', 'pedidos', 'pedido'];
const moneda = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' });
const entidades = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' };
const esc = valor => String(valor ?? '').replace(/[&<>"']/g, letra => entidades[letra]);
let cliente = null;
let saliendo = false;
let temporizador;

function aviso(texto) {
  clearTimeout(temporizador);
  cartel.textContent = texto;
  if (texto) temporizador = setTimeout(() => { cartel.textContent = ''; }, 8000);
}

function ir(destino, mensaje) {
  if (mensaje) sessionStorage.setItem('motostock.aviso', mensaje);
  saliendo = true;
  location.assign(destino);
}

function destinoTrasLogin() {
  const ruta = parametros.get('volver') || '';
  return /^\/(catalogo|producto|carrito|compra|resumen|pedidos|pedido)\.html(?:\?[^#]*)?$/.test(ruta)
    ? ruta : '/catalogo.html';
}

function pedirLogin(mensaje = 'Inicia sesión para continuar.') {
  const query = new URLSearchParams({ volver: location.pathname + location.search });
  ir('login.html?' + query, mensaje);
}

function limpiarCompra() {
  if (cliente) sessionStorage.removeItem('motostock.compra.' + cliente.id);
}

async function solicitar(path, opciones = {}) {
  let respuesta;
  try {
    respuesta = await fetch('/api/v1' + path, { cache: 'no-store', credentials: 'same-origin', ...opciones });
  } catch {
    throw new Error('No se pudo conectar con Motostock. Revisa la conexión e inténtalo de nuevo.');
  }
  if (respuesta.status === 204) return null;
  let datos;
  try { datos = await respuesta.json(); }
  catch { throw new Error('La tienda no recibió una respuesta válida. Vuelve a intentarlo.'); }
  if (!respuesta.ok) {
    const error = new Error(datos.error || 'No se pudo completar la solicitud.');
    error.status = respuesta.status;
    error.campos = datos.campos;
    throw error;
  }
  return datos;
}

async function api(path, { method = 'GET', body } = {}) {
  if (method === 'GET') return solicitar(path);
  const { token } = await solicitar('/auth/csrf');
  const headers = { 'X-CSRF-TOKEN': token };
  if (body !== undefined) headers['Content-Type'] = 'application/json';
  return solicitar(path, { method, headers, body: JSON.stringify(body) });
}

function navegacion(vista) {
  const grupo = ({ producto:'catalogo', compra:'carrito', resumen:'carrito', pedido:'pedidos', registro:'login' })[vista] || vista;
  document.querySelectorAll('[data-nav]').forEach(enlace => {
    if (enlace.dataset.nav === grupo) enlace.setAttribute('aria-current','page');
    else enlace.removeAttribute('aria-current');
  });
  document.querySelector('[data-nav="pedidos"]').hidden = true;
  document.querySelector('[data-nav="login"]').hidden = Boolean(cliente);
  salida.hidden = !cliente;
}


function cuenta() {
  const registro = vista === 'registro';
  const form = main.querySelector('form');
  const boton = form.querySelector('button');
  const destino = destinoTrasLogin();
  const alternar = main.querySelector('.cuenta p a');
  alternar.search = new URLSearchParams({volver: destino}).toString();
  boton.disabled = false;
  form.oninput = evento => {
    evento.target.setCustomValidity('');
    if (registro && evento.target.name === 'password') form.elements.confirmacionPassword.setCustomValidity('');
  };
  form.onsubmit = async evento => {
    evento.preventDefault();
    if (boton.disabled) return;
    const datos = Object.fromEntries(new FormData(form));
    if (registro) {
      datos.nombre = datos.nombre.trim();
      if (!datos.nombre) form.elements.nombre.setCustomValidity('Escribe tu nombre.');
      if (datos.password !== datos.confirmacionPassword) form.elements.confirmacionPassword.setCustomValidity('Las contraseñas deben coincidir.');
      if (new TextEncoder().encode(datos.password).length > 72) form.elements.password.setCustomValidity('Esta contraseña supera el límite de 72 bytes.');
    }
    if (!form.reportValidity()) return;
    boton.disabled = true;
    form.querySelector('.error').textContent = '';
    try {
      await api(registro ? '/auth/register' : '/auth/login', {method: 'POST', body: datos});
      if (registro) ir('login.html?' + new URLSearchParams({volver: destino}), 'Cuenta creada. Ya puedes iniciar sesión.');
      else ir(destino, 'Sesión iniciada.');
    } catch (error) {
      form.querySelector('.error').textContent = error.message;
      boton.disabled = false;
    }
  };
}

async function iniciar() {
  const sesion = await api('/auth/sesion');
  cliente = sesion.autenticado ? sesion : null;
  navegacion(vista);
  document.querySelector('[data-nav="carrito"]').hidden = true;
  if (!cliente && privadas.includes(vista)) return pedirLogin();
  if (vista === 'login' || vista === 'registro') {
    if (cliente) ir(destinoTrasLogin());
    else cuenta();
    return;
  }
  const ctx = {
    main, esc, dinero: valor => moneda.format(valor), activo: () => !saliendo,
    aviso, ir, login: pedirLogin, usuario: () => cliente,
    api: async (path, opciones) => {
      try { return await api(path, opciones); }
      catch (error) {
        if (error.status === 401 && !saliendo) {
          limpiarCompra();
          pedirLogin('Tu sesión terminó. Inicia sesión para continuar.');
        }
        throw error;
      }
    }
  };
  const id = parametros.get('id');
  await catalogo(vista, id, ctx);
}

salida.onclick = async () => {
  salida.disabled = true;
  try { await api('/auth/logout', {method:'POST'}); }
  catch (error) {
    if (error.status !== 401) {
      aviso(error.message);
      salida.disabled = false;
      return;
    }
  }
  limpiarCompra();
  ir('catalogo.html', 'Sesión cerrada.');
};

const mensaje = sessionStorage.getItem('motostock.aviso');
if (mensaje) {
  aviso(mensaje);
  sessionStorage.removeItem('motostock.aviso');
}
window.addEventListener('pagehide', () => { saliendo = true; });
window.addEventListener('pageshow', evento => { if (evento.persisted) location.reload(); });

iniciar().catch(error => {
  if (saliendo) return;
  main.innerHTML = `<section class="panel"><h1>No se pudo abrir la página</h1><p role="alert">${esc(error.message)}</p>
    <div class="acciones"><button class="boton">Reintentar</button><a href="catalogo.html">Ir al catálogo</a></div></section>`;
  main.querySelector('button').onclick = () => location.reload();
});
