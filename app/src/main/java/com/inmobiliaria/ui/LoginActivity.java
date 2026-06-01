package com.inmobiliaria.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityLoginBinding binding;
    private LoginViewModel vm;

    private SensorManager sensorManager;
    private Sensor acelerometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(LoginViewModel.class);

        sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            acelerometro =
                    sensorManager.getDefaultSensor(
                            Sensor.TYPE_ACCELEROMETER
                    );
        }

        vm.getLlamarInmobiliaria().observe(this, llamar -> {

            if (llamar != null && llamar) {
                hacerLlamada();
            }

        });

        binding.btnLogin.setOnClickListener(v -> {

            String usuario = binding.etUsuario.getText().toString();
            String clave = binding.etClave.getText().toString();

            vm.login(usuario, clave);
        });

        binding.tvOlvidePassword.setOnClickListener(v -> {
            vm.resetearPassword();
        });

        vm.getMensaje().observe(this, mensaje -> {

            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_SHORT
            ).show();

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensorManager != null &&
                acelerometro != null) {

            sensorManager.registerListener(
                    this,
                    acelerometro,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()
                == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            vm.procesarMovimientoSensor(
                    x,
                    y,
                    z
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void hacerLlamada() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    1
            );

            return;
        }

        Intent intent =
                new Intent(Intent.ACTION_CALL);

        intent.setData(
                Uri.parse("tel:2664553747")
        );

        startActivity(intent);
    }
}