package br.com.android.check.api;

import br.com.android.check.model.bean.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by marcelo on 02/02/2017.
 */

public interface UsuarioAPI {
    //http://192.168.25.8:8080/CheckVisitaWS/usuario/logar/admin/admin
    @GET("CheckVisitaWS/usuario/logar/{login}/{senha}")
    Call<Usuario> logar(@Path("login") String login, @Path("senha") String senha);
}