package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.android.check.api.VendedorAPI;
import br.com.android.check.controler.Mask;
import br.com.android.check.controler.ValidaCamposObrigatorios;
import br.com.android.check.databinding.ActivityCadVendedorBinding;
import br.com.android.check.domain.Vendedor;
import br.com.android.check.library.BinaryBytes;
import br.com.android.check.library.Util;
import br.com.android.check.library.VendedorDeserializer;
import br.com.android.check.ws.ConfiguracoesWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadVendedor extends AppCompatActivity {
    // constantes
    private static final int ESTADO_IMAGEM_CAPTURADA = 1;
    private static final String NOME = "nome", TELEFONE = "telefone", SENHA = "senha";

    // componentes da tela /
    private Context ctx;
    private static Bitmap foto;

    // componentes do request
    private Vendedor vendedor = null;
    private ActivityCadVendedorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cad_vendedor);

        // construindo toolbar
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.edtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", binding.edtTelefone));

        if (foto != null)
            binding.rivFoto.setImageBitmap(foto);

        if (savedInstanceState != null) {
            if (vendedor == null)
                vendedor = new Vendedor();

            vendedor.setNome(savedInstanceState.getString(NOME, ""));
            vendedor.setTelefone(savedInstanceState.getString(TELEFONE, ""));
            vendedor.setSenha(savedInstanceState.getString(SENHA, ""));

            binding.setVendedor(vendedor);
        }

        cancelar();
        fotografar();
        cadastrar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(NOME, binding.edtNome.getText().toString());
        outState.putString(TELEFONE, binding.edtTelefone.getText().toString());
        outState.putString(SENHA, binding.edtSenha.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int codigo_requistado, int resultado_codigo, Intent data) {
        if (codigo_requistado == ESTADO_IMAGEM_CAPTURADA && resultado_codigo == RESULT_OK) {
            Bundle extras = data.getExtras();
            foto = (Bitmap) extras.get("data");
            binding.rivFoto.setImageBitmap(foto);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelar() {
        binding.fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    private void fotografar() {
        binding.fabFotografar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent capturaFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (capturaFoto.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(capturaFoto, ESTADO_IMAGEM_CAPTURADA);
                }
            }
        });
    }

    private void cadastrar() {
        binding.fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vendedor == null)
                    vendedor = new Vendedor();

                vendedor.setNome(binding.edtNome.getText().toString());
                vendedor.setSenha(binding.edtSenha.getText().toString());
                vendedor.setTelefone(binding.edtTelefone.getText().toString());

                if (foto != null)
                    vendedor.setFoto(BinaryBytes.getResourceInBytes(foto));

                if (camposValidos(vendedor.getNome(), vendedor.getSenha(), vendedor.getTelefone())) {
                    // CADASTRA VENDEDOR - POST
                    Gson gson = new GsonBuilder().registerTypeAdapter(Vendedor.class,
                            new VendedorDeserializer()).create();

                    Retrofit retroit = new Retrofit
                            .Builder()
                            .baseUrl(ConfiguracoesWS.API)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                    VendedorAPI vendedorAPI = retroit.create(VendedorAPI.class);

                    final Call<Vendedor> callVendedor = vendedorAPI.inserir(vendedor);

                    callVendedor.enqueue(new Callback<Vendedor>() {
                        @Override
                        public void onResponse(Call<Vendedor> call, Response<Vendedor> response) {
                            binding.fabCadastrar.setEnabled(false);
                            Util.showAviso(ctx, R.string.vendedor_cadastrado);
                            limpaCampos();
                            binding.fabCadastrar.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<Vendedor> call, Throwable t) {
                            Log.i(ConfiguracoesWS.TAG, "ErroCadastroVendedor " + t.getMessage());
                            Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                        }
                    });
                }
            }
        });
    }

    private void limpaCampos() {
        binding.edtNome.setText("");
        binding.edtSenha.setText("");
        binding.edtTelefone.setText("");
        binding.rivFoto.setImageBitmap(null);
        foto = null;
    }

    // validas os campos
    private boolean camposValidos(String nome, String senha, String telefone) {
        boolean validacao = true;

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(nome)) {
            binding.edtNome.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(senha)) {
            binding.edtSenha.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(telefone)) {
            binding.edtTelefone.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        if (foto == null) {
            Util.showAviso(ctx, R.string.foto_em_branco);
            validacao = false;
        }

        return validacao;
    }

}