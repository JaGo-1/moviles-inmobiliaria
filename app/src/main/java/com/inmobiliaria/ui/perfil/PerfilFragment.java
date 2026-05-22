package com.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.inmobiliaria.R;
import com.inmobiliaria.databinding.FragmentPerfilBinding;
import com.inmobiliaria.model.Propietario;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private PerfilViewModel viewModel;
    private boolean esModoEdicion = false;
    private Propietario propietarioActual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        setCamposEditables(false);

        viewModel.getPropietario().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                propietarioActual = propietario;
                inyectarDatos(propietario);
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            setCamposEditables(false);
        });

        viewModel.obtenerDatos();

        binding.btnEditarGuardar.setOnClickListener(v -> {
            if (!esModoEdicion) {
                setCamposEditables(true);
            } else {
                propietarioActual.setNombre(binding.etNombre.getText().toString().trim());
                propietarioActual.setApellido(binding.etApellido.getText().toString().trim());
                propietarioActual.setDni(binding.etDni.getText().toString().trim());
                propietarioActual.setTelefono(binding.etTelefono.getText().toString().trim());
                propietarioActual.setEmail(binding.etEmail.getText().toString().trim());

                viewModel.actualizarDatos(propietarioActual);
            }
        });

        binding.btnIrCambiarClave.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_perfil_to_nav_cambiar_clave);
        });
    }

    private void inyectarDatos(Propietario p) {
        binding.etId.setText(String.valueOf(p.getIdPropietario()));
        binding.etNombre.setText(p.getNombre());
        binding.etApellido.setText(p.getApellido());
        binding.etDni.setText(p.getDni());
        binding.etTelefono.setText(p.getTelefono());
        binding.etEmail.setText(p.getEmail());
    }

    private void setCamposEditables(boolean editable) {
        esModoEdicion = editable;

        binding.etId.setEnabled(false);

        binding.etNombre.setEnabled(editable);
        binding.etApellido.setEnabled(editable);
        binding.etDni.setEnabled(editable);
        binding.etTelefono.setEnabled(editable);
        binding.etEmail.setEnabled(editable);

        if (editable) {
            binding.btnEditarGuardar.setText("Guardar Cambios");
        } else {
            binding.btnEditarGuardar.setText("Editar Perfil");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

