package com.frivan.tools.view.activities.animation.share.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.transition.Explode
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.frivan.tools.R
import com.frivan.tools.view.fragments.animation.share.ShareImageFromFragment
import com.frivan.tools.view.fragments.animation.share.ShareImageToFragment


class ShareImageActivity : AppCompatActivity(), ShareImageFromFragment.Callbacks {

    private val transition: Transition by lazy {
        return@lazy TransitionInflater.from(this@ShareImageActivity).inflateTransition(R.transition.image_transition)
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, ShareImageActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_image)

        this.initView()
    }

    //region ShareImageFromFragment.Callbacks

    override fun onClick(view: View) {
        this.showFragment(ShareImageToFragment.newInstance().apply {
            sharedElementEnterTransition = this@ShareImageActivity.transition
            sharedElementReturnTransition = this@ShareImageActivity.transition
            enterTransition = Slide(Gravity.START).apply {
                addTarget(this@ShareImageActivity.getString(R.string.transition_name_screen))
            }
            returnTransition = Slide(Gravity.END).apply {
                addTarget(this@ShareImageActivity.getString(R.string.transition_name_screen))
            }
        }, view)
    }

    //endregion ShareImageFromFragment.Callbacks

    private fun initView() {
        this.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ShareImageFromFragment.newInstance().apply {
                    exitTransition = Explode().apply {
                        addTarget(R.id.title)
                    }
//                    allowReturnTransitionOverlap = true
                })
                .commit()
    }

    private fun showFragment(fragment: Fragment, view: View) {
        this.supportFragmentManager
                .beginTransaction()
                .addSharedElement(view, view.transitionName)
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, fragment)
                .commit()
    }
}
