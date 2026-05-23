package com.inmobiliaria.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inmobiliaria.databinding.ItemInmuebleBinding;
import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {

    private List<Inmueble> listaInmuebles;

    public InmuebleAdapter(List<Inmueble> listaInmuebles) {
        this.listaInmuebles = listaInmuebles;
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

                    String token = ApiClient.obtenerToken(
                            holder.itemView.getContext()
                    );

                    Call<Inmueble> call = ApiClient
                            .getServicio()
                            .actualizarInmueble(token, inmueble);

                    call.enqueue(new Callback<Inmueble>() {
                        @Override
                        public void onResponse(Call<Inmueble> call,
                                               Response<Inmueble> response) {

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        holder.itemView.getContext(),
                                        "Estado actualizado",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        holder.itemView.getContext(),
                                        "Error al actualizar",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Inmueble> call,
                                              Throwable t) {

                            Toast.makeText(
                                    holder.itemView.getContext(),
                                    "Error de conexión",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }
        );

        String imagenUrl = ApiClient.BASE_URL +
                inmueble.getImagen().replace("\\", "/");

        Glide.with(holder.itemView.getContext())
                .load(imagenUrl)
                .into(holder.binding.ivImagen);
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
    }

    public static class InmuebleViewHolder extends RecyclerView.ViewHolder {

        private final ItemInmuebleBinding binding;

        public InmuebleViewHolder(ItemInmuebleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}