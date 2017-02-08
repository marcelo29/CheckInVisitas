package br.com.android.check.api;

import br.com.android.check.domain.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by marcelo on 02/02/2017.
 */

public interface UsuarioAPI {
    @GET("CheckVisitaWS/usuario/logar/{login}/{senha}")
    Call<Usuario> logar(@Path("login") String login, @Path("senha") String senha);

    @POST("CheckVisitaWS/usuario/inserir")
    Call<Usuario> inserir(@Body Usuario usuario);
}