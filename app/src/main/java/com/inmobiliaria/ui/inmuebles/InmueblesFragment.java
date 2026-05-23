package com.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.inmobiliaria.R;
import com.inmobiliaria.databinding.FragmentInmueblesBinding;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmueblesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentInmueblesBinding.inflate(
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
                .get(InmueblesViewModel.class);

        binding.rvInmuebles.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        binding.rvInmuebles.setHasFixedSize(true);

        // BOTON +
        binding.fabAgregarInmueble.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(
                    R.id.action_nav_inmuebles_to_agregarInmuebleFragment
            );

        });

        viewModel.getMensaje().observe(
                getViewLifecycleOwner(),
                mensaje -> {

                    Toast.makeText(
                            getContext(),
                            mensaje,
                            Toast.LENGTH_LONG
                    ).show();

                }
        );

        viewModel.getListaInmuebles().observe(
                getViewLifecycleOwner(),
                inmuebles -> {

                    if (inmuebles != null) {

                        Log.d(
                                "INMUEBLES",
                                "Cantidad: " + inmuebles.size()
                        );

                        InmuebleAdapter adapter =
                                new InmuebleAdapter(inmuebles);

                        binding.rvInmuebles.setAdapter(adapter);
                    }
                }
        );

        viewModel.obtenerInmuebles();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}