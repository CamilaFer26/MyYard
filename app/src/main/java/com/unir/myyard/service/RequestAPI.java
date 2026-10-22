package com.unir.myyard.service;

import com.google.gson.JsonObject;
import com.unir.myyard.BuildConfig;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAPI {
    public static void enviarParaPlantNet(MultipartBody.Part bodyImagem, PlantNetCallback callback) {
        PlantNetApi apiService = RetrofitClient.getPlantNetApi();

        Call<JsonObject> call = apiService.identificarPlanta(
                BuildConfig.PLANTNET_API_KEY,
                "pt",
                bodyImagem
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Retorna a resposta completa em formato de texto JSON (String)
                    callback.onResultado(response.body().toString());
                } else{
                    callback.onResultado("Erro na API: Código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onResultado("Falha de conexão: " + t.getMessage());
            }
        });
    }

    public interface PlantNetCallback {
        void onResultado(String resposta);
    }
}
