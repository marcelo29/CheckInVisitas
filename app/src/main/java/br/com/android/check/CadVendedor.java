package br.com.android.check;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.android.check.helper.adapter.CadVendedorHelper;
import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Vendedor;
import br.com.android.check.modelo.dao.VendedorDAO;

public class CadVendedor extends Activity {

    private Context ctx;
    private Button btnCadastrar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(R.layout.activity_cad_vendedor);

        final CadVendedorHelper componentes = new CadVendedorHelper(this);

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentes.validacao()) {
                    VendedorDAO dao = new VendedorDAO(ctx);
                    Vendedor vendedor = componentes.getVendedor();
                    dao.inserirVendedor(vendedor.getNome(), vendedor.getTelefone());
                    new Util().showMessage(ctx, "Cadastro de vendedor realizado com sucesso.");
                    componentes.limpaCampos();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                componentes.limpaCampos();
            }
        });
    }

}