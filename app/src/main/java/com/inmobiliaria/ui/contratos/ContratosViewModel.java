package com.inmobiliaria.ui.contratos;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inmobiliaria.model.Contrato;
import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.model.Pago;
import com.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> mInmueblesAlquilados = new MutableLiveData<>();
    private final MutableLiveData<Contrato> mContratoDetalle = new MutableLiveData<>();
    private final MutableLiveData<List<Pago>> mHistorialPagos = new MutableLiveData<>();
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public ContratosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getInmueblesAlquilados() { return mInmueblesAlquilados; }
    public LiveData<Contrato> getContratoDetalle() { return mContratoDetalle; }
    public LiveData<List<Pago>> getHistorialPagos() { return mHistorialPagos; }
    public LiveData<String> getError() { return mError; }

    public void cargarInmueblesAlquilados() {
        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        ApiClient.getServicio().obtenerInmueblesAlquilados(token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mInmueblesAlquilados.setValue(response.body());
                } else {
                    mError.setValue("Error al obtener inmuebles alquilados.");
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                mError.setValue("Error de red: " + t.getMessage());
            }
        });
    }

    public void cargarDetalleContrato(Inmueble inmueble) {
        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        ApiClient.getServicio().obtenerContratoPorInmueble(token, inmueble.getIdInmueble()).enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mContratoDetalle.setValue(response.body());
                } else {
                    mError.setValue("Este inmueble no posee un contrato activo recuperable.");
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                mError.setValue("Error al conectar: " + t.getMessage());
            }
        });
    }

    public void cargarPagosDelContrato(int idContrato) {
        Context context = getApplication().getApplicationContext();
        String token = ApiClient.obtenerToken(context);

        ApiClient.getServicio().obtenerPagosPorContrato(token, idContrato).enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mHistorialPagos.setValue(response.body());
                } else {
                    mError.setValue("No se encontraron pagos registrados para este alquiler.");
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                mError.setValue("Error de conexión al recuperar pagos: " + t.getMessage());
            }
        });
    }
}