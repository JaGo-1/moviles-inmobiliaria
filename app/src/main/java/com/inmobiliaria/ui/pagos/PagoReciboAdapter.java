package com.inmobiliaria.ui.pagos;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.inmobiliaria.databinding.ItemPagoReciboBinding;
import com.inmobiliaria.model.Pago;
import java.util.List;

public class PagoReciboAdapter extends RecyclerView.Adapter<PagoReciboAdapter.ViewHolder> {

    private final List<Pago> listaPagos;

    public PagoReciboAdapter(List<Pago> listaPagos) {
        this.listaPagos = listaPagos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPagoReciboBinding binding = ItemPagoReciboBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pago pago = listaPagos.get(position);

        holder.binding.tvNroPago.setText("Número de pago: " + pago.getNroPago());
        holder.binding.tvImporte.setText(String.format("$%,.1f", pago.getImporte()));
        holder.binding.tvIdsFlotantes.setText("Código pago: " + pago.getIdPago() + " | Contrato: " + pago.getIdAlquiler());
        holder.binding.tvFechaPago.setText("Fecha de pago: " + pago.getFecha());
    }

    @Override
    public int getItemCount() {
        return listaPagos != null ? listaPagos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemPagoReciboBinding binding;
        public ViewHolder(@NonNull ItemPagoReciboBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}