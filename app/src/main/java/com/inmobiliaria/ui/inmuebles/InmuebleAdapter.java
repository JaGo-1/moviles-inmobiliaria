package com.inmobiliaria.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inmobiliaria.databinding.ItemInmuebleBinding;
import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.request.ApiClient;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {

    public interface OnDisponibilidadChangeListener {
        void onDisponibilidadChanged(Inmueble inmueble);
    }

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    private List<Inmueble> listaInmuebles;
    private OnDisponibilidadChangeListener listener;
    private OnItemClickListener itemClickListener;

    public InmuebleAdapter(
            List<Inmueble> listaInmuebles,
            OnDisponibilidadChangeListener listener,
            OnItemClickListener itemClickListener
    ) {
        this.listaInmuebles = listaInmuebles;
        this.listener = listener;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemInmuebleBinding binding = ItemInmuebleBinding.inflate(
                inflater,
                parent,
                false
        );

        return new InmuebleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {

        Inmueble inmueble = listaInmuebles.get(position);

        holder.binding.tvDireccion.setText(
                inmueble.getDireccion()
        );

        holder.binding.tvTipo.setText(
                "Tipo: " + inmueble.getTipo()
        );

        holder.binding.tvUso.setText(
                "Uso: " + inmueble.getUso()
        );

        holder.binding.tvPrecio.setText(
                "$ " + inmueble.getValor()
        );

        holder.binding.swDisponible.setOnCheckedChangeListener(null);

        holder.binding.swDisponible.setChecked(
                inmueble.isDisponible()
        );

        holder.binding.swDisponible.setText(
                inmueble.isDisponible() ? "Disponible" : "No Disponible"
        );

        holder.binding.swDisponible.setOnCheckedChangeListener(
                (CompoundButton buttonView, boolean isChecked) -> {

                    inmueble.setDisponible(isChecked);

                    holder.binding.swDisponible.setText(
                            isChecked ? "Disponible" : "No Disponible"
                    );

                    if (listener != null) {
                        listener.onDisponibilidadChanged(inmueble);
                    }
                }
        );

        String imagenUrl = ApiClient.BASE_URL +
                inmueble.getImagen().replace("\\", "/");

        Glide.with(holder.itemView.getContext())
                .load(imagenUrl)
                .into(holder.binding.ivImagen);

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(inmueble);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
    }

    public void setListaInmuebles(List<Inmueble> list) {
        this.listaInmuebles = list;
        notifyDataSetChanged();
    }

    public static class InmuebleViewHolder extends RecyclerView.ViewHolder {

        private final ItemInmuebleBinding binding;

        public InmuebleViewHolder(ItemInmuebleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}