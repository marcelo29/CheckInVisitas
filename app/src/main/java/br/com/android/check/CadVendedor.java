package br.com.android.check;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.android.check.api.VendedorAPI;
import br.com.android.check.controler.Mask;
import br.com.android.check.controler.ValidaCamposObrigatorios;
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

    // componentes da tela /
    private Context ctx;
    private FloatingActionButton fabCadastrar, fabCancelar, fabFotografar;
    private Toolbar toolbar;
    private EditText edtNome, edtSenha, edtTelefone;
    private Bitmap foto;
    private ImageView imgFoto;

    // componentes do request
    private Vendedor vendedor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_cad_vendedor);

        // construindo toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // recebendo componentes do xhtml
        fabCadastrar = (FloatingActionButton) findViewById(R.id.fabCadastrar);
        fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);
        fabFotografar = (FloatingActionButton) findViewById(R.id.fabFotografar);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", edtTelefone));

        imgFoto = (ImageView) findViewById(R.id.rivFoto);

        if (foto != null)
            imgFoto.setImageBitmap(foto);

        cancelar();
        fotografar();
        cadastrar();
    }

    @Override
    protected void onActivityResult(int codigo_requistado, int resultado_codigo, Intent data) {
        if (codigo_requistado == ESTADO_IMAGEM_CAPTURADA && resultado_codigo == RESULT_OK) {
            Bundle extras = data.getExtras();
            foto = (Bitmap) extras.get("data");
            imgFoto.setImageBitmap(foto);
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
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpaCampos();
            }
        });
    }

    private void fotografar() {
        fabFotografar.setOnClickListener(new View.OnClickListener() {
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
        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendedor = new Vendedor();
                vendedor.setNome(edtNome.getText().toString());
                vendedor.setSenha(edtSenha.getText().toString());
                vendedor.setTelefone(edtTelefone.getText().toString());
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
                            Util.showAviso(ctx, R.string.vendedor_cadastrado);
                            limpaCampos();
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
        edtNome.setText("");
        edtSenha.setText("");
        edtTelefone.setText("");
        foto = null;
        imgFoto.setImageBitmap(null);
    }

    // validas os campos
    private boolean camposValidos(String nome, String senha, String telefone) {
        boolean validacao = true;

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(nome)) {
            edtNome.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(senha)) {
            edtSenha.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        if (ValidaCamposObrigatorios.seCampoEstaNuloOuEmBranco(telefone)) {
            edtTelefone.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            validacao = false;
        }

        if (foto == null) {
            Util.showAviso(ctx, R.string.foto_em_branco);
            validacao = false;
        }

        return validacao;
    }

}