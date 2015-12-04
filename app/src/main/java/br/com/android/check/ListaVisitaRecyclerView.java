package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import br.com.android.check.fragment.VisitaFragment;
import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.modelo.dao.SessaoDAO;
import br.com.android.check.modelo.dao.VisitaDAO;

public class ListaVisitaRecyclerView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context ctx;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_visita_recycler_view);
        ctx = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        VisitaFragment frag = (VisitaFragment) getSupportFragmentManager().findFragmentByTag("visitaFrag");
        if (frag == null) {
            frag = new VisitaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "visitaFrag");
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
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

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackgroundResource(R.drawable.toolbar_rounded_corners);
        }
    }

    private void verNoMapa() {
        int marcadas = 0, posicaoMarcada = 0;

        List<Visita> lista = new VisitaDAO().listar(new SessaoDAO(ctx).getUsuario());

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

        //atualizaLista();
    }
}