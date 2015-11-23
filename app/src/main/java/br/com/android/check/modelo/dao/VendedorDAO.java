package br.com.android.check.modelo.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import br.com.android.check.modelo.bean.Vendedor;
import br.com.android.check.ws.ConfiguracoesWS;
import br.com.android.check.ws.WebServiceCliente;

public class VendedorDAO {

    //ws ok
    private String url = ConfiguracoesWS.URL_APLICACAO + "vendedor/";

    public VendedorDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public Boolean inserirVendedor(Vendedor vendedor) {
        Boolean flag = false;
        try {
            String vendedorJson = new Gson().toJson(vendedor);
            String[] resposta = new WebServiceCliente().post(url + "inserir", vendedorJson);

            if (resposta[0].equals("200")) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public Vendedor retornaVendedorPorNome(String nome) {
        Vendedor vendedor = null;
        try {
            String[] resposta = new WebServiceCliente().get(url + "retornaVendedorPorNome/" + nome, false);
            vendedor = new Gson().fromJson(resposta[1], Vendedor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendedor;
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

                if (!json.contains("[")) {
                    StringBuilder stringBuilder = new StringBuilder(json);
                    stringBuilder.insert(10, "[");
                    stringBuilder.insert(json.length(), "]");

                    json = stringBuilder.toString();
                }

                Gson g = new Gson();

                JsonParser parser = new JsonParser();

                JsonArray array = null;

                array = parser.parse(json).getAsJsonObject().getAsJsonArray("vendedores");

                for (int i = 0; i < array.size(); i++) {
                    Vendedor vendedor = g.fromJson(array.get(i), Vendedor.class);
                    lista.add(vendedor);
                }
            }
        } catch (Exception e) {
            Log.e("ErroListaVendedor", e.getMessage());
        }

        return lista;
    }

}