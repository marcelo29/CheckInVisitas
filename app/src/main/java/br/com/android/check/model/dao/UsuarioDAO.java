package br.com.android.check.model.dao;

import android.os.StrictMode;

import br.com.android.check.model.bean.Usuario;
import br.com.android.check.ws.ConfiguracoesWS;
import br.com.android.check.ws.DownloadJsonObjectWS;

public class UsuarioDAO {

    // ws ok
    private String url = ConfiguracoesWS.URL_APLICACAO + "usuario/";

    public UsuarioDAO() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    public Boolean insereUsuario(Usuario usuario) {
        Boolean flag = false;
        try {
            usuario.setId(1);
            usuario.setPerfil(usuario.PERFIL_ADM);
            String link = url + "inserir";
            Usuario user = (Usuario) new DownloadJsonObjectWS().validaJson(link, usuario, DownloadJsonObjectWS.POST);
            if (user != null) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public Boolean logar(Usuario user) {
        Usuario usuario = null;
        try {
            String link = url + "logar/" + user.getLogin() + "/" + user.getSenha();
            usuario = (Usuario) new DownloadJsonObjectWS().validaJson(link, user, DownloadJsonObjectWS.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (usuario != null);
    }

    public Usuario usuarioLogado(Usuario user) {
        Usuario usuario = null;
        try {
            String link = url + "usuarioLogado/" + user.getLogin();
            usuario = (Usuario) new DownloadJsonObjectWS().validaJson(link, user, DownloadJsonObjectWS.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }
}