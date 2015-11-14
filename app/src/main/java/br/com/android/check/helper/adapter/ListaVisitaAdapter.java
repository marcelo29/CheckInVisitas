package br.com.android.check.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.android.check.Datas;
import br.com.android.check.R;
import br.com.android.check.modelo.bean.Visita;
import br.com.android.check.modelo.dao.DbOpenHelper;

public class ListaVisitaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Visita> lista;

    public ListaVisitaAdapter(Context context, ArrayList<Visita> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // infla o layout recebido para o layout q chamou
            convertView = inflater.inflate(R.layout.componentes_lista_visita, null);

            final Visita visita = lista.get(position);

            ((TextView) convertView.findViewById(R.id.txtCliente)).setText(visita.getCliente());
            ((TextView) convertView.findViewById(R.id.txtEndereco)).setText(visita.getEndereco());
            ((TextView) convertView.findViewById(R.id.txtTelefone)).setText(visita.getTelefone());

            Datas util = new Datas();

            ((TextView) convertView.findViewById(R.id.txtHora)).setText(visita.getHora());
            ((TextView) convertView.findViewById(R.id.txtData)).setText(util.convertDataEmString(visita.getData()));

            ((TextView) convertView.findViewById(R.id.txtVendedor)).setText(visita.getVendedor().getNome());

            final CheckBox chkLstVisita = (CheckBox) convertView.findViewById(R.id.chkLstVisita);

            if (visita.getSituacao() == DbOpenHelper.DISPONIVEL) {
                chkLstVisita.setEnabled(true);
            } else if (visita.getSituacao() == DbOpenHelper.REALIZADA) {
                chkLstVisita.setEnabled(false);
            }

            chkLstVisita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //visita.setSituacao(REALIZADA);
                        visita.setChkMarcado(true);
                    } else {
                        //visita.setSituacao(DISPONIVEL);
                        visita.setChkMarcado(false);
                    }
                }
            });

        }
        return convertView;
    }

}