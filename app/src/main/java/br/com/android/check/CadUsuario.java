package br.com.android.check;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.dao.UsuarioDAO;

public class CadUsuario extends AppCompatActivity {

    private Context ctx;
    private FloatingActionButton fabCadastrar, fabCancelar;
    private EditText edtUsuario, edtSenha;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_cad_usuario);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        fabCadastrar = (FloatingActionButton) findViewById(R.id.fabCadastrar);
        fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);

        cadastrar();
        cancelar();
    }

    private void cadastrar() {
        fabCadastrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario(edtUsuario.getText().toString(), edtSenha.getText().toString());
                boolean validacao = validacao(usuario.getLogin(), usuario.getSenha());
                if (validacao) {
                    UsuarioDAO dao = new UsuarioDAO();
                    if (dao.insereUsuario(usuario)) {
                        Util.showAviso(ctx, R.string.usuario_cadastrado);
                        limpaCampos();
                    } else {
                        Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                    }
                }
            }
        });
    }

    private void cancelar() {
        fabCancelar.setOnClickListener(new OnClickListener() {
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
    }

    // validas os campos
    private boolean validacao(String usuario, String senha) {
        boolean validacao = true;

        if (usuario == null || usuario.equals("")) {
            validacao = false;
            edtUsuario.setError(Util.AVISO_CAMPO_OBRIGATORIO);
        }

        if (senha == null || senha.equals("")) {
            validacao = false;
            edtSenha.setError(Util.AVISO_CAMPO_OBRIGATORIO);
        }

        return validacao;
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