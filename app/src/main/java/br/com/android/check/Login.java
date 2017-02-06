package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.android.check.api.UsuarioAPI;
import br.com.android.check.controler.ValidaCamposObrigatorios;
import br.com.android.check.library.Util;
import br.com.android.check.model.bean.Usuario;
import br.com.android.check.model.dao.SessaoDAO;
import br.com.android.check.model.dao.UsuarioDAO;
import br.com.android.check.util.UsuarioDeserializer;
import br.com.android.check.ws.ConfiguracoesWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// gerencia o login da aplicacoa
public class Login extends AppCompatActivity {

    // componentes da tela /
    private Context ctx;
    private EditText edtUsuario, edtSenha;
    private FloatingActionButton fabLogar, fabCancelar;

    private String API = "http://192.168.25.8:8080/";
    private String TAG = "TAGE";
    private Usuario usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(R.layout.activity_login);

        // relaciona xml com codigo java
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        fabLogar = (FloatingActionButton) findViewById(R.id.fabLogar);

        fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);
        cancelar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fabLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SHOW USER - REQUEST
                if (camposValidos(edtUsuario.getText().toString(), edtSenha.getText().toString())) {
                    Gson gsonUser = new GsonBuilder().registerTypeAdapter(Usuario.class, new UsuarioDeserializer()).create();

                    Retrofit retroitUser = new Retrofit
                            .Builder()
                            .baseUrl(API)
                            .addConverterFactory(GsonConverterFactory.create(gsonUser))
                            .build();
                    UsuarioAPI usuarioAPI = retroitUser.create(UsuarioAPI.class);

                    final Call<Usuario> callUser = usuarioAPI.logar(edtUsuario.getText().toString(),
                            edtSenha.getText().toString());

                    callUser.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            usuario = response.body();
                            if (usuario != null) {
                                new SessaoDAO(ctx).setUsuario(usuario);

                                carregaLayout(ctx, ListaVisitaRecyclerView.class);
                                limpaCampos();
                            } else {
                                Util.showAviso(ctx, R.string.aviso_login_invalido);
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.i(TAG, "ErroShowMsg " + t.getMessage());
                        }
                    });
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
    private boolean camposValidos(String usuario, String senha) {
        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(usuario)) {
            edtUsuario.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            return false;
        }

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(senha)) {
            edtSenha.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            return false;
        }

        return true;
    }

}