package com.unir.myyard.util;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;

public class Picture {
    public static File getPicture(){
        return new File("a");
    }
}
