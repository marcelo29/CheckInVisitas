package br.com.android.check.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

public class Util extends Activity {

    public static void showMessage(Context ctx, String msg) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctx);
        dialogo.setMessage(msg);
        dialogo.setNeutralButton("Ok", null);
        dialogo.show();
    }


    public static void showAviso(Context ctx, int msg) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctx);
        dialogo.setMessage(msg);
        dialogo.setNeutralButton("Ok", null);
        dialogo.show();
    }

}