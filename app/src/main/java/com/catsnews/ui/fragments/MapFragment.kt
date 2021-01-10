package com.catsnews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catsnews.catnews.R
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(R.layout.fragment_map) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val map = ArcGISMap(Basemap.Type.TOPOGRAPHIC, 56.464097, 84.979557, 16)
        mapVieww.map = map
    }


}