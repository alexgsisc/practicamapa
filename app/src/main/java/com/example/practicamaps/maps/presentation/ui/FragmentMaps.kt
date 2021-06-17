package com.example.practicamaps.maps.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.location.Location
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.practicamaps.MainActivity
import com.example.practicamaps.R
import com.example.practicamaps.commons.base.BaseFragment
import com.example.practicamaps.databinding.MapsFragmentBinding
import com.example.practicamaps.maps.data.entity.LocationTemp
import com.example.practicamaps.maps.data.entity.RoutersModel
import com.example.practicamaps.maps.data.repository.RoutesMapsRepoImp
import com.example.practicamaps.maps.domain.usecase.LocationUpdateUse
import com.example.practicamaps.maps.domain.usecase.RoutersMapsUseImp
import com.example.practicamaps.maps.presentation.viewmodel.RoutersMapsViewModel
import com.example.practicamaps.maps.presentation.viewmodel.RoutersMapsViewModelFactory
import com.example.practicamaps.maps.presentation.viewmodel.UpdateLocationViewModel
import com.example.practicamaps.maps.presentation.viewmodel.UpdateLocationViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class FragmentMaps : BaseFragment<MapsFragmentBinding>(), OnMapReadyCallback {

    private val viewModelRouters: RoutersMapsViewModel by viewModels {
        RoutersMapsViewModelFactory(
            RoutersMapsUseImp(
                RoutesMapsRepoImp(
                    MainActivity.dataBase!!.routerDao()
                )
            )
        )
    }

    lateinit var viewModelUpdateLocation: UpdateLocationViewModel


    private lateinit var mMap: GoogleMap
    private var isLocationPermissionGranted = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var latLngFirst: LatLng

    private lateinit var gpsTrack: Polyline
    private lateinit var polylineOptions: PolylineOptions
    private var distanceRouter = 0.0
    private var lisLocation: MutableList<Location> = arrayListOf()


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> {
                    isLocationPermissionGranted = true
                    enableLocation()
                }
                else -> {
                    enableLocation()
                }
            }
        }


    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MapsFragmentBinding {
        return MapsFragmentBinding.inflate(inflater, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableLocation()
    }


    @SuppressLint("MissingPermission")
    fun enableLocation() {
        if (!::mMap.isInitialized) return
        if (isLocationPermissionGranted) {
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this.requireActivity())


            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    latLngFirst = LatLng(it.latitude, it.longitude)
                    setCameraPosition()
                }
            }

            mMap.isMyLocationEnabled = true

            starActionButton()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    private fun setCameraPosition() {
        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    latLngFirst,
                    25f,
                    0f,
                    0f
                )
            )
        )
    }


    @SuppressLint("MissingPermission")
    private fun starActionButton() {
        binding.btnStartRecoding.setOnClickListener {
            //onStartRouteMap()
            getLocationViewModel()
            binding.btnStopRecoding.visibility = View.VISIBLE
            binding.btnStartRecoding.visibility = View.GONE
            polylineOptions = PolylineOptions()
            polylineOptions.color(Color.RED)
            polylineOptions.width(7f)
            gpsTrack = mMap.addPolyline(polylineOptions)


        }

        binding.btnStopRecoding.setOnClickListener {
            stopLocation()
            binding.btnStopRecoding.visibility = View.GONE
            binding.btnStartRecoding.visibility = View.VISIBLE
            lisLocation.clear()
            val df = DecimalFormat("#.00")
            var listPoint: MutableList<LocationTemp> = arrayListOf()
            gpsTrack.points.forEach {
                listPoint.add(LocationTemp(it.latitude, it.longitude))
            }
            saveData(distance = "${df.format(distanceRouter.div(1000))} Km", listPoint)

            distanceRouter = 0.0

            mMap.clear()
        }


    }

    private fun saveData(distance: String, listPoint: List<LocationTemp>) {
        val date: Date = Calendar.getInstance().time
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("Guardar recorido")

        val input = EditText(this.requireContext())
        input.hint = "Nombre de viaje"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Guardar", DialogInterface.OnClickListener { dialog, _ ->
            var stringName = input.text.toString()

            if (!stringName.isNullOrEmpty()) {
                val routersMaps = RoutersModel(
                    nameRouter = stringName,
                    date = formatter.format(date),
                    distance = distance,
                    trackRouter = listPoint
                )
                viewModelRouters.insert(routersMaps)
                dialog.dismiss()
            }

        })
        builder.show()

    }


    private fun updateTrack(location: Location) {
        location.bearing
        var pointsTemp: MutableList<LatLng> = gpsTrack.points
        pointsTemp.add(LatLng(location.latitude, location.longitude))
        if (lisLocation.size > 0) {
            distanceRouter += (lisLocation[lisLocation.size - 1].distanceTo(location))
        }
        lisLocation.add(location)
        gpsTrack.points = pointsTemp
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
        const val TAG = "FragmentMaps"
        fun newInstance() = FragmentMaps()

    }


    private fun getLocationViewModel() {
        viewModelUpdateLocation = ViewModelProvider(
            this,
            UpdateLocationViewModelFactory(LocationUpdateUse(fusedLocationClient))
        ).get(UpdateLocationViewModel::class.java)

        viewModelUpdateLocation.monitorUserLocation()

        viewModelUpdateLocation.responseLocation.observe(this, androidx.lifecycle.Observer {
            updateTrack(it)
        })
    }

    private fun stopLocation() {
        if (!::viewModelUpdateLocation.isInitialized) return
        viewModelUpdateLocation.stopMonitorLocation()
    }
}