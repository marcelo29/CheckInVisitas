package br.com.android.check.modelo.dao;

import android.os.StrictMode;

import com.google.gson.Gson;

import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.ws.ConfiguracoesWS;
import br.com.android.check.ws.WebServiceCliente;

public class UsuarioDAO {

    //ws ok
    private String url = ConfiguracoesWS.URL_APLICACAO + "usuario/";
    public static final String PERFIL_ADM = "adm", PERFIL_VENDEDOR = "vendedor";

    public UsuarioDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public Boolean insereUsuario(Usuario usuario) {
        Boolean flag = false;
        try {
            usuario.setPerfil(PERFIL_ADM);
            String usuarioJson = new Gson().toJson(usuario);
            String[] resposta = new WebServiceCliente().post(url + "inserir", usuarioJson);

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

    public Boolean Logar(String login, String senha) {
        Usuario usuario = null;
        try {
            String[] resposta = new WebServiceCliente().get(url + "logar/" + login + "/" + senha, false);

            if (resposta[0].equals("200")) {
                usuario = new Gson().fromJson(resposta[1], Usuario.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (usuario != null);
    }

    public Usuario usuarioLogado(String usuarioNome) {
        Usuario usuario = null;
        try {
            String[] resposta = new WebServiceCliente().get(url + "usuarioLogado/" + usuarioNome, false);

            if (resposta[0].equals("200")) {
                usuario = new Gson().fromJson(resposta[1], Usuario.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }
}