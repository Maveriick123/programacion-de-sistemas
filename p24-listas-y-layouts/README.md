# P24 · Listas y layouts

Gestor de una lista de artículos con **RecyclerView** y una pantalla de detalle editable. A partir de esta práctica, todo el acceso a las vistas se hace con **View Binding**, sin `findViewById`.

## Qué hace

- Lista desplazable de artículos, cada uno con título y subtítulo en tamaño menor.
- **Borrar todo** vacía la lista.
- **Añadir** abre un diálogo para indicar cuántos elementos crear (10 por defecto). La numeración continúa desde el tamaño actual de la lista.
- Al pulsar un artículo se abre una **actividad de detalle** (`Tile`) con título, subtítulo y descripción:
  - **Editar** habilita la edición de los tres campos.
  - **OK** devuelve los cambios.
  - **Eliminar** cierra la pantalla e indica que hay que borrar el elemento.
- Al volver, la lista principal se **actualiza sola** e informa del tipo de cambio y de la posición afectada.

## Implementación

- **`Article`** es el modelo de datos e implementa `Parcelable` para poder enviarse entre actividades.
- **`ArticlesAdapter`** es el adaptador del `RecyclerView`, con su `ViewHolder`. Las notificaciones son específicas (inserción, cambio o borrado de un elemento) en lugar de redibujar toda la lista.
- La respuesta de la actividad de detalle se recoge con la **Activity Result API** (`registerForActivityResult`).

## Archivos

```
app/src/main/java/.../MainActivity.java     Lista y diálogo de alta
app/src/main/java/.../ArticlesAdapter.java  Adaptador del RecyclerView
app/src/main/java/.../Article.java          Modelo Parcelable
app/src/main/java/.../Tile.java             Detalle y edición
docs/enunciado.pdf
```
