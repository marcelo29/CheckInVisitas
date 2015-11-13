package br.com.android.check;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MenuGeral extends Activity {

    private RadioButton rdCadVendedor, rdCadAdmin, rdCadVisita, rdLstVisita, rdVoltar;
    private RadioGroup rgMenu;
    private Button btnConfirmar;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setContentView(R.layout.activity_menu_geral);

        rgMenu = (RadioGroup) findViewById(R.id.rgMenu);

        rdCadVendedor = (RadioButton) findViewById(R.id.rdCadVendedor);
        rdCadVisita = (RadioButton) findViewById(R.id.rdCadVisita);
        rdCadAdmin = (RadioButton) findViewById(R.id.rdCadAdmin);
        rdLstVisita = (RadioButton) findViewById(R.id.rdLstVisita);
        rdVoltar = (RadioButton) findViewById(R.id.rdVoltar);

        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);

        escolheOpcao();
    }

    private void escolheOpcao() {
        btnConfirmar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rgMenu.getCheckedRadioButtonId()) {
                    case R.id.rdCadVendedor:
                        carregaLayout(ctx, CadVendedor.class);
                        break;
                    case R.id.rdCadVisita:
                        carregaLayout(ctx, CadVisita.class);
                        break;
                    case R.id.rdCadAdmin:
                        carregaLayout(ctx, CadUsuario.class);
                        break;
                    case R.id.rdLstVisita:
                        carregaLayout(ctx, ListaVisita.class);
                        break;
                    case R.id.rdVoltar:
                        carregaLayout(ctx, Login.class);
                        break;
                    default:
                        Toast.makeText(ctx, "Nenhuma opção foi escolhida verifique novamente", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void carregaLayout(Context ctx, Class classeDestino) {
        Intent intent = new Intent(ctx, classeDestino);
        startActivity(intent);
    }

}