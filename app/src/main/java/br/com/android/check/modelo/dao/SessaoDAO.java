package br.com.android.check.modelo.dao;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.android.check.modelo.bean.Usuario;

public class SessaoDAO {

    private final String PREFERENCE_NAME = "LOGIN", PERFIL_USUARIO = "PERFIL_USUARIO", LOGIN_USUARIO = "LOGIN_USUARIO",
            ID_USUARIO = "ID_USUARIO", TELEFONE_USUARIO = "TELEFONE_USUARIO";
    private SharedPreferences sharedPreferences;

    public SessaoDAO(Context activity) {
        sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public Usuario getUsuario() {

        Usuario usuario = new Usuario();

        usuario.setId(sharedPreferences.getInt(ID_USUARIO, 0));
        usuario.setLogin(sharedPreferences.getString(LOGIN_USUARIO, ""));
        usuario.setPerfil(sharedPreferences.getString(PERFIL_USUARIO, "vendedor"));

        return usuario;
    }

    public void setUsuario(String usuarioNome, UsuarioDAO dao) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Usuario user = dao.usuarioLogado(usuarioNome);

        editor.putInt(ID_USUARIO, user.getId());
        editor.putString(PERFIL_USUARIO, user.getPerfil());
        editor.putString(LOGIN_USUARIO, user.getLogin());

        editor.commit();
    }

}