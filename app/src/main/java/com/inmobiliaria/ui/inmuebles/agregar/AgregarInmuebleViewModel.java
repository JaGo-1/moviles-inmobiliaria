package com.inmobiliaria.ui.inmuebles.agregar;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Uri> imagen;
    private MutableLiveData<String> mensaje;

    public AgregarInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getImagen() {

        if (imagen == null) {
            imagen = new MutableLiveData<>();
        }

        return imagen;
    }

    public LiveData<String> getMensaje() {

        if (mensaje == null) {
            mensaje = new MutableLiveData<>();
        }

        return mensaje;
    }

    public void recibirImagen(ActivityResult result) {

        if (result.getResultCode() == RESULT_OK) {

            Intent data = result.getData();

            if (data != null) {

                Uri uri = data.getData();

                imagen.setValue(uri);
            }
        }
    }

    public void guardarInmueble(String direccion,
                                String uso,
                                String tipo,
                                String ambientes,
                                String superficie,
                                String valor) {

        try {

            if (direccion.isEmpty() ||
                    uso.isEmpty() ||
                    tipo.isEmpty() ||
                    ambientes.isEmpty() ||
                    superficie.isEmpty() ||
                    valor.isEmpty()) {

                mensaje.setValue("Complete todos los campos");
                return;
            }

            Inmueble inmueble = new Inmueble();

            inmueble.setDireccion(direccion);
            inmueble.setUso(uso);
            inmueble.setTipo(tipo);
            inmueble.setAmbientes(Integer.parseInt(ambientes));
            inmueble.setSuperficie(Integer.parseInt(superficie));
            inmueble.setValor(Double.parseDouble(valor));

            byte[] imagenBytes = transformarImagen();

            if (imagenBytes.length == 0) {

                mensaje.setValue("Debe seleccionar una imagen");
                return;
            }

            String inmuebleJson = new Gson().toJson(inmueble);

            RequestBody inmuebleBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    inmuebleJson
            );

            RequestBody requestFile = RequestBody.create(
                    MediaType.parse("image/jpeg"),
                    imagenBytes
            );

            MultipartBody.Part imagenPart =
                    MultipartBody.Part.createFormData(
                            "imagen",
                            "imagen.jpg",
                            requestFile
                    );

            String token = ApiClient.obtenerToken(
                    getApplication()
            );

            Call<Inmueble> call = ApiClient
                    .getServicio()
                    .cargarInmueble(
                            token,
                            imagenPart,
                            inmuebleBody
                    );

            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call,
                                       Response<Inmueble> response) {

                    if (response.isSuccessful()) {

                        mensaje.setValue(
                                "Inmueble guardado correctamente"
                        );

                    } else {

                        mensaje.setValue(
                                "Error al guardar inmueble"
                        );
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call,
                                      Throwable t) {

                    mensaje.setValue(
                            "Error de conexión"
                    );
                }
            });

        } catch (Exception e) {

            mensaje.setValue(
                    "Datos inválidos"
            );
        }
    }

    private byte[] transformarImagen() {

        try {

            Uri uri = imagen.getValue();

            if (uri == null) {
                return new byte[]{};
            }

            InputStream inputStream = getApplication()
                    .getContentResolver()
                    .openInputStream(uri);

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    baos
            );

            return baos.toByteArray();

        } catch (FileNotFoundException e) {

            return new byte[]{};
        }
    }
}