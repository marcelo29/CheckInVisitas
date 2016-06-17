package br.com.android.check.controler;

public class ValidaCamposObrigatorios {

    public static boolean seCampoEstaNuloOuEmBranco(String campo) {
        return (campo == null || campo.equals(""));
    }

}