package com.inmobiliaria.ui.inicio;

import static org.maplibre.android.style.layers.PropertyFactory.circleColor;
import static org.maplibre.android.style.layers.PropertyFactory.circleRadius;
import static org.maplibre.android.style.layers.PropertyFactory.circleStrokeColor;
import static org.maplibre.android.style.layers.PropertyFactory.circleStrokeWidth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;
import org.maplibre.android.style.layers.CircleLayer;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.Point;

public class InicioViewModel extends AndroidViewModel {

    private final MutableLiveData<ManejadorMapa> mapaActual = new MutableLiveData<>();
    private final LatLng ubicacionInmobiliaria = new LatLng(-33.295011, -66.335633);
    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ManejadorMapa> getMapActual() {
        return mapaActual;
    }

    public void cargarMapa() {
        mapaActual.setValue(new ManejadorMapa());
    }

    public class ManejadorMapa implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull MapLibreMap mapLibreMap) {
            String estiloUrl = "https://tiles.openfreemap.org/styles/positron";

            mapLibreMap.setStyle(new Style.Builder().fromUri(estiloUrl), style -> {

                if (style.isFullyLoaded()) {

                    Feature puntoGeografico = Feature.fromGeometry(
                            Point.fromLngLat(ubicacionInmobiliaria.getLongitude(), ubicacionInmobiliaria.getLatitude())
                    );

                    GeoJsonSource fuenteData = style.getSourceAs("fuente_marcador_inmobiliaria");
                    if (fuenteData != null) {
                        fuenteData.setGeoJson(puntoGeografico);
                    } else {
                        style.addSource(new GeoJsonSource("fuente_marcador_inmobiliaria", puntoGeografico));

                        CircleLayer capaPuntoRojo = new CircleLayer("capa_marcador_inmobiliaria", "fuente_marcador_inmobiliaria")
                                .withProperties(
                                        circleRadius(8f),
                                        circleColor("#E74C3C"),
                                        circleStrokeColor("rgba(231, 76, 60, 0.3)"),
                                        circleStrokeWidth(12f)
                                );
                        style.addLayer(capaPuntoRojo);
                    }
                }
            });

            CameraPosition posicionCamara = new CameraPosition.Builder()
                    .target(ubicacionInmobiliaria)
                    .zoom(14.5)
                    .bearing(0)
                    .tilt(0)
                    .build();

            mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(posicionCamara), 2200);
        }
    }
}