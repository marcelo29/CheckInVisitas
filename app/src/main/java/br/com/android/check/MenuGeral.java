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

    private RadioButton rdCadVendedor, rdCadVisita, rdLstVisita, rdVoltar;
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
                        mudaTela(CadVendedor.class);
                        break;
                    case R.id.rdCadVisita:
                        mudaTela(CadVisita.class);
                        break;
                    case R.id.rdLstVisita:
                        mudaTela(ListaVisita.class);
                        break;
                    case R.id.rdVoltar:
                        mudaTela(Login.class);
                        break;
                    default:
                        Toast.makeText(ctx, "Nenhuma opcao foi escolhida verifique novamente", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void mudaTela(Class classeDestino) {
        Intent intent = new Intent(ctx, classeDestino);
        startActivity(intent);
    }

}