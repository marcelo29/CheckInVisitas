package br.com.android.check.domain;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by masasp29 on 08/02/17.
 */
public class Sessao {

    private final String PREFERENCE_NAME = "LOGIN", PERFIL_USUARIO = "PERFIL_USUARIO", LOGIN_USUARIO = "LOGIN_USUARIO",
            ID_USUARIO = "ID_USUARIO", TELEFONE_USUARIO = "TELEFONE_USUARIO";
    private SharedPreferences sharedPreferences;

    public Sessao(Context activity) {
        sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public Usuario getUsuario() {

        Usuario usuario = new Usuario();

        usuario.setId(sharedPreferences.getInt(ID_USUARIO, 0));
        usuario.setLogin(sharedPreferences.getString(LOGIN_USUARIO, ""));
        usuario.setPerfil(sharedPreferences.getString(PERFIL_USUARIO, "vendedor"));

        return usuario;
    }

    public void setUsuario(Usuario user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(ID_USUARIO, user.getId());
        editor.putString(PERFIL_USUARIO, user.getPerfil());
        editor.putString(LOGIN_USUARIO, user.getLogin());

        editor.commit();
    }
}