# P21 · Actividades y recursos

Primera aplicación de la asignatura, pensada para entender **cómo gestiona Android el ciclo de vida de una actividad** y cómo se separan los recursos del código.

## Qué hace

- Dos actividades, `MainActivity` y `segunda_activ`, ambas configuradas como punto de entrada en el manifiesto y con *task affinity* propia, de modo que aparecen como dos tareas independientes en el lanzador.
- En las dos sobrescribí **todos los métodos del ciclo de vida** (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onRestart`, `onDestroy`) y dejan un mensaje en el log. Así se puede seguir en Logcat qué ocurre al girar la pantalla, cambiar de aplicación o volver atrás.
- Un botón cuyo texto cambia al pulsarlo, con todas las cadenas definidas como recursos.

## Recursos alternativos

- **Internacionalización:** textos en español e inglés (`values/` y `values-en/`). La app cambia de idioma según la configuración del dispositivo.
- **Orientación:** un layout distinto para horizontal (`layout-land/`), que Android selecciona automáticamente al girar el dispositivo.

## Archivos

```
app/src/main/java/.../MainActivity.java, segunda_activ.java
app/src/main/res/layout/, layout-land/, values/, values-en/
docs/enunciado.pdf
```
