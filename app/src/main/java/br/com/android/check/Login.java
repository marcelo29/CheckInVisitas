package br.com.android.check;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import br.com.android.check.modelo.bean.Usuario;
import br.com.android.check.modelo.dao.DbOpenHelper;
import br.com.android.check.modelo.dao.SessaoDAO;
import br.com.android.check.modelo.dao.UsuarioDAO;

// gerencia o login da aplicacoa
public class Login extends Activity {

    // componentes da tela /
    private Context context;
    private EditText edtUsuario, edtSenha;
    private Button btnLogar, btnCadastrar;
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

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        CadUsuario();
    }

    private void CadUsuario() {
        btnCadastrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CadUsuario.class);
                startActivity(intent);
            }
        });
    }

    private void ChecarLogin() {
        // evento chamando ao clicar do botao
        btnLogar.setOnClickListener(new OnClickListener() {

            @Override
            // acao executado ao clicar do botao
            public void onClick(View v) {

                // recupera usuario e senha digitados
                Usuario usuario = new Usuario(edtUsuario.getText().toString(), edtSenha.getText().toString());

                // chama o dao
                UsuarioDAO dao = new UsuarioDAO(context);

                // valida os campos
                boolean validacao = validacao(usuario.getLogin(), usuario.getSenha());

                // monta um dialogo
                AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
                // add botao ao dialogo
                dialogo.setNeutralButton("Ok", null);
                try {
                    // verifica se bate usuario e senha
                    if (validacao) {
                        if (dao.Logar(usuario.getLogin(), usuario.getSenha())) {
                            // registra perfil logado na sessao
                            new SessaoDAO(Login.this).setUsuario(usuario.getLogin(), dao);

                            // carrega novo layotu
                            Intent intent = new Intent(context, MenuGeral.class);
                            startActivity(intent);
                            limpaCampos();
                        } else {
                            // avisa q usuario e senha estao errados
                            dialogo.setMessage(R.string.msg_erro_invalido_login);
                            dialogo.show(); // exibe dialogo
                        }
                    } else {
                        // avisa q usuario e senha estao errados
                        dialogo.setMessage(R.string.msg_erro_validacao_login);
                        dialogo.show(); // exibe dialogo
                    }
                } catch (SQLiteException e) {
                    // reportar erro
                    e.printStackTrace();
                } finally {
                    // garante a finalizacao da conexao com o banco
                    dao.close();
                }
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