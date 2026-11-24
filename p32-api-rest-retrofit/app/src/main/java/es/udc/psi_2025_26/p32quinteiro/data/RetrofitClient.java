package es.udc.psi_2025_26.p32quinteiro.data;

import android.util.Log;

import java.util.List;

import es.udc.psi_2025_26.p32quinteiro.BuildConfig;
import es.udc.psi_2025_26.p32quinteiro.MainActivity;
import es.udc.psi_2025_26.p32quinteiro.data.interceptor.MotosApiInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Singleton, sólo se crea la primera vez
// Los dos fragmentos piden aquí la API, así sólo se crea Retrofit una vez
public class RetrofitClient {

    // Tiene que acabar en / !!
    private static final String BASE_URL = "https://api.api-ninjas.com/v1/";
    private static MotosAPI instance;

    // synchronized para que no la creen dos hilos a la vez
    // es decir uno espera
    public static synchronized MotosAPI getInstance() {

        if (instance == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                    message -> Log.d(MainActivity.TAG, message))
                    // Al ejecutar en AndroidStudio siempre se pone el modo DEBUG
                    // Si generasemos una apk de realese, entonces sería NONE y ya no sale nada en el logcat
                    .setLevel(BuildConfig.DEBUG? HttpLoggingInterceptor.Level.BODY: HttpLoggingInterceptor.Level.NONE);

            // Se oculta la cabecera de la api-key
            logging.redactHeader("X-Api-Key");

            // Creación del cliente OkHttp
            OkHttpClient.Builder okHttp = new OkHttpClient.Builder();
            List<Interceptor> interceptors = okHttp.interceptors();
            // Los interceptores se ejecutan en orden
            // Primero el que pone los headers
            // Después el del log, para mostrar la petición con las cebeceras
            interceptors.add(new MotosApiInterceptor());
            interceptors.add(logging);

            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttp.build())
                    .addConverterFactory(GsonConverterFactory.create()) // JSON -> Moto
                    .build().create(MotosAPI.class);
        }
        return instance;
    }
}
