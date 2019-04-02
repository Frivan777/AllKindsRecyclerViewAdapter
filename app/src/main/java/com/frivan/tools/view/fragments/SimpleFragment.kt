package com.frivan.tools.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frivan.tools.R

class SimpleFragment : Fragment() {

    companion object {
        fun newInstance() = SimpleFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_fragment, container, false)
    }

}
