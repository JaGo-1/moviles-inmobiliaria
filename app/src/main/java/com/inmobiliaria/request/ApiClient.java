package com.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inmobiliaria.model.Inmueble;
import com.inmobiliaria.model.Propietario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class ApiClient {

    public static final String BASE_URL = "https://capacitacion.alwaysdata.net/";

    public static MiServicioInmobiliaria getServicio() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(MiServicioInmobiliaria.class);
    }

    public interface MiServicioInmobiliaria {

        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(
                @Field("Usuario") String usuario,
                @Field("Clave") String clave
        );

        @GET("api/Propietarios")
        Call<Propietario> obtenerPropietario(
                @Header("Authorization") String token
        );

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> cambiarClave(
                @Header("Authorization") String token,
                @Field("currentPassword") String currentPassword,
                @Field("newPassword") String newPassword
        );

        @GET("api/Inmuebles")
        Call<List<Inmueble>> obtenerInmuebles(
                @Header("Authorization") String token
        );

        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(
                @Header("Authorization") String token,
                @Body Inmueble inmueble
        );

        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> cargarInmueble(
                @Header("Authorization") String token,
                @Part MultipartBody.Part imagen,
                @Part("inmueble") RequestBody inmueble
        );
    }

    public static void guardarToken(Context context, String token) {

        SharedPreferences sp = context.getSharedPreferences(
                "token.xml",
                Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("token", "Bearer " + token);

        editor.apply();
    }

    public static String obtenerToken(Context context) {

        SharedPreferences sp = context.getSharedPreferences(
                "token.xml",
                Context.MODE_PRIVATE
        );

        return sp.getString("token", null);
    }
}