package com.frivan.tools.view.fragments.animation.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frivan.tools.R

class ShareImageToFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() = ShareImageToFragment()

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_image_to, container, false)
    }

}
