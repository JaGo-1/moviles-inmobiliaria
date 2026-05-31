package com.inmobiliaria.ui.inquilinos;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.inmobiliaria.databinding.DialogDetalleInquilinoBinding;
import com.inmobiliaria.databinding.FragmentInquilinosBinding;
import com.inmobiliaria.model.Inquilino;
import com.inmobiliaria.ui.contratos.ContratosInmuebleAdapter;

public class InquilinosFragment extends Fragment {

    private FragmentInquilinosBinding binding;
    private InquilinosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);
        binding.rvInquilinosInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getInmueblesAlquilados().observe(getViewLifecycleOwner(), lista -> {
            ContratosInmuebleAdapter adapter = new ContratosInmuebleAdapter(lista, inmueble -> {
                viewModel.cargarInquilinoDelInmueble(inmueble);
            });
            binding.rvInquilinosInmuebles.setAdapter(adapter);
        });

        viewModel.getInquilinoDetalle().observe(getViewLifecycleOwner(), this::mostrarBottomSheetInquilino);

        viewModel.getError().observe(getViewLifecycleOwner(), msg ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());

        viewModel.cargarInmueblesAlquilados();
    }

    @SuppressLint("SetTextI18n")
    private void mostrarBottomSheetInquilino(Inquilino inquilino) {
        if (inquilino == null) return;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        DialogDetalleInquilinoBinding dialogBinding = DialogDetalleInquilinoBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvBsNombreInquilino.setText(inquilino.getNombre() + " " + inquilino.getApellido());
        dialogBinding.tvBsDni.setText(inquilino.getDni());
        dialogBinding.tvBsTelefono.setText(inquilino.getTelefono());
        dialogBinding.tvBsEmail.setText(inquilino.getEmail());

        bottomSheetDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}