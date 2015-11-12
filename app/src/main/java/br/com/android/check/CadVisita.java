package br.com.android.check;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Vendedor;
import br.com.android.check.modelo.dao.VendedorDAO;
import br.com.android.check.modelo.dao.VisitaDAO;

public class CadVisita extends Activity {

    private EditText edtCliente, edtEnderedo, edtTelefone, edtData, edtHora;
    private Button btnCadastrar, btnCancelar;
    private Spinner spnVendedores;
    private Context ctx;
    private String cliente, endereco, telefone, data, hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(R.layout.activity_cad_visita);

        edtCliente = (EditText) findViewById(R.id.edtCliente);
        edtEnderedo = (EditText) findViewById(R.id.edtEndereco);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtData = (EditText) findViewById(R.id.edtData);
        edtHora = (EditText) findViewById(R.id.edtHora);

        // A construcao do objeto spinner e separada em varias etapas sendo um exempo de padrao Builder
        ArrayList<String> strVendedores = populaVendedor();
        ArrayAdapter<String> adpVendedores = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                strVendedores);
        spnVendedores = (Spinner) findViewById(R.id.spnVendedor);
        spnVendedores.setAdapter(adpVendedores);

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validacao()) {
                    VisitaDAO visita = new VisitaDAO(ctx);
                    int idVendedor = new VendedorDAO(ctx).retornaId(spnVendedores.getSelectedItem().toString());
                    visita.inserirVisita(cliente, endereco, telefone, data, hora, idVendedor);
                    new Util().showMessage(ctx, "Visita cadastra com sucesso");
                    cancelar();
                }
            }
        });

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });
    }

    private void cancelar() {
        edtCliente.setBackgroundColor(Color.WHITE);
        edtCliente.setText("");
        edtEnderedo.setBackgroundColor(Color.WHITE);
        edtEnderedo.setText("");
        edtTelefone.setBackgroundColor(Color.WHITE);
        edtTelefone.setText("");
        edtData.setBackgroundColor(Color.WHITE);
        edtData.setText("");
        edtHora.setBackgroundColor(Color.WHITE);
        edtHora.setText("");
    }

    // validas os campos
    public boolean validacao() {
        Boolean valido = true;

        cliente = edtCliente.getText().toString();
        endereco = edtEnderedo.getText().toString();
        telefone = edtTelefone.getText().toString();
        data = edtData.getText().toString();
        hora = edtHora.getText().toString();

        if (cliente == null || cliente.equals("")) {
            edtCliente.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtCliente.setBackgroundColor(Color.WHITE);
        }

        if (endereco == null || endereco.equals("")) {
            edtEnderedo.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtEnderedo.setBackgroundColor(Color.WHITE);
        }

        if (telefone == null || telefone.equals("")) {
            edtTelefone.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtTelefone.setBackgroundColor(Color.WHITE);
        }

        if (data == null || data.equals("")) {
            edtData.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtData.setBackgroundColor(Color.WHITE);
        }

        if (hora == null || hora.equals("")) {
            edtHora.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtHora.setBackgroundColor(Color.WHITE);
        }

        return valido;
    }

    private ArrayList<String> populaVendedor() {
        VendedorDAO vdao = new VendedorDAO(CadVisita.this);
        ArrayList<String> lista = new ArrayList<String>();
        ArrayList<Vendedor> vendedores = vdao.listaVendedores();
        for (int i = 0; i < vendedores.size(); i++) {
            lista.add(vendedores.get(i).getNome());
        }
        return lista;
    }

}