package br.com.android.check;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.android.check.controler.ValidaCamposObrigatorios;
import br.com.android.check.library.Util;
import br.com.android.check.model.bean.Vendedor;
import br.com.android.check.model.dao.VendedorDAO;

public class CadVendedor extends AppCompatActivity {

    private Context ctx;
    private FloatingActionButton fabCadastrar, fabCancelar;
    private Toolbar toolbar;
    private EditText edtNome, edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_cad_vendedor);

        // construindo toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // recebendo componentes do xhtml
        fabCadastrar = (FloatingActionButton) findViewById(R.id.fabCadastrar);
        fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        cadastrar();
    }

    private void cadastrar() {
        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VendedorDAO dao = new VendedorDAO();

                Vendedor vendedor = new Vendedor();
                vendedor.setNome(edtNome.getText().toString());
                vendedor.setSenha(edtSenha.getText().toString());

                if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(vendedor.getNome()))
                    if (dao.inserirVendedor(vendedor)) {
                        Util.showAviso(ctx, R.string.vendedor_cadastrado);
                    } else {
                        Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                    }
                else
                    edtNome.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}