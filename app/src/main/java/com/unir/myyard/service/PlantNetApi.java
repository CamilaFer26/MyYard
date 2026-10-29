package com.unir.myyard.service;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

// funciona como contratp formal de mapeamento da api
public interface PlantNetApi {
    @Multipart  // indica ao retrofit como formatar a requisição
    @POST("v2/identify/all")    // indica o método http e o end point
    Call<PlantNetResponse> identificarPlanta(         // define o tipo de retorno assíncrono da API
            @Query("api-key") String apiKey,    // @Query adiciona parâmetros na URL de requisição
            @Query("lang") String idioma,
            @Part MultipartBody.Part imagem
    );
}
