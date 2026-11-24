# P26 · Interfaz avanzada

Aplicación que reúne los elementos de interfaz que interactúan con el sistema: **menús, notificaciones, Snackbars y servicios en primer plano**, gestionando los **permisos en tiempo de ejecución**.

## Qué hace

- **Menú de opciones** con cinco entradas, definido como recurso XML. Dos son visibles en la barra superior (Add y Snack) y tres quedan en el menú desplegable (Clear, Msg y FGS).
- **Add** lanza una **notificación** con tres botones. Cualquiera de ellos abre la aplicación y muestra cuál se pulsó. **Clear** cancela la notificación.
- **Snack** muestra un `Snackbar` con acción y **Msg** un `Toast`.
- **Lista con menú contextual** (pulsación larga) con las opciones *Delete* y *Share*. Al borrar aparece un `Snackbar` con **Deshacer**.
- **FGS** arranca un ***foreground service*** que cuenta hasta 10 en un hilo interrumpible. Solo puede haber uno activo, y al pulsar su notificación se detienen el servicio y el hilo.

## Puntos clave

- **Canal de notificaciones**: se crea al iniciar la aplicación, porque desde Android 8 las notificaciones sin canal no se muestran.
- **Permisos en tiempo de ejecución**: antes de notificar o arrancar el servicio se comprueba `POST_NOTIFICATIONS`, y si falta se solicita. Si el usuario lo deniega, la aplicación sigue funcionando y se lo indica.
- **`PendingIntent` con códigos de petición distintos** para cada botón de la notificación. Android considera iguales dos *intents* que solo se diferencian en los *extras*, así que sin códigos distintos todos los botones devolverían el último.

## Archivos

```
app/src/main/java/.../MainActivity.java        Menús, notificaciones, lista y permisos
app/src/main/java/.../ForegroundService.java   Servicio en primer plano
app/src/main/res/menu/                         Menú de opciones y contextual
docs/enunciado.pdf
```
