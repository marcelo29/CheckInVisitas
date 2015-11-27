package br.com.android.check.helper.adapter;

import android.widget.EditText;

import br.com.android.check.CadVendedor;
import br.com.android.check.R;
import br.com.android.check.library.Util;
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
    }

    // validas os campos
    public boolean validacao() {
        Boolean valido = true;

        String nome = edtNome.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String senha = edtSenha.getText().toString();

        if (nome == null || nome.equals("")) {
            edtNome.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (telefone == null || telefone.equals("")) {
            edtTelefone.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (senha == null || senha.equals("")) {
            edtSenha.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        return valido;
    }

}