package com.inmobiliaria.ui.inquilinos;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inmobiliaria.model.Contrato;
import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.model.Inquilino;
import com.inmobiliaria.request.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> mInmueblesAlquilados = new MutableLiveData<>();

    private final MutableLiveData<Inquilino> mInquilinoDetalle = new MutableLiveData<>();

    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getInmueblesAlquilados() {
        return mInmueblesAlquilados;
    }

    public LiveData<Inquilino> getInquilinoDetalle() {
        return mInquilinoDetalle;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void cargarInmueblesAlquilados() {
        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        ApiClient.getServicio().obtenerInmueblesAlquilados(token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(@NonNull Call<List<Inmueble>> call, @NonNull Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mInmueblesAlquilados.setValue(response.body());
                } else {
                    mError.setValue("No se pudieron cargar los inmuebles alquilados.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Inmueble>> call, @NonNull Throwable t) {
                mError.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void cargarInquilinoDelInmueble(Inmueble inmueble) {
        if (inmueble == null) return;

        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        ApiClient.getServicio().obtenerContratoPorInmueble(token, inmueble.getIdInmueble()).enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(@NonNull Call<Contrato> call, @NonNull Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Contrato contrato = response.body();

                    if (contrato.getInquilino() != null) {
                        mInquilinoDetalle.setValue(contrato.getInquilino());
                    } else {
                        mError.setValue("El contrato no tiene un inquilino asociado.");
                    }
                } else {
                    mError.setValue("No se pudo recuperar el detalle del inquilino.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Contrato> call, @NonNull Throwable t) {
                mError.setValue("Error de red: " + t.getMessage());
            }
        });
    }
}