package br.com.android.check.api;

import java.util.List;

import br.com.android.check.domain.Visita;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by marcelo on 06/02/2017.
 */

public interface VisitaAPI {
    @POST("CheckVisitaWS/visita/inserir")
    Call<Visita> inserir(@Body Visita visita);

    @POST("CheckVisitaWS/visita/finalizaVisita")
    Call<Visita> finalizar(@Body Visita visita);

    @GET("CheckVisitaWS/visita/lista/{login}/{perfil}")
    Call<List<Visita>> listar(@Path("login") String login, @Path("perfil") String perfil);
}