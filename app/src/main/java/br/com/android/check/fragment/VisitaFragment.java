package br.com.android.check.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.android.check.R;
import br.com.android.check.adapter.VisitaAdapter;
import br.com.android.check.api.VisitaAPI;
import br.com.android.check.domain.Sessao;
import br.com.android.check.domain.Usuario;
import br.com.android.check.domain.Visita;
import br.com.android.check.library.Util;
import br.com.android.check.library.VisitaDeserializer;
import br.com.android.check.ws.ConfiguracoesWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.gms.internal.zzip.runOnUiThread;


/**
 * Created by masasp29 on 04/12/15.
 */
public class VisitaFragment extends Fragment {

    private Visita visita;
    private Usuario user;
    private List<Visita> lista;
    public static int posicao = -1;

    private Context ctx;
    private RecyclerView recyclerViewVisita;
    private FloatingActionButton fabMarcaVisita, fabAtualizaLista;
    private VisitaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visita, container, false);
        ctx = view.getContext();
        lista = null;

        fabMarcaVisita = (FloatingActionButton) view.findViewById(R.id.fabMarcaVisita);
        fabAtualizaLista = (FloatingActionButton) view.findViewById(R.id.fabAtualizaLista);
        recyclerViewVisita = (RecyclerView) view.findViewById(R.id.rv_lst);

        user = new Sessao(ctx).getUsuario();

        if (user.getPerfil().equals(Usuario.PERFIL_VENDEDOR)) {
            fabMarcaVisita.setVisibility(View.INVISIBLE);
        }

        recyclerViewVisita.setHasFixedSize(true);

        recyclerViewVisita.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(ctx);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewVisita.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        posicao = -1;

        fabAtualizaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaLista();
            }
        });

        fabMarcaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizaVisita();
            }
        });
    }

    private void finalizaVisita() {
        if (lista.size() != 0) {
            lista = VisitaAdapter.lista;

            int qtdMarcada = 0;

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getChkMarcado()) {
                    if (lista.get(i).getSituacao() == Visita.EM_ANDAMENTO) {
                        qtdMarcada++;
                        posicao = i;
                    }
                }
            }

            if (qtdMarcada == 1) {
                // FINALIZA VISITA - REQUEST
                Gson gson = new GsonBuilder().registerTypeAdapter(Visita.class, new VisitaDeserializer()).create();

                Retrofit retroit = new Retrofit
                        .Builder()
                        .baseUrl(ConfiguracoesWS.API)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                VisitaAPI visitaAPI = retroit.create(VisitaAPI.class);

                lista.get(posicao).setSituacao(Visita.FINALIZADA);
                Call<Visita> callVisita = visitaAPI.finalizar(lista.get(posicao));

                callVisita.enqueue(new Callback<Visita>() {
                    @Override
                    public void onResponse(Call<Visita> call, Response<Visita> response) {
                        visita = response.body();
                    }

                    @Override
                    public void onFailure(Call<Visita> call, Throwable t) {
                        Log.i("onFailure", "ErroFinalizaVisita " + t.getMessage());
                        Util.showAviso(ctx, R.string.aviso_erro_finaliza_visita);
                    }
                });

                atualizaLista();
            } else if (qtdMarcada > 1) {
                Util.showAviso(ctx, R.string.aviso_apenas_uma_visita);
            } else {
                Util.showAviso(ctx, R.string.aviso_marque_uma_visita);
            }
        } else {
            Util.showAviso(ctx, R.string.aviso_clique_duplo_botao_esquerdo);
        }
    }

    public void atualizaLista() {
        if (lista == null) {
            lista = new ArrayList<>();
        }

        // RETORNA VISITAS - REQUEST
        Gson gson = new GsonBuilder().registerTypeAdapter(Visita.class,
                new VisitaDeserializer()).create();

        Retrofit retroit = new Retrofit
                .Builder()
                .baseUrl(ConfiguracoesWS.API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        VisitaAPI visitaAPI = retroit.create(VisitaAPI.class);

        final Call<List<Visita>> call = visitaAPI.listar(user.getLogin(), user.getPerfil());

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    List<Visita> visitas = call.execute().body();

                    if (visitas != null && lista.size() != visitas.size()) {
                        for (Visita v : visitas) {
                            lista.add(v);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("IOException", "Erro Retorna Visitas " + e.getMessage());
                    Util.showAviso(ctx, R.string.aviso_erro_lista_visita);
                } finally {
                    onStop();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("onSucess", "RETORNA VISITAS - REQUEST ok");
                    }
                });
            }
        }.start();

        adapter = null;
        adapter = new VisitaAdapter(ctx, lista);
        recyclerViewVisita.setAdapter(adapter);
    }

}