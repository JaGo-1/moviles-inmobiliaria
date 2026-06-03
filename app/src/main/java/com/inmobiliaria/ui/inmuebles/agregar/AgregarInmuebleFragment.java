package com.inmobiliaria.ui.inmuebles.agregar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inmobiliaria.databinding.FragmentAgregarInmuebleBinding;

public class AgregarInmuebleFragment extends Fragment {

    private FragmentAgregarInmuebleBinding binding;
    private AgregarInmuebleViewModel viewModel;

    private ActivityResultLauncher<Intent> selectorImagen;
    private Intent intentGaleria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAgregarInmuebleBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(AgregarInmuebleViewModel.class);

        abrirGaleria();

        cargarSpinners();

        viewModel.getImagen().observe(getViewLifecycleOwner(), uri -> {

            binding.ivImagen.setImageURI(uri);

        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), mensaje -> {

            Toast.makeText(
                    getContext(),
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();

        });

        binding.btnSeleccionarImagen.setOnClickListener(v -> {

            selectorImagen.launch(intentGaleria);

        });

        binding.btnGuardar.setOnClickListener(v -> {

            viewModel.guardarInmueble(
                    binding.etDireccion.getText().toString(),
                    binding.spUso.getSelectedItem().toString(),
                    binding.spTipo.getSelectedItem().toString(),
                    binding.etAmbientes.getText().toString(),
                    binding.etSuperficie.getText().toString(),
                    binding.etValor.getText().toString()
            );

        });
    }

    private void cargarSpinners() {

        String[] usos = {
                "Residencial",
                "Comercial",
                "Personal",
                "Doméstico"
        };

        String[] tipos = {
                "Casa",
                "Departamento",
                "Local Comercial",
                "Loft"
        };

        ArrayAdapter<String> adapterUsos =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        usos
                );

        adapterUsos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        binding.spUso.setAdapter(adapterUsos);

        ArrayAdapter<String> adapterTipos =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        tipos
                );

        adapterTipos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        binding.spTipo.setAdapter(adapterTipos);
    }

    private void abrirGaleria() {

        intentGaleria = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        selectorImagen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> viewModel.recibirImagen(result)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}