package br.com.android.check.helper.adapter;

import android.graphics.Color;
import android.widget.EditText;

import br.com.android.check.CadVendedor;
import br.com.android.check.R;
import br.com.android.check.modelo.bean.Vendedor;

public class CadVendedorHelper {

    private EditText edtNome, edtTelefone, edtSenha;

    public CadVendedorHelper(CadVendedor activity) {
        edtNome = (EditText) activity.findViewById(R.id.edtNome);
        edtTelefone = (EditText) activity.findViewById(R.id.edtTelefone);
        edtSenha = (EditText) activity.findViewById(R.id.edtSenha);
    }

    public Vendedor getVendedor() {
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(edtNome.getText().toString());
        vendedor.setTelefone(edtTelefone.getText().toString());
        vendedor.setSenha(edtSenha.getText().toString());
        return vendedor;
    }

    public void limpaCampos() {
        edtNome.setText("");
        edtTelefone.setText("");
        edtSenha.setText("");

        edtNome.setBackgroundColor(Color.WHITE);
        edtTelefone.setBackgroundColor(Color.WHITE);
        edtSenha.setBackgroundColor(Color.WHITE);
    }

    // validas os campos
    public boolean validacao() {
        Boolean valido = true;

        String nome = edtNome.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String senha = edtSenha.getText().toString();

        if (nome == null || nome.equals("")) {
            edtNome.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtNome.setBackgroundColor(Color.WHITE);
        }

        if (telefone == null || telefone.equals("")) {
            edtTelefone.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtTelefone.setBackgroundColor(Color.WHITE);
        }

        if (senha == null || senha.equals("")) {
            edtSenha.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtSenha.setBackgroundColor(Color.WHITE);
        }

        return valido;
    }

}