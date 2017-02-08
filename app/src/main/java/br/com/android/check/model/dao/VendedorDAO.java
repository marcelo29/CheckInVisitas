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
    private Vendedor vendedorSelecionado = null;

    public VendedorDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    /*public Vendedor retornaVendedorPorNome(String nome) {
        Vendedor vendedor = null;
        try {
            String[] resposta = new WebServiceCliente().get(url + "retornaVendedorPorNome/" + nome, false);
            vendedor = new Gson().fromJson(resposta[1], Vendedor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendedor;
    }

    // RETORNA VENDEDOR - REQUEST
    public Vendedor retornaVendedorPorNome(String nome) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Vendedor.class,
                new VendedorDeserializer()).create();

        Retrofit retroit = new Retrofit
                .Builder()
                .baseUrl(ConfiguracoesWS.API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        VendedorAPI vendedorAPI = retroit.create(VendedorAPI.class);

        Call<Vendedor> call = vendedorAPI.retornaVendedorPorNome(nome);
        call.enqueue(new Callback<Vendedor>() {
            @Override
            public void onResponse(Call<Vendedor> call, Response<Vendedor> response) {
                Vendedor v = response.body();
                vendedorSelecionado = v;
            }

            @Override
            public void onFailure(Call<Vendedor> call, Throwable t) {
                Log.i(ConfiguracoesWS.TAG, "ErroRetornaVendedorPorNome " + t.getMessage());
            }
        });

        return vendedorSelecionado;
    }*/

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