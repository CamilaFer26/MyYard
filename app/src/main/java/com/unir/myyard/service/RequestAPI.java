package com.unir.myyard.service;

import com.google.gson.JsonObject;
import com.unir.myyard.BuildConfig;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// centraliza a execução da chamada HTTP
public class RequestAPI {
    public static void enviarParaPlantNet(MultipartBody.Part bodyImagem, PlantNetCallback callback) {
        PlantNetApi apiService = RetrofitClient.getPlantNetApi();

        Call<PlantNetResponse> call = apiService.identificarPlanta(
                BuildConfig.PLANTNET_API_KEY,
                "pt",
                bodyImagem
        );

        call.enqueue(new Callback<PlantNetResponse>() {
            @Override
            public void onResponse(Call<PlantNetResponse> call, Response<PlantNetResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlantNetResponse respostaApi = response.body();

                    if(respostaApi.getResults() != null && !respostaApi.getResults().isEmpty()){
                        PlantNetResponse.Result melhorResultado = respostaApi.getResults().get(0);
                        double precisao = melhorResultado.getScore() * 100;

                        // dados botânicos
                        PlantNetResponse.Species especia = melhorResultado.getSpecies();
                        String nomeCientifico = especia.getScientificName();

                        // lista de nomes comuns
                        String nomePopular = "Não informado";
                        if (especia.getCommonNames() != null && !especia.getCommonNames().isEmpty()) {
                            nomePopular = especia.getCommonNames().get(0); // pega o primeiro nome popular
                        }

                        String resultadoFormatado = String.format("Nome Popular: %s\nNome Científico: %s\nPrecisão: %.1f%%",
                                                        nomePopular, nomeCientifico, precisao);
                        callback.onResultado(resultadoFormatado);
                    } else {
                        callback.onResultado("Nenhuma planta identificada na imagem");
                    }
                } else{
                    callback.onResultado("Erro na API: Código " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PlantNetResponse> call, Throwable t) {
                callback.onResultado("Falha de conexão: " + t.getMessage());
            }
        });
    }

    public interface PlantNetCallback {
        void onResultado(String resposta);
    }
}
