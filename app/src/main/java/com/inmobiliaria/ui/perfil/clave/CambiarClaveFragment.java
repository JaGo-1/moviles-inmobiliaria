package com.inmobiliaria.ui.perfil.clave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.inmobiliaria.databinding.FragmentCambiarClaveBinding;

public class CambiarClaveFragment extends Fragment {

    private FragmentCambiarClaveBinding binding;
    private CambiarClaveViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCambiarClaveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CambiarClaveViewModel.class);
        NavController navController = Navigation.findNavController(view);

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });

        viewModel.getClaveCambiada().observe(getViewLifecycleOwner(), exitoso -> {
            if (exitoso) {
                Toast.makeText(getContext(), "Contraseña modificada con éxito", Toast.LENGTH_SHORT).show();
                limpiarCampos();

                navController.popBackStack();
            }
        });

        binding.btnCambiarPassword.setOnClickListener(v -> {
            String claveActual = binding.etClaveActual.getText().toString();
            String nuevaClave = binding.etNuevaClave.getText().toString();
            String repetirNueva = binding.etRepetirNuevaClave.getText().toString();

            viewModel.procesarCambioClave(claveActual, nuevaClave, repetirNueva);
        });
    }

    private void limpiarCampos() {
        binding.etClaveActual.setText("");
        binding.etNuevaClave.setText("");
        binding.etRepetirNuevaClave.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}