# P22 · Intents

Aplicación para practicar el mecanismo de **Intents**, que es la forma en que se comunican las actividades entre sí y con otras aplicaciones.

## Qué hace

La pantalla principal tiene un campo de texto y tres botones. Si el campo está vacío, se avisa con un `Toast` y no se hace nada.

- **Compartir:** *intent* implícito con `ACTION_SEND`. Abre el selector del sistema (`createChooser`) con todas las aplicaciones capaces de recibir texto plano.
- **Continuar:** *intent* explícito que abre una segunda actividad y le pasa el texto como *extra*.
- **Reacción:** abre una tercera actividad con tres botones. Cada uno cierra la actividad y **devuelve un resultado** a la principal, que muestra qué botón se pulsó.

Además, las dos actividades secundarias declaran un ***intent filter*** para texto plano y están exportadas, así que aparecen en la lista de "Compartir" de cualquier aplicación del dispositivo, incluida esta.

## Archivos

```
app/src/main/java/.../MainActivity.java, NextActivity.java, ReactActivity.java
app/src/main/AndroidManifest.xml     Intent filters de las actividades
docs/enunciado.pdf
```
