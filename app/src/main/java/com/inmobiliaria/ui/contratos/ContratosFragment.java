package com.inmobiliaria.ui.contratos;

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
import com.inmobiliaria.databinding.DialogDetalleContratoBinding;
import com.inmobiliaria.databinding.DialogDetallePagosBinding;
import com.inmobiliaria.databinding.FragmentContratosBinding;
import com.inmobiliaria.model.Contrato;
import com.inmobiliaria.model.Pago;
import com.inmobiliaria.ui.pagos.PagoReciboAdapter;

import java.util.List;

public class ContratosFragment extends Fragment {

    private FragmentContratosBinding binding;
    private ContratosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);
        binding.rvContratosInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getInmueblesAlquilados().observe(getViewLifecycleOwner(), lista -> {
            ContratosInmuebleAdapter adapter = new ContratosInmuebleAdapter(lista, inmueble -> {
                viewModel.cargarDetalleContrato(inmueble);
            });
            binding.rvContratosInmuebles.setAdapter(adapter);
        });

        viewModel.getContratoDetalle().observe(getViewLifecycleOwner(), this::mostrarBottomSheetContrato);

        viewModel.getHistorialPagos().observe(getViewLifecycleOwner(), this::mostrarBottomSheetPagos);

        viewModel.getError().observe(getViewLifecycleOwner(), msg ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());

        viewModel.cargarInmueblesAlquilados();
    }

    private void mostrarBottomSheetContrato(Contrato contrato) {
        if (contrato == null) return;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());

        DialogDetalleContratoBinding dialogBinding = DialogDetalleContratoBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvBsCodigoContrato.setText(String.valueOf(contrato.getIdContrato()));
        dialogBinding.tvBsFechaInicio.setText(contrato.getFechaInicio());
        dialogBinding.tvBsFechaFin.setText(contrato.getFechaFin());
        dialogBinding.tvBsMonto.setText(String.format("$%,.2f", contrato.getMonto()));

        dialogBinding.btnVerPagos.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            viewModel.cargarPagosDelContrato(contrato.getIdContrato());
        });

        bottomSheetDialog.show();
    }

    private void mostrarBottomSheetPagos(List<Pago> pagos) {
        if (pagos == null || pagos.isEmpty()) return;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        DialogDetallePagosBinding dialogBinding = DialogDetallePagosBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());

        dialogBinding.rvListaRecibos.setLayoutManager(new LinearLayoutManager(getContext()));
        PagoReciboAdapter reciboAdapter = new PagoReciboAdapter(pagos);
        dialogBinding.rvListaRecibos.setAdapter(reciboAdapter);

        bottomSheetDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}