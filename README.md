# Programación de Sistemas: desarrollo de aplicaciones Android

Prácticas de la asignatura **Programación de Sistemas** (Grado en Ingeniería Informática, Universidade da Coruña, curso 2026/2027).

Cada práctica es una aplicación Android nativa en **Java** que se centra en un bloque del framework. Empiezan por el ciclo de vida de las actividades y terminan consumiendo una API REST con Retrofit. Cada carpeta contiene un proyecto completo de Android Studio con su README y su enunciado.

## Contenido

| Práctica | Tema | Conceptos principales |
|---|---|---|
| [P21 · Actividades y recursos](p21-actividades-y-recursos) | Fundamentos | Ciclo de vida, recursos, internacionalización, layouts por orientación |
| [P22 · Intents](p22-intents) | Comunicación entre componentes | Intents explícitos e implícitos, `ACTION_SEND`, resultados entre actividades, *intent filters* |
| [P23 · Fragments y diálogos](p23-fragments-y-dialogos) | Interfaz modular | Fragmentos dinámicos, comunicación fragmento-actividad, `WebView`, `AlertDialog` |
| [P24 · Listas y layouts](p24-listas-y-layouts) | Colecciones de datos | `RecyclerView`, View Binding, `Parcelable`, Activity Result API |
| [P25 · Trabajo en segundo plano](p25-trabajo-en-segundo-plano) | Concurrencia | *Broadcast receivers*, servicios iniciados y enlazados, hilos |
| [P26 · Interfaz avanzada](p26-interfaz-avanzada) | Interacción con el sistema | Menús, notificaciones, permisos en tiempo de ejecución, *foreground service* |
| [**P32 · API REST con Retrofit**](p32-api-rest-retrofit) | Red | Retrofit, OkHttp, interceptores, gestión segura de la clave de API |

## Tecnologías

- **Java** · Android SDK (minSdk 26, targetSdk 37) · Gradle con Kotlin DSL
- AndroidX: AppCompat, Fragments, RecyclerView, Material Components
- Retrofit 2 + Gson + OkHttp

## Ejecución

Abrir la carpeta de la práctica en **Android Studio** y ejecutarla en un emulador o dispositivo. La P32 necesita además una clave de API (ver su README).
