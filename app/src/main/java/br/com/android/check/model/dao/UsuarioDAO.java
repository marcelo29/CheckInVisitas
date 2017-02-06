package br.com.android.check.model.dao;

import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.android.check.api.UsuarioAPI;
import br.com.android.check.model.bean.Usuario;
import br.com.android.check.util.UsuarioDeserializer;
import br.com.android.check.ws.ConfiguracoesWS;
import br.com.android.check.ws.DownloadJsonObjectWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioDAO {

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
            String link = "http://192.168.25.8:8080/CheckVisitaWS/usuario/inserir";
            Usuario user = (Usuario) new DownloadJsonObjectWS().validaJson(link, usuario, DownloadJsonObjectWS.POST);
            if (user != null) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}