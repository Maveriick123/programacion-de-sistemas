# P23 · Fragments y diálogos

Interfaz construida con **fragmentos** que se crean y sustituyen de forma dinámica dentro de una única actividad.

## Qué hace

- **TopFragment**, siempre visible en la parte superior: botones A, B, Url y Clear y un campo de texto.
  - **A / B** muestran el nombre del botón pulsado en un `TextFragment`.
  - **Url** carga la dirección del campo de texto en un `WebFragment` con un `WebView` (por defecto, `https://fic.udc.es`).
  - **Clear** elimina el fragmento inferior que haya en ese momento.
- En la parte inferior solo puede haber un fragmento, el de texto o el web, y se sustituyen entre sí. Se crean **solo cuando hacen falta**.
- `TextFragment` tiene un botón que activa o desactiva el botón Clear de `TopFragment`.
- Al pulsar **atrás**, un `AlertDialog` pide confirmación antes de salir.

## Decisiones de diseño

- **Los fragmentos no se conocen entre sí.** Cada uno declara una interfaz de *listener* con sus eventos, la actividad la implementa y hace de intermediaria. Así cada fragmento es reutilizable en cualquier otra actividad.
- Por el mismo motivo, `WebFragment` valida por sí mismo la URL que recibe, aunque esta aplicación siempre le pase una correcta.
- El estado se conserva al **girar la pantalla**, por ejemplo el último botón pulsado. Hay layouts específicos para horizontal.

## Archivos

```
app/src/main/java/.../MainActivity.java
app/src/main/java/.../TopFragment.java, TextFragment.java, WebFragment.java
docs/enunciado.pdf
```
