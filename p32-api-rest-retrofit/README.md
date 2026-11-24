# P32 · Consumo de una API REST con Retrofit

Catálogo de motocicletas que obtiene los datos en tiempo real de la API pública de [API Ninjas](https://api-ninjas.com/api/motorcycles). Se busca por marca, los resultados aparecen en una lista y al seleccionar un modelo se muestra su ficha técnica.

## Arquitectura

```
MainActivity (contenedor)
 ├── ListaMotosFragment ──► RecyclerView + MotosAdapter
 └── DetalleMotoFragment
          │
          ▼
 data/RetrofitClient (singleton) ──► MotosAPI (interfaz Retrofit)
          │
          └── OkHttp: MotosApiInterceptor ──► HttpLoggingInterceptor
domain/Moto (modelo, deserializado con Gson)
```

- **Separación en capas.** La interfaz (`fragments`), el acceso a datos (`data`) y el modelo (`domain`) están en paquetes distintos.
- **`MotosAPI`** define los *endpoints* de forma declarativa con anotaciones de Retrofit (`@GET`, `@Query`): uno para listar por marca y otro para el detalle de un modelo y año concretos.
- **`RetrofitClient`** es un *singleton* sincronizado. Los dos fragmentos comparten una única instancia del cliente.
- Las peticiones son **asíncronas** (`enqueue`), así que la red nunca bloquea el hilo principal.
- Los fragmentos se comunican con la actividad mediante una interfaz de *listener*. Al girar la pantalla se restaura el fragmento visible en lugar de crearlo de nuevo.

## Gestión de la clave de API

- La clave **no está en el código ni en el repositorio**. Se lee de `local.properties`, que Git ignora, y Gradle la inyecta en `BuildConfig` al compilar.
- Un **interceptor de OkHttp** añade la cabecera `X-Api-Key` a todas las peticiones, de modo que no se repite en cada *endpoint*.
- El interceptor de *logging* **oculta esa cabecera** en Logcat y solo está activo en las compilaciones de depuración.

## Configuración

Añadir la clave personal de API Ninjas en `local.properties`:

```properties
apininjas.apikey=TU_CLAVE
```

Si falta, la aplicación muestra un aviso en lugar de fallar.

## Archivos

```
app/src/main/java/.../MainActivity.java
app/src/main/java/.../ListaMotosFragment.java, DetalleMotoFragment.java, MotosAdapter.java
app/src/main/java/.../data/       RetrofitClient, MotosAPI, interceptor/MotosApiInterceptor
app/src/main/java/.../domain/     Moto
```
