package com.inmobiliaria.ui.inmuebles.detalle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.inmobiliaria.databinding.FragmentDetalleInmuebleBinding;
import com.inmobiliaria.request.ApiClient;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;
    private DetalleInmuebleViewModel viewModel;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        viewModel.getInmuebleMutable().observe(getViewLifecycleOwner(), inmueble -> {
            binding.tvCodigoDetalle.setText("Nro: " + inmueble.getIdInmueble());
            binding.tvDireccionDetalle.setText(inmueble.getDireccion());
            binding.tvUsoDetalle.setText(inmueble.getUso());
            binding.tvTipoDetalle.setText(inmueble.getTipo());
            binding.tvAmbientesDetalle.setText(String.valueOf(inmueble.getAmbientes()));
            binding.tvPrecioDetalle.setText(String.format("$%,.2f", inmueble.getValor()));
            binding.swDisponible.setChecked(inmueble.isDisponible());
            binding.swDisponible.setText(inmueble.isDisponible() ? "Disponible" : "No Disponible");

            String imageUrl = ApiClient.BASE_URL + inmueble.getImagen().replace("\\", "/");

            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.ivFotoDetalle);
        });

        viewModel.getMensajeMutable().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                viewModel.clearMensaje();
            }
        });

        binding.swDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean estaTildado = binding.swDisponible.isChecked();
                binding.swDisponible.setText(estaTildado ? "Disponible" : "No Disponible");
                viewModel.actualizarEstado(estaTildado);
            }
        });

        viewModel.recuperarDatos(getArguments());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
