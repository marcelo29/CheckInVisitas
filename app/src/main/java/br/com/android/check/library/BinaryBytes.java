package br.com.android.check.library;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by masasp29 on 15/02/17.
 */

public class BinaryBytes {

    public static byte[] getResourceInBytes(Bitmap img) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return (byteArray);
    }
}