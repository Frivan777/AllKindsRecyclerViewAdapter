package com.frivan.tools.view.activities.animation.share.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.toBitmap
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.frivan.tools.R
import com.frivan.tools.view.base.transition.android.TransitionListener
import kotlinx.android.synthetic.main.activity_detail_contact_scene_1.*

class DetailContactActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_NAME = "name"

        private const val EXTRA_IMAGE = "image"

        private const val FADE_ANIMATION_DURATION = 100L

        fun newIntent(context: Context, name: String, image: Drawable): Intent {
            return Intent(context, DetailContactActivity::class.java).apply {
                putExtra(EXTRA_NAME, name)
                putExtra(EXTRA_IMAGE, image.toBitmap())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        this.setupTransition()

        setContentView(R.layout.activity_detail_contact_scene_1)

        this.initView()
    }

    private fun initView() {
        this.name.text = this.intent.getStringExtra(EXTRA_NAME)
        this.icon.setImageBitmap(this.intent.getParcelableExtra(EXTRA_IMAGE))

//        this.supportPostponeEnterTransition()
//        this.name.postDelayed({
//            this.supportStartPostponedEnterTransition()
//        }, 2000)


        this.window.sharedElementEnterTransition.addListener(object : TransitionListener() {

            override fun onTransitionEnd(transition: Transition) {
                super.onTransitionEnd(transition)

                this@DetailContactActivity.showViews()
//
//                val scene =
//                    Scene.getSceneForLayout(root, R.layout.activity_detail_contact_scene_1, this@DetailContactActivity)
//                scene.enter()
//
//                this@DetailContactActivity.showScene(R.layout.activity_detail_contact_scene_2,
//                    Explode().apply {
//                        excludeTarget(R.id.view, true)
//                    }, this@DetailContactActivity.root
//                )
            }
        })
    }

    private fun setupTransition() {
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

            enterTransition = android.transition.Fade(Fade.IN).apply {
                duration = FADE_ANIMATION_DURATION
                addTarget(R.id.emailValue)
            }
        }
    }

    private fun showViews() {
        val set = ConstraintSet().apply {
            clone(this@DetailContactActivity, R.layout.activity_detail_contact_scene_2)
        }

        TransitionManager.beginDelayedTransition(this.root, TransitionSet().apply {
            duration = FADE_ANIMATION_DURATION
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(ChangeBounds())
            addTransition(Fade(Fade.IN))
        }.also {
            it.excludeTarget(R.id.icon, true)
            it.excludeTarget(R.id.name, true)
        })

        set.applyTo(this.root)
    }
}
