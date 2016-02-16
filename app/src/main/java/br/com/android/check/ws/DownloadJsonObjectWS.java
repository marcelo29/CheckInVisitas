package br.com.android.check.ws;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by masasp29 on 16/12/15.
 */
public class DownloadJsonObjectWS {

    public static final int GET = 0, POST = 1;

    public Object validaJson(String link, Object object, int tipoDeRequisicao) {
        Object jsonObject = null;
        try {
            Gson gson = new Gson();

            String[] resposta = new String[2];

            switch (tipoDeRequisicao) {
                case GET:
                    resposta = new WebServiceCliente().get(link, false);
                    break;
                case POST:
                    String json = gson.toJson(object);
                    resposta = new WebServiceCliente().post(link, json);
                    break;
            }

            if (resposta[0].equals("200") && tipoDeRequisicao == GET) {
                jsonObject = gson.fromJson(resposta[1], object.getClass());
            }
            if (resposta[0].equals("200") && tipoDeRequisicao == POST) {
                jsonObject = object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public ArrayList<Object> validaListaJson(String link) {
        ArrayList<Object> lista = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(link, false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new Gson();

                lista = gson.fromJson(json, new TypeToken<ArrayList<Object>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroLista", e.getMessage());
        }

        return lista;
    }

}