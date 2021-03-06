package br.com.android.check;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.android.check.api.VendedorAPI;
import br.com.android.check.api.VisitaAPI;
import br.com.android.check.controler.Mask;
import br.com.android.check.databinding.ActivityCadVisitaBinding;
import br.com.android.check.domain.Vendedor;
import br.com.android.check.domain.Visita;
import br.com.android.check.library.Util;
import br.com.android.check.library.VendedorDeserializer;
import br.com.android.check.library.VisitaDeserializer;
import br.com.android.check.ws.ConfiguracoesWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadVisita extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private Context ctx;
    private String cliente, endereco, telefone, data, hora, vendedor;

    private Vendedor vendedorSelecionado = null;
    private Visita visita = null;
    private ActivityCadVisitaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cad_visita);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.edtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", binding.edtTelefone));

        //adiciona mascara ao texto
        binding.edtData.addTextChangedListener(Mask.insert("##/##/####", binding.edtData));

        List<String> strVendedores = populaVendedor();
        ArrayAdapter<String> adpVendedores = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                strVendedores);
        binding.spnVendedor.setAdapter(adpVendedores);

        cadastrar();
        cancelar();
        exibeRelogio();
        exibeCalendario();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        String monthOfYearString = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;
        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String time = "" + dayOfMonthString + "/" + monthOfYearString + "/" + year + "";
        binding.edtData.setText(time);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "" + hourString + ":" + minuteString + ":" + secondString + "";
        binding.edtHora.setText(time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cad_visita, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void cadastrar() {
        binding.fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validacao()) {
                    // RETORNA VENDEDOR - REQUEST
                    Gson gson = new GsonBuilder().registerTypeAdapter(Vendedor.class,
                            new VendedorDeserializer()).create();

                    Retrofit retroit = new Retrofit
                            .Builder()
                            .baseUrl(ConfiguracoesWS.API)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                    VendedorAPI vendedorAPI = retroit.create(VendedorAPI.class);

                    Call<Vendedor> call = vendedorAPI.retornaVendedorPorNome(binding.spnVendedor.getSelectedItem().
                            toString());

                    call.enqueue(new Callback<Vendedor>() {
                        @Override
                        public void onResponse(Call<Vendedor> call, Response<Vendedor> response) {
                            vendedorSelecionado = response.body();
                        }

                        @Override
                        public void onFailure(Call<Vendedor> call, Throwable t) {
                            Log.i(ConfiguracoesWS.TAG, "ErroRetornaVendedorPorNome " + t.getMessage());
                            Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                        }
                    });

                    // CADASTRA VISITA - REQUEST
                    Gson gsonVisita = new GsonBuilder().registerTypeAdapter(Visita.class,
                            new VisitaDeserializer()).create();

                    Retrofit retroitVisita = new Retrofit
                            .Builder()
                            .baseUrl(ConfiguracoesWS.API)
                            .addConverterFactory(GsonConverterFactory.create(gsonVisita))
                            .build();
                    VisitaAPI visitaAPI = retroitVisita.create(VisitaAPI.class);

                    if (visita == null)
                        visita = new Visita(Visita.EM_ANDAMENTO, cliente, endereco, telefone, hora);
                    visita.setData(data);
                    visita.setVendedor(vendedorSelecionado);

                    Call<Visita> callVisita = visitaAPI.inserir(visita);

                    callVisita.enqueue(new Callback<Visita>() {
                        @Override
                        public void onResponse(Call<Visita> call, Response<Visita> response) {
                            Visita visitaCad = response.body();
                            if (visitaCad != null) {
                                Util.showAviso(ctx, R.string.visita_cadastrada);
                                limparCampos();
                            }
                            binding.setVisita(visitaCad);
                        }

                        @Override
                        public void onFailure(Call<Visita> call, Throwable t) {
                            Log.i(ConfiguracoesWS.TAG, "ErroCadastraVisita " + t.getMessage());
                            Util.showAviso(ctx, R.string.aviso_erro_cadastro);
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
                limparCampos();
            }
        });
    }

    private void exibeCalendario() {
        binding.edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CadVisita.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setThemeDark(true);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.showYearPickerFirst(false);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        binding.edtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Util.showAviso(ctx, R.string.aviso_clique_data);
                }
            }
        });
    }

    private void exibeRelogio() {
        binding.edtHora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Util.showAviso(ctx, R.string.aviso_clique_hora);
                }
            }
        });
        binding.edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CadVisita.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.enableSeconds(true);
                tpd.setThemeDark(true);
                tpd.vibrate(true);
                tpd.dismissOnPause(true);

                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    private void limparCampos() {
        binding.edtCliente.setText("");
        binding.edtEndereco.setText("");
        binding.edtTelefone.setText("");
        binding.edtData.setText("");
        binding.edtHora.setText("");
        binding.spnVendedor.setSelection(0);
    }

    // validas os campos
    public boolean validacao() {
        Boolean valido = true;

        cliente = binding.edtCliente.getText().toString();
        endereco = binding.edtEndereco.getText().toString();
        telefone = binding.edtTelefone.getText().toString();
        data = binding.edtData.getText().toString();
        hora = binding.edtHora.getText().toString();
        vendedor = binding.spnVendedor.getSelectedItem().toString();

        if (cliente == null || cliente.equals("")) {
            binding.edtCliente.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (endereco == null || endereco.equals("")) {
            binding.edtEndereco.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (telefone == null || telefone.equals("")) {
            binding.edtTelefone.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (data == null || data.equals("")) {
            binding.edtData.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (hora == null || hora.equals("")) {
            binding.edtHora.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (vendedor == null || vendedor.equals("")) {
            binding.spnVendedor.setBackgroundColor(Color.RED);
            valido = false;
        }

        return valido;
    }

    private List<String> populaVendedor() {
        final List<String> lista = new ArrayList<>();
        lista.add("");

        // RETORNA VENDEDORES - REQUEST
        Gson gson = new GsonBuilder().registerTypeAdapter(Vendedor.class,
                new VendedorDeserializer()).create();

        Retrofit retroit = new Retrofit
                .Builder()
                .baseUrl(ConfiguracoesWS.API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        VendedorAPI vendedorAPI = retroit.create(VendedorAPI.class);

        final Call<List<Vendedor>> call = vendedorAPI.listar();

        new Thread() {
            @Override
            public void run() {
                super.run();

                List<Vendedor> vendedores;

                try {
                    vendedores = call.execute().body();

                    if (vendedores != null) {
                        for (Vendedor v : vendedores) {
                            lista.add(v.getNome());
                        }
                    }
                } catch (IOException e) {
                    Log.i(ConfiguracoesWS.TAG, "ErroListaVendedores " + e.getMessage());
                    Util.showAviso(ctx, R.string.aviso_erro_lista_vendedores);
                    e.printStackTrace();
                } finally {
                    onStop();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("onSucess", "RETORNA VENDEDORES - REQUEST ok");
                    }
                });
            }
        }.start();

        return lista;
    }

}