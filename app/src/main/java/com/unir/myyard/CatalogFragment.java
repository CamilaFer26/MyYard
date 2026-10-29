package com.unir.myyard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;
import com.unir.myyard.service.RequestAPI;
import com.unir.myyard.util.ImageUtils;
import com.unir.myyard.util.Picture;

import java.io.File;

import okhttp3.MultipartBody;

public class CatalogFragment extends Fragment {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private TextView tvResultado;
    private ShapeableImageView btnCapturarFoto;
    private ShapeableImageView btnSalvar;
    private File fileImage;

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickerLauncher = Picture.criarLauncher(this, fileFoto -> {
            this.fileImage = fileFoto;

            if (tvResultado != null) tvResultado.setText("Identificando planta...");

            // converter file em bitmap
            String filePath = fileFoto.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            if(getView() != null){
                ImageView imageView = this.getView().findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            }

            if(btnSalvar != null){
                btnSalvar.setVisibility(View.VISIBLE);
                btnSalvar.setClickable(true);
            }

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
        btnSalvar = view.findViewById(R.id.btnSave);

        btnSalvar.setVisibility(View.INVISIBLE);
        btnSalvar.setClickable(false);

        if (btnCapturarFoto != null) {
            btnCapturarFoto.setOnClickListener(v -> {
                Picture.tirarFoto((AppCompatActivity) requireActivity(), imagePickerLauncher);
            });
        }

    }
}