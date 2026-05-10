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

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensaje() {

        if (mensaje == null) {
            mensaje = new MutableLiveData<>();
        }

        return mensaje;
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
}