package com.inmobiliaria.ui.perfil.clave;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarClaveViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mClaveCambiada = new MutableLiveData<>();

    public CambiarClaveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getError() { return mError; }
    public LiveData<Boolean> getClaveCambiada() { return mClaveCambiada; }

    public void procesarCambioClave(String actual, String nueva, String repetirNueva) {
        if (actual.isEmpty() || nueva.isEmpty() || repetirNueva.isEmpty()) {
            mError.setValue("Todos los campos son obligatorios.");
            return;
        }

        if (!nueva.equals(repetirNueva)) {
            mError.setValue("La nueva contraseña y su repetición no coinciden.");
            return;
        }

        if (nueva.length() < 4) {
            mError.setValue("La nueva contraseña debe ser más segura.");
            return;
        }

        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        if (token == null) {
            mError.setValue("Sesión no válida.");
            return;
        }

        ApiClient.getServicio().cambiarClave(token, actual, nueva).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mClaveCambiada.setValue(true);
                } else {
                    mError.setValue("Contraseña actual incorrecta o formato inválido.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mError.setValue("Error al conectar con el servidor: " + t.getMessage());
            }
        });
    }
}