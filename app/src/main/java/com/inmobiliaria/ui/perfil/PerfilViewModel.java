package com.inmobiliaria.ui.perfil;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inmobiliaria.model.Propietario;
import com.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();
    private MutableLiveData<String> mensaje = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getPropietario() {
        return mPropietario;
    }
    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public void obtenerDatos() {
        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        if (token == null) {
            mensaje.setValue("Token no válido o sesión expirada.");
            return;
        }

        ApiClient.getServicio().obtenerPropietario(token).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPropietario.setValue(response.body());
                } else {
                    mensaje.setValue("Error al obtener los datos del perfil.");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensaje.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void actualizarDatos(Propietario propietarioModificado) {
        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        if (token == null) {
            mensaje.setValue("Token ausente. Inicie sesión nuevamente.");
            return;
        }

        if (propietarioModificado == null) {
            mensaje.setValue("Los datos de actualización no son válidos.");
            return;
        }

        ApiClient.getServicio().actualizarPropietario(token, propietarioModificado).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPropietario.setValue(response.body());
                    mensaje.setValue("Perfil actualizado correctamente.");
                } else {
                    mensaje.setValue("Hubo un problema al actualizar los datos.");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensaje.setValue("Error de red: " + t.getMessage());
            }
        });
    }
}
