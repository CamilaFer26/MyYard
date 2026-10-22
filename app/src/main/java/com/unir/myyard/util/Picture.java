package com.unir.myyard.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.dhaval2404.imagepicker.ImagePicker;

/*
    Classe que centraliza a lógica de captura/seleção de imagens de plantas pelo usuário
    utilizando ImagePicker
 */
public class Picture {
    // Listener para notificar quando a imagem estiver pronta
    public interface OnFotoCapturadaListener {
        void onFotoRecebida(File arquivoFoto);
    }

    // Launcher que "escuta" a resposta do imagepicker
    public static ActivityResultLauncher<Intent> criarLauncher(AppCompatActivity activity, OnFotoCapturadaListener listener) {
        return activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri uriFoto = data.getData();

                        if (uriFoto != null) {
                            // converte uri em file temporário
                            File fileFoto = converterUriParaFile(activity, uriFoto);

                            if (fileFoto != null) {
                                listener.onFotoRecebida(fileFoto);
                            } else {
                                Toast.makeText(activity, "Erro ao processar imagem capturada", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // iniciar seletor e câmera do imagepicker
    public static void tirarFoto(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher) {
        ImagePicker.with(activity)
                .crop(1f, 1f)   // cortar imagem
                .compress(1024) // limite do arquivo em 1mb
                .maxResultSize(1080, 1080)  // resolução máxima
                .createIntent(intent -> {
                    launcher.launch(intent);
                    return null;
                });
    }


    // copia o conteúdo da uri para um arquivo temporário em cache
    private static File converterUriParaFile(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            File tempFile = File.createTempFile("plant_picture_", ".jpg", context.getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            if (inputStream != null) inputStream.close();

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}