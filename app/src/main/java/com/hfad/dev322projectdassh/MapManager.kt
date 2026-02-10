package com.hfad.dev322projectdassh

import android.content.Context
import android.graphics.Color
import android.location.Location
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapManager(private val context: Context) {
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController
    private lateinit var locationOverlay: MyLocationNewOverlay
    private var routePolyline: Polyline? = null
    
    fun initializeMap(mapView: MapView) {
        this.mapView = mapView
        
        // Configure OSMDroid
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        
        // Set up map
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        
        mapController = mapView.controller
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        
        // Configure location overlay
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        locationOverlay.runOnFirstFix {
            centerMapOnLocation()
        }
        
        mapView.overlays.add(locationOverlay)
        
        // Set default zoom and center
        mapController.setZoom(15.0)
    }
    
    fun addLocationToRoute(location: Location) {
        val geoPoint = GeoPoint(location.latitude, location.longitude)
        
        if (routePolyline == null) {
            routePolyline = Polyline()
            routePolyline?.let { polyline ->
                polyline.outlinePaint.color = Color.RED
                polyline.outlinePaint.strokeWidth = 8f
                mapView.overlays.add(polyline)
            }
        }
        
        routePolyline?.addPoint(geoPoint)
        mapView.invalidate()
    }
    
    fun centerMapOnLocation() {
        locationOverlay.myLocation?.let { location ->
            mapController.setCenter(GeoPoint(location.latitude, location.longitude))
        }
    }
    
    fun clearRoute() {
        routePolyline?.let { polyline ->
            mapView.overlays.remove(polyline)
        }
        routePolyline = null
        mapView.invalidate()
    }
    
    fun onResume() {
        mapView.onResume()
    }
    
    fun onPause() {
        mapView.onPause()
    }
}
