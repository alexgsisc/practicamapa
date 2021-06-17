package com.example.practicamaps.maps.presentation.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.practicamaps.R
import com.example.practicamaps.commons.base.BaseFragment
import com.example.practicamaps.databinding.DetailRoutersFragmentBinding
import com.example.practicamaps.maps.data.entity.RoutersModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson

class DetailRoutesUserFragment : BaseFragment<DetailRoutersFragmentBinding>(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var gpsTrack: Polyline
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var resultRouter: RoutersModel


    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DetailRoutersFragmentBinding {
        return DetailRoutersFragmentBinding.inflate(inflater, container, false)
    }

    override fun init() {
        arguments?.let {
            resultRouter =
                Gson().fromJson(it.getString("RESULT_ONCLICK_ROUTERS"), RoutersModel::class.java)
        }

        binding.tvName.text = resultRouter.nameRouter
        binding.tvDate.text = resultRouter.date
        binding.tvDistance.text = "Distancia recorida: ${resultRouter.distance}"

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    companion object {
        const val TAG = "DetailRoutesUserFragment"
        fun newInstance() = DetailRoutesUserFragment()
    }

    override fun onMapReady(maps: GoogleMap) {
        mMap = maps
        setCameraPosition(
            LatLng(
                resultRouter.trackRouter[0].latitude,
                resultRouter.trackRouter[0].longitud
            )
        )
        paintPoint()
        createPolyline()

    }

    private fun paintPoint() {

        if (!resultRouter.trackRouter.isNullOrEmpty()) {
            val star = LatLng(
                resultRouter.trackRouter[0].latitude,
                resultRouter.trackRouter[0].longitud
            )
            val endLocation = LatLng(
                resultRouter.trackRouter[resultRouter.trackRouter.size - 1].latitude,
                resultRouter.trackRouter[resultRouter.trackRouter.size - 1].longitud
            )
            mMap.addMarker(
                MarkerOptions().position(star).title("Star")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            mMap.addMarker(MarkerOptions().position(endLocation).title("End"))
        }
    }

    private fun createPolyline() {
        polylineOptions = PolylineOptions()
        polylineOptions.color(Color.RED)
        polylineOptions.width(10f)

        resultRouter.trackRouter.forEach {
            polylineOptions.add(LatLng(it.latitude, it.longitud))
        }
        gpsTrack = mMap.addPolyline(polylineOptions)

    }

    private fun setCameraPosition(latLngFirst: LatLng) {
        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    latLngFirst,
                    23f,
                    10f,
                    180f
                )
            )
        )
    }
}