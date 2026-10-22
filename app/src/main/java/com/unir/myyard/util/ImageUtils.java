package com.unir.myyard.util;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageUtils {
    public static MultipartBody.Part converterFileParaMultipart(File file) {
        // Define o RequestBody com o MIME type correto para imagem
        RequestBody requestFile = RequestBody.create(
                file,
                MediaType.parse("image/*")
        );

        return MultipartBody.Part.createFormData("images", file.getName(), requestFile);
    }
}
