package br.com.android.check.api;

import br.com.android.check.model.bean.Visita;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by marcelo on 06/02/2017.
 */

public interface VisitaAPI {
    @POST("CheckVisitaWS/visita/inserir")
    Call<Visita> inserir(@Body Visita visita);
}