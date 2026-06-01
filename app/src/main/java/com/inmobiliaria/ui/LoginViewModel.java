package com.inmobiliaria.ui;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inmobiliaria.MainActivity;
import com.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje;

    private MutableLiveData<Boolean> llamarInmobiliaria;
    private long ultimoTiempoShake = 0;
    private int contadorSacudidas = 0;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensaje() {

        if (mensaje == null) {
            mensaje = new MutableLiveData<>();
        }

        return mensaje;
    }

    public LiveData<Boolean> getLlamarInmobiliaria() {

        if (llamarInmobiliaria == null) {
            llamarInmobiliaria = new MutableLiveData<>();
        }

        return llamarInmobiliaria;
    }

    public void login(String usuario, String clave) {

        if (usuario.isEmpty() || clave.isEmpty()) {

            mensaje.setValue("Complete todos los campos");
            return;
        }

        ApiClient.MiServicioInmobiliaria servicio =
                ApiClient.getServicio();

        Call<String> call = servicio.login(usuario, clave);

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {

                if (response.isSuccessful()) {

                    String token = response.body();

                    Log.d("TOKEN", token);

                    ApiClient.guardarToken(
                            getApplication(),
                            token
                    );

                    Intent intent = new Intent(
                            getApplication(),
                            MainActivity.class
                    );

                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                    getApplication().startActivity(intent);

                } else {

                    mensaje.setValue("Usuario o clave incorrectos");

                    Log.d("LOGIN_ERROR", response.message());
                    Log.d("LOGIN_ERROR", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                mensaje.setValue("Error de conexión");

                Log.d("LOGIN_ERROR", t.getMessage());
            }
        });
    }

    public void resetearPassword() {

        ApiClient.MiServicioInmobiliaria servicio =
                ApiClient.getServicio();

        Call<Object> call = servicio.resetearPassword();

        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call,
                                   Response<Object> response) {

                if (response.isSuccessful()) {

                    mensaje.setValue(
                            "Contraseña reseteada correctamente"
                    );

                } else {

                    mensaje.setValue(
                            "Error al resetear la contraseña"
                    );
                }
            }

            @Override
            public void onFailure(Call<Object> call,
                                  Throwable t) {

                mensaje.setValue(
                        "Error de conexión"
                );

                Log.d(
                        "RESET_ERROR",
                        t.getMessage()
                );
            }
        });
    }

    public void procesarMovimientoSensor(
            float x,
            float y,
            float z
    ) {

        double aceleracion =
                Math.sqrt(x * x + y * y + z * z)
                        - android.hardware.SensorManager.GRAVITY_EARTH;

        if (aceleracion > 6) {

            long tiempoActual =
                    System.currentTimeMillis();

            if (tiempoActual - ultimoTiempoShake > 3000) {
                contadorSacudidas = 0;
            }

            if (tiempoActual - ultimoTiempoShake > 500) {

                ultimoTiempoShake = tiempoActual;

                contadorSacudidas++;

                if (contadorSacudidas >= 3) {

                    llamarInmobiliaria.setValue(true);

                    contadorSacudidas = 0;
                }
            }
        }
    }
}