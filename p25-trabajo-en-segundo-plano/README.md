# P25 · Trabajo en segundo plano

Aplicación que compara las distintas formas de ejecutar trabajo fuera del hilo principal en Android y de comunicar el resultado a la interfaz.

## Qué hace

- **Broadcast receiver** que escucha dos emisiones y muestra sus datos en un `Toast`:
  - una del sistema, `USER_PRESENT`, que se recibe al desbloquear el dispositivo;
  - una propia, `es.udc.PSI.broadcast.GENERAL`, que se lanza con un botón.
- **Servicio iniciado** (`LocalService`), que se arranca y detiene con un *switch*. Cuenta hasta el valor indicado con una espera de 0,5 s entre pasos. Al terminar envía una emisión propia para que la actividad ponga el *switch* en "off".
- **Servicio enlazado** (`BindService`), que hace una cuenta descendente. La actividad se enlaza a él con `bindService` y, gracias a su `Binder`, puede **leer el valor actual del contador (Get) y modificarlo (Set)** mientras se ejecuta.
- **Tarea en segundo plano** con un hilo, que muestra en pantalla el progreso de la cuenta.

## Puntos clave

- Cada servicio ejecuta su trabajo en un **hilo propio** (`CountThread`). Un servicio corre por defecto en el hilo principal, y dormir ahí bloquearía la interfaz.
- Los hilos son **interrumpibles**: al detener el servicio se interrumpe el hilo de forma limpia.
- Los receptores se registran y se dan de baja siguiendo el ciclo de vida de la actividad, para no dejar fugas.

## Archivos

```
app/src/main/java/.../MainActivity.java   Interfaz, receptores y tarea en segundo plano
app/src/main/java/.../LocalService.java   Servicio iniciado
app/src/main/java/.../BindService.java    Servicio enlazado con Binder
docs/enunciado.pdf
```
