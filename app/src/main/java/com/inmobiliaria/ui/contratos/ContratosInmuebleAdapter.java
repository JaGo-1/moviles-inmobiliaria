package com.inmobiliaria.ui.contratos;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.inmobiliaria.databinding.ItemInmuebleAlquiladoBinding;
import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.request.ApiClient;
import java.util.List;

public class ContratosInmuebleAdapter extends RecyclerView.Adapter<ContratosInmuebleAdapter.ViewHolder> {

    private final List<Inmueble> listaInmuebles;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    public ContratosInmuebleAdapter(List<Inmueble> listaInmuebles, OnItemClickListener listener) {
        this.listaInmuebles = listaInmuebles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInmuebleAlquiladoBinding binding = ItemInmuebleAlquiladoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = listaInmuebles.get(position);

        holder.binding.tvDireccion.setText(inmueble.getDireccion());
        holder.binding.tvTipoUso.setText(inmueble.getTipo() + " • " + inmueble.getUso());
        holder.binding.tvPrecio.setText("$ " + inmueble.getValor());

        String imagenUrl = ApiClient.BASE_URL + inmueble.getImagen().replace("\\", "/");
        Glide.with(holder.itemView.getContext())
                .load(imagenUrl)
                .into(holder.binding.ivImagen);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(inmueble));
    }

    @Override
    public int getItemCount() {
        return listaInmuebles != null ? listaInmuebles.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemInmuebleAlquiladoBinding binding;
        public ViewHolder(@NonNull ItemInmuebleAlquiladoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}