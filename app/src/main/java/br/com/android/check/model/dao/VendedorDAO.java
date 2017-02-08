package br.com.android.check.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import br.com.android.check.model.bean.Vendedor;
import br.com.android.check.ws.ConfiguracoesWS;
import br.com.android.check.ws.WebServiceCliente;

public class VendedorDAO {

    // ws ok
    private String url = ConfiguracoesWS.URL_APLICACAO + "vendedor/";

    public VendedorDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public ArrayList<Vendedor> listaVendedores() {
        ArrayList<Vendedor> lista = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "lista", false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new Gson();

                lista = gson.fromJson(json, new TypeToken<ArrayList<Vendedor>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroListaVendedor", e.getMessage());
        }

        return lista;
    }

}