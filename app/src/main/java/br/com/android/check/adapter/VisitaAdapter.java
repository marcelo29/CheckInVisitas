package br.com.android.check.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import br.com.android.check.R;
import br.com.android.check.modelo.bean.Visita;

/**
 * Created by masasp29 on 04/12/15.
 */
public class VisitaAdapter extends RecyclerView.Adapter<VisitaAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Visita> lista;

    public VisitaAdapter(Context ctx, List<Visita> lista) {
        this.lista = lista;
        this.layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public VisitaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_visita, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(VisitaAdapter.ViewHolder vh, int position) {
        final Visita visita = lista.get(position);

        vh.txtCliente.setText(visita.getCliente());
        vh.txtEndereco.setText(visita.getEndereco());
        vh.txtTelefone.setText(visita.getTelefone());
        vh.txtHora.setText(visita.getHora());
        vh.txtData.setText(visita.getData());
        vh.txtVendedor.setText(visita.getVendedor().getNome());

        if (visita.getSituacao() == Visita.EM_ANDAMENTO) {
            vh.chkLstVisita.setEnabled(true);
            vh.txtSituacao.setText(R.string.em_andamento);
        } else if (visita.getSituacao() == Visita.FINALIZADA) {
            vh.chkLstVisita.setEnabled(false);
            vh.txtSituacao.setText(R.string.finalizada);
        }

        vh.chkLstVisita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    visita.setChkMarcado(true);
                } else {
                    visita.setChkMarcado(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCliente, txtEndereco, txtTelefone, txtHora, txtData, txtVendedor, txtSituacao;
        private CheckBox chkLstVisita;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCliente = (TextView) itemView.findViewById(R.id.txtCliente);
            txtEndereco = (TextView) itemView.findViewById(R.id.txtEndereco);
            txtTelefone = (TextView) itemView.findViewById(R.id.txtTelefone);
            txtHora = (TextView) itemView.findViewById(R.id.txtHora);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtVendedor = (TextView) itemView.findViewById(R.id.txtVendedor);
            txtSituacao = (TextView) itemView.findViewById(R.id.txtSituacao);

            chkLstVisita = (CheckBox) itemView.findViewById(R.id.chkLstVisita);

            //itemView.setOnClickListener(this);
        }
    }
}
