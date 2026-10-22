package com.unir.myyard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.unir.myyard.service.RequestAPI;
import com.unir.myyard.util.ImageUtils;
import com.unir.myyard.util.Picture;

import okhttp3.MultipartBody;

public class CatalogFragment extends Fragment {

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private TextView tvResultado;
    private Button btnCapturarFoto;

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickerLauncher = Picture.criarLauncher(this, fileFoto -> {
            if (tvResultado != null) tvResultado.setText("Identificando planta...");

            MultipartBody.Part bodyImagem = ImageUtils.converterFileParaMultipart(fileFoto);
            RequestAPI.enviarParaPlantNet(bodyImagem, respostaOuErro -> {
                if (isAdded() && tvResultado != null) tvResultado.setText(respostaOuErro);
            });
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvResultado = view.findViewById(R.id.tvResultado);
        btnCapturarFoto = view.findViewById(R.id.btnFoto);

        if (btnCapturarFoto != null) {
            btnCapturarFoto.setOnClickListener(v -> {
                Picture.tirarFoto((AppCompatActivity) requireActivity(), imagePickerLauncher);
            });
        }
    }
}