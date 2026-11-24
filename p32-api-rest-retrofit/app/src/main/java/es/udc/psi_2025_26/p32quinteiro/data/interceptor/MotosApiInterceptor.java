package es.udc.psi_2025_26.p32quinteiro.data.interceptor;

import java.io.IOException;

import androidx.annotation.NonNull;
import es.udc.psi_2025_26.p32quinteiro.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

// Se mete en medio de TODAS las peticiones antes de salir al servidor
// y les añade las cabeceras, así no hay que repetir @Headers en cada método de la interfaz

public class MotosApiInterceptor implements Interceptor {

    private static final String API_KEY_HEADER = "X-Api-Key";
    private static final String ACCEPT_HEADER = "Accept";

    // Viene de local.properties (apininjas.apikey)
    private static final String API_KEY = BuildConfig.API_NINJAS_KEY;
    private static final String JSON_RESPONSE = "application/json";


    // El cliente OkHttp llama a este método en cada petición
    @NonNull
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {

        // La petición tal cual la monta Retrofit a partir de MotosAPI
        Request original = chain.request();

        // No se puede modificar la original, así que se hace una copia
        Request.Builder requestBuilder = original.newBuilder();

        requestBuilder.header(API_KEY_HEADER, API_KEY); // La API KEY, sin ella da 401
        requestBuilder.header(ACCEPT_HEADER, JSON_RESPONSE); // Queremos JSON como respuesta

        Request request = requestBuilder.build();
        // Sigue la cadena, la manda y devuelve la respuesta
        return chain.proceed(request);
    }
}
