package com.unir.myyard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unir.myyard.service.RequestAPI;
import com.unir.myyard.util.ImageUtils;
import com.unir.myyard.util.Picture;

import okhttp3.MultipartBody;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private Button btnSelecionarFoto;
    private TextView tvResultado;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // inicializa launcher de Picture
        imagePickerLauncher = Picture.criarLauncher(this, fileFoto -> {
            tvResultado.setText("Identificando planta...");

            // converte file em Multipart e envia para a API
            MultipartBody.Part bodyImagem = ImageUtils.converterFileParaMultipart(fileFoto);
            RequestAPI.enviarParaPlantNet(bodyImagem, respostaOuErro -> {
                tvResultado.setText(respostaOuErro);
            });
        });

        btnSelecionarFoto = findViewById(R.id.btnFoto);
        tvResultado = findViewById(R.id.textView);

        btnSelecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picture.tirarFoto(MainActivity.this, imagePickerLauncher);
            }
        });
    }
}