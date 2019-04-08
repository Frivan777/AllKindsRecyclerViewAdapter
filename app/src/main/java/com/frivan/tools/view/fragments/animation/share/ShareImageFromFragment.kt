package com.frivan.tools.view.fragments.animation.share

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frivan.tools.R
import kotlinx.android.synthetic.main.fragment_share_image_from.*

class ShareImageFromFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() = ShareImageFromFragment()

    }

    private var callbacks: Callbacks? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_image_from, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Callbacks) {
            callbacks = context
        } else {
            throw RuntimeException("$context must implement Callbacks")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initView()
    }

    override fun onDetach() {
        super.onDetach()

        callbacks = null
    }

    private fun initView() {
        this.image.setOnClickListener {
            this.callbacks?.onClick(it)
        }
    }

    interface Callbacks {

        fun onClick(view: View)

    }

}
