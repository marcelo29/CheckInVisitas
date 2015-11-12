package br.com.android.check;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.android.check.helper.adapter.ListaVisitaAdapter;
import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.modelo.dao.SessaoDAO;
import br.com.android.check.modelo.dao.VisitaDAO;

/**
 * Created by masasp29 on 25/10/15.
 */
public class ListaVisita extends Activity {

    private ListView lstVisita;
    private Button btnMarcaVisita;
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

        user = new SessaoDAO(this).getUsuario();

        vdao = new VisitaDAO(this);

        atualizaLista();

        btnMarcaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdMarcada = 0, posicaoMarcada = 0;
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getSituacao() == 1) {
                        qtdMarcada++;
                        posicaoMarcada = i;
                    }
                }
                if (qtdMarcada == 1) {
                    vdao.visitaRealizada(lista.get(posicaoMarcada).getId());
                    new Util().showMessage(ctx, "Visita concluída");
                } else if (qtdMarcada > 1) {
                    new Util().showMessage(ctx, "Marque apenas um endereço para visualizar no mapa.");
                } else {
                    new Util().showMessage(ctx, "Marque um endereço para visualizar no mapa.");
                }
                atualizaLista();
            }
        });
    }

    private void atualizaLista() {
        lista = vdao.listar(user);
        lstVisita.setAdapter(new ListaVisitaAdapter(this, lista));
    }

    // carrega menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();

        inflater.inflate(R.menu.menu_lst, menu);

        return true;
    }

    // ao selecionar item do menu faca
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mapa:
                int qtdMarcada = 0, posicaoMarcada = 0;
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getSituacao() == 1) {
                        qtdMarcada++;
                        posicaoMarcada = i;
                    }
                }
                if (qtdMarcada == 1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(
                            Uri.parse("geo:0,0?z=14&q=" + lista.get(posicaoMarcada).getEndereco()));
                    startActivity(intent);
                } else if (qtdMarcada > 1) {
                    new Util().showMessage(ctx, "Marque apenas um endereço para visualizar no mapa.");
                } else {
                    new Util().showMessage(ctx, "Marque um endereço para visualizar no mapa.");
                }
                atualizaLista();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}