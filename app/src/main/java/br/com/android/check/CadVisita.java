package br.com.android.check;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.android.check.api.VendedorAPI;
import br.com.android.check.api.VisitaAPI;
import br.com.android.check.controler.Mask;
import br.com.android.check.library.Util;
import br.com.android.check.model.bean.Vendedor;
import br.com.android.check.model.bean.Visita;
import br.com.android.check.model.dao.VendedorDAO;
import br.com.android.check.util.VendedorDeserializer;
import br.com.android.check.util.VisitaDeserializer;
import br.com.android.check.ws.ConfiguracoesWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadVisita extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private EditText edtCliente, edtEnderedo, edtTelefone, edtData, edtHora;
    //private MaskedEditText metData;
    private FloatingActionButton fabCadastrar, fabCancelar;
    private Spinner spnVendedores;
    private Context ctx;
    private String cliente, endereco, telefone, data, hora, vendedor;
    private Toolbar toolbar;

    private Vendedor vendedorSelecionado = null;
    private Visita visita, visitaCad = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_cad_visita);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        edtCliente = (EditText) findViewById(R.id.edtCliente);
        edtEnderedo = (EditText) findViewById(R.id.edtEndereco);

        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", edtTelefone));

        //adiciona mascara ao texto
        edtData = (EditText) findViewById(R.id.edtData);
        edtData.addTextChangedListener(Mask.insert("##/##/####", edtData));

        edtHora = (EditText) findViewById(R.id.edtHora);

        ArrayList<String> strVendedores = populaVendedor();
        ArrayAdapter<String> adpVendedores = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                strVendedores);
        spnVendedores = (Spinner) findViewById(R.id.spnVendedor);
        spnVendedores.setAdapter(adpVendedores);

        fabCadastrar = (FloatingActionButton) findViewById(R.id.fabCadastrar);
        fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);

        //cadastrar();
        cancelar();
        exibeRelogio();
        exibeCalendario();
    }

    private void cancelar() {
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });
    }

    private void exibeCalendario() {
        edtData.setOnClickListener(new View.OnClickListener() {
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

        edtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    Util.showAviso(ctx, R.string.aviso_clique_data);
                }
            }
        });
    }

    private void exibeRelogio() {
        edtHora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    Util.showAviso(ctx, R.string.aviso_clique_hora);
                }
            }
        });
        edtHora.setOnClickListener(new View.OnClickListener() {
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
        edtCliente.setText("");
        edtEnderedo.setText("");
        edtTelefone.setText("");
        edtData.setText("");
        edtHora.setText("");
        spnVendedores.setSelection(0);
    }

    // validas os campos
    public boolean validacao() {
        Boolean valido = true;

        cliente = edtCliente.getText().toString();
        endereco = edtEnderedo.getText().toString();
        telefone = edtTelefone.getText().toString();
        data = edtData.getText().toString();
        hora = edtHora.getText().toString();
        vendedor = spnVendedores.getSelectedItem().toString();

        if (cliente == null || cliente.equals("")) {
            edtCliente.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (endereco == null || endereco.equals("")) {
            edtEnderedo.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (telefone == null || telefone.equals("")) {
            edtTelefone.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (data == null || data.equals("")) {
            edtData.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (hora == null || hora.equals("")) {
            edtHora.setError(Util.AVISO_CAMPO_OBRIGATORIO);
            valido = false;
        }

        if (vendedor == null || vendedor.equals("")) {
            spnVendedores.setBackgroundColor(Color.RED);
            valido = false;
        }

        return valido;
    }

    private ArrayList<String> populaVendedor() {
        VendedorDAO vdao = new VendedorDAO();
        ArrayList<String> lista = new ArrayList<String>();
        lista.add("");
        ArrayList<Vendedor> vendedores = vdao.listaVendedores();
        for (int i = 0; i < vendedores.size(); i++) {
            lista.add(vendedores.get(i).getNome());
        }
        return lista;
    }

    @Override
    protected void onResume() {
        super.onResume();

        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
        if (dpd != null) dpd.setOnDateSetListener(this);

        fabCadastrar.setOnClickListener(new View.OnClickListener() {
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

                    Call<Vendedor> call = vendedorAPI.retornaVendedorPorNome(spnVendedores.getSelectedItem().
                            toString());

                    call.enqueue(new Callback<Vendedor>() {
                        @Override
                        public void onResponse(Call<Vendedor> call, Response<Vendedor> response) {
                            Vendedor v = response.body();
                            vendedorSelecionado = v;
                        }

                        @Override
                        public void onFailure(Call<Vendedor> call, Throwable t) {
                            Log.i(ConfiguracoesWS.TAG, "ErroRetornaVendedorPorNome " + t.getMessage());
                            Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                        }
                    });

                    // CADASTRA VISITA - REQUEST
                    gson = new GsonBuilder().registerTypeAdapter(Visita.class, new VisitaDeserializer()).create();

                    retroit = new Retrofit
                            .Builder()
                            .baseUrl(ConfiguracoesWS.API)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                    VisitaAPI visitaAPI = retroit.create(VisitaAPI.class);

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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        String monthOfYearString = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;
        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String time = "" + dayOfMonthString + "/" + monthOfYearString + "/" + year + "";
        edtData.setText(time);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "" + hourString + ":" + minuteString + ":" + secondString + "";
        edtHora.setText(time);
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

}