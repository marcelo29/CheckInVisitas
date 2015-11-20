package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.android.check.helper.adapter.ListaVisitaAdapter;
import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.modelo.dao.DbOpenHelper;
import br.com.android.check.modelo.dao.SessaoDAO;
import br.com.android.check.modelo.dao.VisitaDAO;

public class ListaVisita extends AppCompatActivity {

    private ListView lstVisita;
    private Button btnMarcaVisita, btnVerNoMapa;
    private Context ctx;
    private ArrayList<Visita> lista;
    private Usuario user;
    private VisitaDAO vdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_lista_visita);

        lstVisita = (ListView) findViewById(R.id.lstVisita);
        btnMarcaVisita = (Button) findViewById(R.id.btnMarcaVisita);
        btnVerNoMapa = (Button) findViewById(R.id.btnVerNoMapa);

        user = new SessaoDAO(this).getUsuario();

        if (user.getPerfil().equals("vendedor")) {
            btnMarcaVisita.setVisibility(View.INVISIBLE);
        }

        vdao = new VisitaDAO(this);

        atualizaLista();

        realizaVisita();

        verNoMapa();
    }

    private void verNoMapa() {
        btnVerNoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int marcadas = 0, posicaoMarcada = 0;

                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getChkMarcado()) {
                        if (lista.get(i).getSituacao() == DbOpenHelper.VISITA_DISPONIVEL) {
                            marcadas++;
                            posicaoMarcada = i;
                        }
                    }
                }

                if (marcadas == 1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(
                            Uri.parse("geo:0,0?z=14&q=" + lista.get(posicaoMarcada).getEndereco()));
                    startActivity(intent);
                } else if (marcadas > 1) {
                    Util.showMessage(ctx, "Marque apenas um endereço para visualizar no mapa.");
                } else {
                    Util.showMessage(ctx, "Marque um endereço para visualizar no mapa.");
                }

                atualizaLista();

            }
        });
    }

    private void realizaVisita() {
        btnMarcaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdMarcada = 0;
                int posicaoMarcada = 0;

                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getChkMarcado())
                        if (lista.get(i).getSituacao() == DbOpenHelper.VISITA_DISPONIVEL) {
                            qtdMarcada++;
                            posicaoMarcada = i;
                        }
                }

                if (qtdMarcada == 1) {
                    lista.get(posicaoMarcada).setSituacao(DbOpenHelper.VISITA_REALIZADA);
                    vdao.visitaRealizada(lista.get(posicaoMarcada));
                    Util.showMessage(ctx, "Visita concluída");
                } else if (qtdMarcada > 1) {
                    Util.showMessage(ctx, "Marque apenas uma visita para realizar.");
                } else {
                    Util.showMessage(ctx, "Marque uma visita para realizar.");
                }

                atualizaLista();
            }
        });
    }

    private void atualizaLista() {
        lista = vdao.listar(user);
        lstVisita.setAdapter(new ListaVisitaAdapter(this, lista));
    }
}