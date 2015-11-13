package br.com.android.check;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.dao.DbOpenHelper;
import br.com.android.check.modelo.dao.SessaoDAO;
import br.com.android.check.modelo.dao.UsuarioDAO;

// gerencia o login da aplicacoa
public class Login extends Activity {

    // componentes da tela /
    private Context context;
    private EditText edtUsuario, edtSenha;
    private Button btnLogar, btnCancelar;
    private DbOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_login);

        db = new DbOpenHelper(this);
        db.onCreate(db.getWritableDatabase());

        // relaciona xml com codigo java
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        btnLogar = (Button) findViewById(R.id.btnLogar);
        ChecarLogin();

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        cancelar();
    }

    private void cancelar() {
        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    private void ChecarLogin() {
        btnLogar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario(edtUsuario.getText().toString(), edtSenha.getText().toString());

                // chama o dao
                UsuarioDAO dao = new UsuarioDAO(context);

                // valida os campos
                boolean validacao = validacao(usuario.getLogin(), usuario.getSenha());
                try {
                    if (validacao) {
                        if (dao.Logar(usuario.getLogin(), usuario.getSenha())) {
                            new SessaoDAO(context).setUsuario(usuario.getLogin(), dao);

                            Usuario usuarioLogado = new SessaoDAO(context).getUsuario();

                            if (usuarioLogado.getPerfil().equals("adm")) {
                                carregaLayout(context, MenuGeral.class);
                                limpaCampos();
                            } else if (usuarioLogado.getPerfil().equals("vendedor")) {
                                carregaLayout(context, ListaVisita.class);
                                limpaCampos();
                            }
                        } else {
                            Util.showMessage(context, "Usuário ou senha inválidos.");
                        }
                    } else {
                        Util.showMessage(context, "Usuário ou senha em brancos.");
                    }
                } catch (SQLiteException e) {
                    e.printStackTrace();
                } finally {
                    dao.close();
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

    // validas os campos
    private boolean validacao(String usuario, String senha) {
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