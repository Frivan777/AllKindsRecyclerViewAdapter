package com.frivan.tools.view.fragments.animation.share

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frivan.tools.R
import com.frivan.tools.view.activities.animation.share.image.ImageActivity
import kotlinx.android.synthetic.main.fragment_share_image_to.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initView()
    }

    private fun initView() {
        this.image.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                val options = ActivityOptions.makeSceneTransitionAnimation(this.activity,
                        Pair<View, String>(this.image, this.image.transitionName))

                this.context?.let {
                    this.startActivity(ImageActivity.newIntent(it), options.toBundle())
                }

            } else {
                this.context?.let {
                    this.startActivity(ImageActivity.newIntent(it))
                }
            }
        }
    }

}
