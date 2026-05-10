package com.inmobiliaria.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(LoginViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {

            String usuario = binding.etUsuario.getText().toString();
            String clave = binding.etClave.getText().toString();

            vm.login(usuario, clave);
        });

        vm.getMensaje().observe(this, mensaje -> {

            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_SHORT
            ).show();

        });
    }
}