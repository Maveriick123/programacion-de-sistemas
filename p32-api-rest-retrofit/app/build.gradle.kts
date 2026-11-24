import java.util.Properties
// Para leer ficheros clave-valor, en este caso local.properties

plugins {
    alias(libs.plugins.android.application)
}

// El api-key se lee de local.properties
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        // Se abre, carga y cierra solo
        file.inputStream().use { load(it) }
    }
}

android {
    namespace = "es.udc.psi_2025_26.p32quinteiro"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "es.udc.psi_2025_26.p32quinteiro"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Al compilar Gradle genera la clase BuildConfig
        // Que tendrá la api-key como constante
        buildConfigField(
            "String",
            "API_NINJAS_KEY",
            // \" es necesario porque se pega el valor tal cual
            // y la api-key debe de quedar entre comillas
            "\"${localProperties.getProperty("apininjas.apikey", "")}\""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true // Como siempre por cada layout genera una clase
        buildConfig = true // Para crear la clase BuildConfig
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    // Retrofit (Cliente HTTP)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Converter: JSON → Java (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // Logging interceptor (para debug)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // RecyclerView (el profe también la pone aparte)
    implementation("androidx.recyclerview:recyclerview:1.4.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}