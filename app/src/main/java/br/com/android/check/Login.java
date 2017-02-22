package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.android.check.api.UsuarioAPI;
import br.com.android.check.controler.ValidaCamposObrigatorios;
import br.com.android.check.databinding.ActivityLoginBinding;
import br.com.android.check.domain.Sessao;
import br.com.android.check.domain.Usuario;
import br.com.android.check.library.UsuarioDeserializer;
import br.com.android.check.library.Util;
import br.com.android.check.ws.ConfiguracoesWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// gerencia o login da aplicacao
public class Login extends AppCompatActivity {
    private Usuario usuario = null;

    // componentes da activity
    private Context ctx;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        // relaciona xml com codigo java
        usuario = new Usuario("admin", "admin");
        binding.setUsuario(usuario);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logar();
        cancelar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logar() {
        binding.fabLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario(binding.edtUsuario.getText().toString(), binding.edtSenha.getText().toString());
                binding.setUsuario(usuario);
                if (camposValidos(binding.edtUsuario.getText().toString(), binding.edtSenha.getText().toString())) {
                    // LOGAR USUARIO - REQUEST
                    Gson gsonUser = new GsonBuilder().registerTypeAdapter(Usuario.class,
                            new UsuarioDeserializer()).create();

                    Retrofit retroitUser = new Retrofit
                            .Builder()
                            .baseUrl(ConfiguracoesWS.API)
                            .addConverterFactory(GsonConverterFactory.create(gsonUser))
                            .build();
                    UsuarioAPI usuarioAPI = retroitUser.create(UsuarioAPI.class);

                    Call<Usuario> callUser = usuarioAPI.logar(binding.edtUsuario.getText().toString(),
                            binding.edtSenha.getText().toString());

                    callUser.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            usuario = response.body();
                            if (usuario != null) {
                                new Sessao(ctx).setUsuario(usuario);

                                carregaLayout(ctx, ListaVisitaRecyclerView.class);
                            } else {
                                Util.showAviso(ctx, R.string.aviso_login_invalido);
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Util.showAviso(ctx, R.string.aviso_erro_conexaows);
                            Log.i("onFailureLogar", "ErroLogarUsuario " + t.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void cancelar() {
        binding.fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    // limpa os campos ao logar
    private void limpaCampos() {
        binding.edtUsuario.setText("");
        binding.edtSenha.setText("");
    }

    private void carregaLayout(Context context, Class layoutDestino) {
        Intent intent = new Intent(context, layoutDestino);
        startActivity(intent);
        //limpaCampos();
    }

    // validas os campos
    private boolean camposValidos(String usuario, String senha) {
        boolean validacao = true;

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(usuario)) {
            binding.edtUsuario.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(senha)) {
            binding.edtSenha.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        return validacao;
    }

}