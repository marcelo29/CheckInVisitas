package br.com.android.check.api;

import java.util.List;

import br.com.android.check.domain.Vendedor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by marcelo on 06/02/2017.
 */

public interface VendedorAPI {
    @POST("CheckVisitaWS/vendedor/inserir")
    Call<Vendedor> inserir(@Body Vendedor vendedor);

    @GET("CheckVisitaWS/vendedor/retornaVendedorPorNome/{nome}")
    Call<Vendedor> retornaVendedorPorNome(@Path("nome") String nome);

    @GET("CheckVisitaWS/vendedor/lista")
    Call<List<Vendedor>> listar();
}