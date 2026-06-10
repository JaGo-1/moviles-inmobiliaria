package com.inmobiliaria.ui.inmuebles.detalle;

import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> inmuebleMutable;
    private MutableLiveData<String> mensajeMutable;

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getInmuebleMutable() {
        if (inmuebleMutable == null) {
            inmuebleMutable = new MutableLiveData<>();
        }
        return inmuebleMutable;
    }

    public LiveData<String> getMensajeMutable() {
        if (mensajeMutable == null) {
            mensajeMutable = new MutableLiveData<>();
        }
        return mensajeMutable;
    }

    public void recuperarDatos(Bundle bundle) {
        if (bundle != null) {
            Inmueble inmueble = (Inmueble) bundle.getSerializable("inmueble");
            if (inmueble != null) {
                inmuebleMutable = (MutableLiveData<Inmueble>) getInmuebleMutable();
                inmuebleMutable.setValue(inmueble);
            }
        }
    }

    public void actualizarEstado(boolean nuevoEstado) {
        Inmueble inmuebleActual = getInmuebleMutable().getValue();

        if (inmuebleActual != null) {
            inmuebleActual.setDisponible(nuevoEstado);

            String token = ApiClient.obtenerToken(getApplication());
            ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio();

            Call<Inmueble> call = api.actualizarInmueble(token, inmuebleActual);
            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if (response.isSuccessful()) {
                        mensajeMutable = (MutableLiveData<String>) getMensajeMutable();
                        mensajeMutable.setValue("Estado actualizado");
                    } else {
                        mensajeMutable = (MutableLiveData<String>) getMensajeMutable();
                        mensajeMutable.setValue("Error al actualizar");
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    mensajeMutable = (MutableLiveData<String>) getMensajeMutable();
                    mensajeMutable.setValue("Error de conexión");
                }
            });
        }
    }

    public void clearMensaje() {
        if (mensajeMutable != null) {
            mensajeMutable.setValue(null);
        }
    }
}
