package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.android.check.adapter.ListaVisitaAdapter;
import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.modelo.dao.SessaoDAO;
import br.com.android.check.modelo.dao.VisitaDAO;

public class ListaVisita extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lstVisita;
    private FloatingActionButton fabMarcaVisita;
    private Context ctx;
    private ArrayList<Visita> lista;
    private Usuario user;
    private VisitaDAO vdao;
    private Toolbar toolbar;
    //private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_lista_visita);

        //img = (ImageView) findViewById(R.id.btnVoltar);
        //img.setVisibility(View.INVISIBLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(R.string.title_activity_lst);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lstVisita = (ListView) findViewById(R.id.lstVisita);
        fabMarcaVisita = (FloatingActionButton) findViewById(R.id.fabMarcaVisita);

        user = new SessaoDAO(this).getUsuario();

        if (user.getPerfil().equals(user.PERFIL_VENDEDOR)) {
            fabMarcaVisita.setVisibility(View.INVISIBLE);
        }

        vdao = new VisitaDAO();

        atualizaLista();

        finalizaVisita();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackgroundResource(R.drawable.toolbar_rounded_corners);
        }
    }

    private void verNoMapa() {
        int marcadas = 0, posicaoMarcada = 0;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getChkMarcado()) {
                if (lista.get(i).getSituacao() == Visita.EM_ANDAMENTO) {
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
            Util.showAviso(ctx, R.string.aviso_apenas_um_endereco);
        } else {
            Util.showAviso(ctx, R.string.aviso_marque_um_endereco);
        }

        atualizaLista();
    }

    private void finalizaVisita() {
        fabMarcaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdMarcada = 0;
                int posicaoMarcada = 0;

                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getChkMarcado())
                        if (lista.get(i).getSituacao() == Visita.EM_ANDAMENTO) {
                            qtdMarcada++;
                            posicaoMarcada = i;
                        }
                }

                if (qtdMarcada == 1) {
                    lista.get(posicaoMarcada).setSituacao(Visita.FINALIZADA);
                    if (vdao.finalizaVisita(lista.get(posicaoMarcada))) {
                        Util.showAviso(ctx, R.string.aviso_visita_concluida);
                    } else {
                        Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                    }
                } else if (qtdMarcada > 1) {
                    Util.showAviso(ctx, R.string.aviso_apenas_uma_visita);
                } else {
                    Util.showAviso(ctx, R.string.aviso_marque_uma_visita);
                }

                atualizaLista();
            }
        });
    }

    private void atualizaLista() {
        lista = vdao.listar(user);
        lstVisita.setAdapter(new ListaVisitaAdapter(this, lista));
    }

    private void carregaLayout(Context ctx, Class classeDestino) {
        Intent intent = new Intent(ctx, classeDestino);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Usuario usuarioLogado = new SessaoDAO(ctx).getUsuario();
        if (usuarioLogado.getPerfil().equals(usuarioLogado.PERFIL_ADM)) {
            switch (menuItem.getItemId()) {
                case R.id.itCadVendedor:
                    carregaLayout(ctx, CadVendedor.class);
                    break;
                case R.id.itCadVisita:
                    carregaLayout(ctx, CadVisita.class);
                    break;
                case R.id.itCadAdmin:
                    carregaLayout(ctx, CadUsuario.class);
                    break;
            }
        }
        if (menuItem.getItemId() == R.id.itVerNoMapa) {
            verNoMapa();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lst, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            carregaLayout(ctx, ListaVisitaRecyclerView.class);
        }
        return true;
    }

}