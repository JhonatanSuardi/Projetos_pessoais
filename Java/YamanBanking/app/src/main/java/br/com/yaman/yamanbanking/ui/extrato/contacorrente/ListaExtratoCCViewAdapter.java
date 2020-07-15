package br.com.yaman.yamanbanking.ui.extrato.contacorrente;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.yaman.yamanbanking.R;

class ListaExtratoCCViewAdapter extends RecyclerView.Adapter<ListaExtratoCCViewHolder> {

    public List<ListExtratoCC> listaExtratoCC;


    public ListaExtratoCCViewAdapter(List<ListExtratoCC> listaExtratoCC) {
        this.listaExtratoCC = listaExtratoCC;
    }

    @Override
    public ListaExtratoCCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_extratocc, parent, false);
        ListaExtratoCCViewHolder mViewHolder = new ListaExtratoCCViewHolder(itemView);

        return mViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void  onBindViewHolder(ListaExtratoCCViewHolder holder, int position){
        ListExtratoCC itemcc = listaExtratoCC.get(position);
        holder.datacc.setText(itemcc.getDataCC());
        holder.valorcc.setText(itemcc.getValorCC());
        holder.descrcc.setText(itemcc.getDescrCC());

        if (itemcc.getValorCC().contains("-")){
            holder.valorcc.setTextColor(R.color.colorAccent);
        }
    }

    public int getItemCount() {
        return listaExtratoCC.size();
    }
}

