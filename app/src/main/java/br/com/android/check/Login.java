package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import br.com.android.check.library.Util;
import br.com.android.check.model.bean.Usuario;
import br.com.android.check.model.dao.SessaoDAO;
import br.com.android.check.model.dao.UsuarioDAO;
import br.com.android.check.ws.ConfiguracoesWS;

// gerencia o login da aplicacoa
public class Login extends AppCompatActivity {

    // componentes da tela /
    private Context ctx;
    private EditText edtUsuario, edtSenha;
    private FloatingActionButton fabLogar, fabCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(R.layout.activity_login);

        // relaciona xml com codigo java
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        fabLogar = (FloatingActionButton) findViewById(R.id.fabLogar);
        ChecarLogin();

        fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);
        cancelar();
    }

    private void cancelar() {
        fabCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    private void ChecarLogin() {
        fabLogar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario(edtUsuario.getText().toString(), edtSenha.getText().toString());

                // chama o dao
                UsuarioDAO dao = new UsuarioDAO();

                // testando ping
                if (Util.ipOff(ConfiguracoesWS.IP)) {
                    Util.showAviso(ctx, R.string.ip_fora_da_rede);
                } else {
                    Util.showAviso(ctx, R.string.ip_na_rede);
                }

                // valida os campos
                boolean validacao = validacao(usuario.getLogin(), usuario.getSenha());
                try {
                    if (validacao) {
                        if (dao.logar(usuario)) {
                            new SessaoDAO(ctx).setUsuario(usuario, dao);

                            carregaLayout(ctx, ListaVisitaRecyclerView.class);
                            limpaCampos();
                        } else {
                            Util.showAviso(ctx, R.string.aviso_login_invalido);
                        }
                    }
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void carregaLayout(Context context, Class layoutDestino) {
        Intent intent = new Intent(context, layoutDestino);
        startActivity(intent);
        limpaCampos();
    }

    // limpa os campos ao logar
    private void limpaCampos() {
        edtUsuario.setText("");
        edtSenha.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

}