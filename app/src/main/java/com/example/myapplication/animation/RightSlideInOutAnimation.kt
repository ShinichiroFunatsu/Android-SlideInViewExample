package com.example.myapplication.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewPropertyAnimator
import com.google.android.material.animation.AnimationUtils
import kotlin.properties.Delegates

class RightSlideInOutAnimation() {

    private var width by Delegates.notNull<Int>()
    private var currentAnimator: ViewPropertyAnimator? = null
    private lateinit var _child: View

    fun initAnimationParam(width: Int, child: View) {
        this.width = width
        this._child = child
    }

    fun slideIn(onAnimationStart: () -> Unit) {
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            _child.clearAnimation()
        }
        animateChildTo(
            _child,
            -width,
            ENTER_ANIMATION_DURATION.toLong(),
            AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR,
            ENTER_START_DELAY.toLong(),
            onAnimationStart = onAnimationStart
        )
    }

    fun slideOut(onAnimationStart: () -> Unit) {
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            _child.clearAnimation()
        }
        animateChildTo(
            _child,
            0,
            EXIT_ANIMATION_DURATION.toLong(),
            AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR,
            onAnimationStart = onAnimationStart
        )
    }

    private fun animateChildTo(
        child: View,
        targetX: Int,
        duration: Long,
        interpolator: TimeInterpolator,
        startDelay: Long = 0,
        onAnimationStart: () -> Unit
    ) {
        currentAnimator = child
            .animate()
            .translationX(targetX.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setStartDelay(startDelay)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        onAnimationStart()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }
                })
    }

    companion object {
        private const val ENTER_START_DELAY = 120
        private const val ENTER_ANIMATION_DURATION = 260
        private const val EXIT_ANIMATION_DURATION = 275
    }

}