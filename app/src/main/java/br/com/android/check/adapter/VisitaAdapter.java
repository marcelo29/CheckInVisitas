package br.com.android.check.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;

import br.com.android.check.R;
import br.com.android.check.databinding.ItemVisitaBinding;
import br.com.android.check.domain.Visita;

/**
 * Created by masasp29 on 04/12/15.
 */
public class VisitaAdapter extends RecyclerView.Adapter<VisitaAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context ctx;
    public static List<Visita> lista;
    public static ItemVisitaBinding itemVisitaBinding;

    public VisitaAdapter(Context ctx, List<Visita> lista) {
        this.lista = lista;
        this.ctx = ctx;
        this.layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.itemVisitaBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_visita, parent, false);
        return new ViewHolder(itemVisitaBinding.getRoot(), itemVisitaBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemVisitaBinding binding;

        public ViewHolder(View itemView, ItemVisitaBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        @UiThread
        public void bind(final Visita visita) {
            binding.chkLstVisita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        visita.setChkMarcado(true);
                    } else {
                        visita.setChkMarcado(false);
                    }
                }
            });

            if (visita.getSituacao() == Visita.EM_ANDAMENTO) {
                binding.chkLstVisita.setEnabled(true);
                visita.setTxtSituacao("Em andamento");
            } else if (visita.getSituacao() == Visita.FINALIZADA) {
                binding.chkLstVisita.setChecked(false);
                //visita.setChkMarcado(false);
                binding.chkLstVisita.setEnabled(false);
                visita.setTxtSituacao("Finalizada");
            }

            binding.setVisita(visita);
        }
    }
}