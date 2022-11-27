
package com.example.MemoI

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.memoi.databinding.FragmentMapBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * An activity that displays a map showing the place at the device's current location.
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mView: MapView
    lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater,container,false)
/*
        val map = childFragmentManager.findFragmentById(binding.mapView.id)

        var rootView = inflater.inflate(binding.mapView, container, false)
        mView = rootView.mapView

        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)
*/
        return binding.root
    }



    override fun onMapReady(googleMap: GoogleMap) {
        val myLocation = LatLng(37.654601, 127.060530)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))

        //마커 출력
        val marker = MarkerOptions()
            .position(myLocation)
            .title("Nowon")
            .snippet("노원역입니다.")
        googleMap?.addMarker(marker)
    }


    override fun onStart() {
        super.onStart()
        mView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }



}

