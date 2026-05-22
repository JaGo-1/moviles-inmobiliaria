package com.inmobiliaria.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.inmobiliaria.databinding.FragmentInicioBinding;

import org.maplibre.android.MapLibre;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding b;
    private InicioViewModel vm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapLibre.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInicioBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        b.mapView.onCreate(savedInstanceState);

        vm.getMapActual().observe(getViewLifecycleOwner(), manejadorMapa -> {
            if (manejadorMapa != null) {
                b.mapView.getMapAsync(manejadorMapa);
            }
        });

        vm.cargarMapa();

        return b.getRoot();
    }

    @Override
    public void onStart() { super.onStart(); b.mapView.onStart(); }

    @Override
    public void onResume() { super.onResume(); b.mapView.onResume(); }

    @Override
    public void onPause() { super.onPause(); b.mapView.onPause(); }

    @Override
    public void onStop() { super.onStop(); b.mapView.onStop(); }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (b != null) b.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() { super.onLowMemory(); if (b != null) b.mapView.onLowMemory(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (b != null) b.mapView.onDestroy();
        b = null;
    }
}