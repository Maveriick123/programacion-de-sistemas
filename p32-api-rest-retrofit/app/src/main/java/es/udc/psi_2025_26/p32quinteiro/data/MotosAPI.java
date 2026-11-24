package es.udc.psi_2025_26.p32quinteiro.data;

import java.util.List;

import es.udc.psi_2025_26.p32quinteiro.domain.Moto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Cada método es un endpoint del API
// La cabecera con la API KEY la pone el MotosApiInterceptor, aquí no hace falta
// La API devuelve siempre un array JSON, por eso List<Moto>
public interface MotosAPI {

    // Petición 1: https://api.api-ninjas.com/v1/motorcycles?make=kawasaki
    // Lista de motos de una marca, para el recycler
    @GET("motorcycles")
    // ?make=kawasaki
    Call<List<Moto>> getMotosByMake(@Query("make") String make);

    // Petición 2: https://api.api-ninjas.com/v1/motorcycles?make=kawasaki&model=Ninja 650&year=2022
    // Ficha de una moto concreta, para el fragmento del detalle
    // Retrofit ya codifica los espacios (Ninja%20650)
    @GET("motorcycles")
    // Los espacios se codifican como %20
    //?make=Kawasaki&model=Brute%20Force%20300&year=2022.
    Call<List<Moto>> getMotoDetail(@Query("make") String make,
                                   @Query("model") String model,
                                   @Query("year") String year);
}
