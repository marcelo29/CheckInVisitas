package br.com.android.check.library;

import android.app.AlertDialog;
import android.content.Context;

import java.io.IOException;

import br.com.android.check.R;

public class Util {

    public static final String AVISO_CAMPO_OBRIGATORIO = "Campo obrigatório";

    public static void showAviso(Context ctx, int msg) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctx);
        dialogo.setIcon(R.drawable.ic_exclamacao_preto);
        dialogo.setMessage(msg);
        dialogo.setNeutralButton("Ok", null);
        dialogo.show();
    }

    public static boolean ipOff(String ip) {
        //System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c " + ip);
            int mExitValue = mIpAddrProcess.waitFor();
            //System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            //System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println(" Exception:" + e);
        }
        return false;
    }
}