package br.com.android.check;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.android.check.helper.adapter.CadVendedorHelper;
import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Vendedor;
import br.com.android.check.modelo.dao.VendedorDAO;

public class CadVendedor extends AppCompatActivity {

    private Context ctx;
    private FloatingActionButton fabCadastrar, fabCancelar;
    private TextView txtTitulo;
    private ImageView btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_cad_vendedor);
        txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtTitulo.setText(R.string.title_activity_cad_vendedor);

        final CadVendedorHelper componentes = new CadVendedorHelper(this);

        fabCadastrar = (FloatingActionButton) findViewById(R.id.fabCadastrar);
        fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);
        btnVoltar = (ImageView) findViewById(R.id.btnVoltar);

        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentes.validacao()) {
                    VendedorDAO dao = new VendedorDAO();
                    Vendedor vendedor = componentes.getVendedor();
                    if (dao.inserirVendedor(vendedor)) {
                        Util.showAviso(ctx, R.string.vendedor_cadastrado);
                        componentes.limpaCampos();
                    } else {
                        Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                    }
                }
            }
        });

        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                componentes.limpaCampos();
            }
        });
        voltar();
    }

    private void voltar() {
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}