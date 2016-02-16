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
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import br.com.android.check.adapter.VisitaAdapter;
import br.com.android.check.fragment.VisitaFragment;
import br.com.android.check.library.Util;
import br.com.android.check.model.bean.Usuario;
import br.com.android.check.model.bean.Visita;
import br.com.android.check.model.dao.SessaoDAO;

public class ListaVisitaRecyclerView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context ctx;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_visita_recycler_view);
        ctx = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_recycler);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        carregaVisitaFragment();
    }

    private void carregaVisitaFragment() {
        try {
            VisitaFragment frag = (VisitaFragment) getSupportFragmentManager().findFragmentByTag("visitaFrag");

            if (frag == null) {
                frag = new VisitaFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.rl_fragment_container, frag, "visitaFrag");
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
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

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        // setTitle(menuItem.getTitle());
        drawer.closeDrawers();

        return true;
    }

    private void carregaLayout(Context ctx, Class classeDestino) {
        Intent intent = new Intent(ctx, classeDestino);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_recycler);
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

        List<Visita> lista = VisitaAdapter.lista;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lst, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}