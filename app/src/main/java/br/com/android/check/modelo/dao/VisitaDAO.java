package br.com.android.check.modelo.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.ws.ConfiguracoesWS;
import br.com.android.check.ws.WebServiceCliente;

/**
 * Created by masasp29 on 20/10/15.
 */
public class VisitaDAO {

    //ws
    private String url = ConfiguracoesWS.URL_APLICACAO + "visita/";

    public VisitaDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public Boolean inserirVisita(Visita visita) {
        Boolean flag = false;
        try {
            Gson gson = new Gson();

            String vendedorJson = gson.toJson(visita);
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

    public Boolean finalizaVisita(Visita visita) {
        Boolean flag = false;
        try {
            Gson gson = new Gson();

            String vendedorJson = gson.toJson(visita);
            String[] resposta = new WebServiceCliente().post(url + "finalizaVisita", vendedorJson);

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

    public ArrayList<Visita> listar(Usuario user) {
        ArrayList<Visita> lista = new ArrayList<>();

        try {
            String[] resposta = new WebServiceCliente().get(url + "lista/" + user.getLogin() + "/" + user.getPerfil(), false);

            if (resposta[0].equals("200")) {
                String json = resposta[1];

                if (json.equals("null")) {
                    return null;
                }

                Gson gson = new Gson();

                lista = gson.fromJson(json, new TypeToken<ArrayList<Visita>>() {
                }.getType());
            }
        } catch (Exception e) {
            Log.e("ErroListaVisita", e.getMessage());
        }

        return lista;
    }

}