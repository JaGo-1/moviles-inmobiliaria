package com.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> listaInmuebles = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getListaInmuebles() {
        return listaInmuebles;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public void obtenerInmuebles() {

        Context context = getApplication().getApplicationContext();

        String token = ApiClient.obtenerToken(context);

        if (token == null || token.isEmpty()) {
            mensaje.setValue("Sesión inválida.");
            return;
        }

        ApiClient.getServicio().obtenerInmuebles(token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    listaInmuebles.setValue(response.body());

                } else {

                    mensaje.setValue("Error al obtener inmuebles.");
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {

                mensaje.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}