package br.com.android.check;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.dao.UsuarioDAO;

public class CadUsuario extends Activity {

    private Context context;
    private Button btnCadastrar, btnCancelar;
    private EditText edtUsuario, edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_cad_usuario);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        Cadastrar();

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        Cancelar();
    }

    private void Cadastrar() {
        btnCadastrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario(edtUsuario.getText().toString(), edtSenha.getText().toString());
                boolean validacao = validacao(usuario.getLogin(), usuario.getSenha(), usuario.getPerfil());
                if (validacao) {
                    UsuarioDAO dao = new UsuarioDAO(context);
                    dao.insereUsuario(usuario.getLogin(), usuario.getSenha(), usuario.getPerfil());
                    new Util().showMessage(context, "Cadastro de usuário realizado com sucesso.");
                    limpaCampos();
                }
            }
        });
    }

    private void Cancelar() {
        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    // limpa os campos ao logar
    private void limpaCampos() {
        edtUsuario.setText("");
        edtSenha.setText("");

        edtUsuario.setBackgroundColor(Color.WHITE);
        edtSenha.setBackgroundColor(Color.WHITE);
    }

    // validas os campos
    private boolean validacao(String usuario, String senha, String perfil) {
        boolean validacao = true;

        if (usuario == null || usuario.equals("")) {
            validacao = false;
            edtUsuario.setBackgroundColor(Color.RED);
        } else {
            edtUsuario.setBackgroundColor(Color.WHITE);
        }

        if (senha == null || senha.equals("")) {
            validacao = false;
            edtSenha.setBackgroundColor(Color.RED);
        } else {
            edtSenha.setBackgroundColor(Color.WHITE);
        }

        return validacao;
    }

}