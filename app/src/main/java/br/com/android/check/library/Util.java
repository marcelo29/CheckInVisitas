package br.com.android.check.library;

import android.app.AlertDialog;
import android.content.Context;

import br.com.android.check.R;

public class Util {

    public static final String AVISO_CAMPO_OBRIGATORIO = "Campo obrigatório";

    public static void showAviso(Context ctx, int msg) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctx);
        dialogo.setMessage(msg);
        dialogo.setIcon(R.drawable.ic_exclamacao_preto);
        dialogo.setNeutralButton("Ok", null);
        dialogo.show();
    }
}