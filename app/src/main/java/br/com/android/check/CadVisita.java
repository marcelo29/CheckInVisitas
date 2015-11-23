package br.com.android.check;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.android.check.library.Util;
import br.com.android.check.modelo.bean.Vendedor;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.modelo.dao.VendedorDAO;
import br.com.android.check.modelo.dao.VisitaDAO;

public class CadVisita extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText edtCliente, edtEnderedo, edtTelefone, edtData, edtHora;
    private Button btnCadastrar, btnCancelar;
    private Spinner spnVendedores;
    private Context ctx;
    private String cliente, endereco, telefone, data, hora, vendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(R.layout.activity_cad_visita);

        edtCliente = (EditText) findViewById(R.id.edtCliente);
        edtEnderedo = (EditText) findViewById(R.id.edtEndereco);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtData = (EditText) findViewById(R.id.edtData);
        edtHora = (EditText) findViewById(R.id.edtHora);

        ArrayList<String> strVendedores = populaVendedor();
        ArrayAdapter<String> adpVendedores = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                strVendedores);
        spnVendedores = (Spinner) findViewById(R.id.spnVendedor);
        spnVendedores.setAdapter(adpVendedores);

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        cadastrar();
        cancelar();
        exibeRelogio();
        exibeCalendario();
    }

    private void cadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validacao()) {

                    VisitaDAO dao = new VisitaDAO();

                    Vendedor vendedor = new VendedorDAO().retornaVendedorPorNome(spnVendedores.getSelectedItem().toString());

                    Visita visita = new Visita(Visita.EM_ANDAMENTO, cliente, endereco, telefone, hora);
                    visita.setData(data);
                    visita.setVendedor(vendedor);

                    if (dao.inserirVisita(visita)) {
                        Util.showAviso(ctx, R.string.visita_cadastrada);
                        limparCampos();
                    } else {
                        Util.showAviso(ctx, R.string.aviso_erro_cadastro);
                    }
                } else {
                    Util.showAviso(ctx, R.string.aviso_validacao_login);
                }
            }
        });
    }

    private void cancelar() {
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });
    }

    private void exibeCalendario() {
        edtData.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Util.showAviso(ctx, R.string.aviso_clique_data);
                edtData.setText("");
                return false;
            }
        });
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

    }

    private void exibeRelogio() {
        edtHora.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Util.showAviso(ctx, R.string.aviso_clique_hora);
                edtHora.setText("");
                return false;
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
        edtCliente.setBackgroundColor(Color.WHITE);
        edtCliente.setText("");
        edtEnderedo.setBackgroundColor(Color.WHITE);
        edtEnderedo.setText("");
        edtTelefone.setBackgroundColor(Color.WHITE);
        edtTelefone.setText("");
        edtData.setBackgroundColor(Color.WHITE);
        edtData.setText("");
        edtHora.setBackgroundColor(Color.WHITE);
        edtHora.setText("");
    }

    // validas os campos
    public boolean validacao() {
        Boolean valido = true;

        cliente = edtCliente.getText().toString();
        endereco = edtEnderedo.getText().toString();
        telefone = edtTelefone.getText().toString();
        data = edtData.getText().toString();
        hora = edtHora.getText().toString();
        String vendedor = spnVendedores.getSelectedItem().toString();

        if (cliente == null || cliente.equals("")) {
            edtCliente.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtCliente.setBackgroundColor(Color.WHITE);
        }

        if (endereco == null || endereco.equals("")) {
            edtEnderedo.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtEnderedo.setBackgroundColor(Color.WHITE);
        }

        if (telefone == null || telefone.equals("")) {
            edtTelefone.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtTelefone.setBackgroundColor(Color.WHITE);
        }

        if (data == null || data.equals("")) {
            edtData.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtData.setBackgroundColor(Color.WHITE);
        }

        if (hora == null || hora.equals("")) {
            edtHora.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            edtHora.setBackgroundColor(Color.WHITE);
        }

        if (vendedor == null || vendedor.equals("")) {
            spnVendedores.setBackgroundColor(Color.RED);
            valido = false;
        } else {
            spnVendedores.setBackgroundColor(Color.WHITE);
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
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String time = "" + dayOfMonth + "/" + monthOfYear + "/" + year + "";
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
}