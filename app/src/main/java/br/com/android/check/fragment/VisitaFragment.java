package br.com.android.check.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.android.check.R;
import br.com.android.check.adapter.VisitaAdapter;
import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.modelo.dao.SessaoDAO;
import br.com.android.check.modelo.dao.VisitaDAO;


/**
 * Created by masasp29 on 04/12/15.
 */
public class VisitaFragment extends Fragment {

    private RecyclerView recyclerViewVisita;
    private List<Visita> lista;
    private FloatingActionButton fabMarcaVisita;
    private VisitaDAO vdao;
    private Usuario user;
    private Context ctx;
    private VisitaAdapter adapter;
    public static int posicao = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visita, container, false);
        this.ctx = view.getContext();

        fabMarcaVisita = (FloatingActionButton) view.findViewById(R.id.fabMarcaVisita);
        recyclerViewVisita = (RecyclerView) view.findViewById(R.id.rv_lst);

        vdao = new VisitaDAO();
        user = new SessaoDAO(ctx).getUsuario();

        if (user.getPerfil().equals(Usuario.PERFIL_VENDEDOR)) {
            fabMarcaVisita.setVisibility(View.INVISIBLE);
        }

        finalizaVisita();

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

        atualizaLista();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizaLista();
        posicao = -1;
    }

    private void finalizaVisita() {
        fabMarcaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdMarcada = 0;
                //int posicaoMarcada = 0;

                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getChkMarcado()) {
                        if (lista.get(i).getSituacao() == Visita.EM_ANDAMENTO) {
                            qtdMarcada++;
                            //posicaoMarcada = i;
                            posicao = i;
                        }
                    }
                }

                if (qtdMarcada == 1) {
                    //lista.get(posicaoMarcada).setSituacao(Visita.FINALIZADA);
                    lista.get(posicao).setSituacao(Visita.FINALIZADA);
                    if (vdao.finalizaVisita(lista.get(posicao))) {
                        atualizaLista();
                    } else {
                        Util.showAviso(ctx, R.string.aviso_erro_conexaows);
                    }
                } else if (qtdMarcada > 1) {
                    Util.showAviso(ctx, R.string.aviso_apenas_uma_visita);
                } else {
                    Util.showAviso(ctx, R.string.aviso_marque_uma_visita);
                }
            }
        });
    }

    private void atualizaLista() {
        lista = vdao.listar(user);
        adapter = null;
        adapter = new VisitaAdapter(ctx, lista);
        recyclerViewVisita.setAdapter(adapter);
    }

}