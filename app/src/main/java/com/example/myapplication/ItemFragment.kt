package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.FrameLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.dummy.DummyContent
import com.google.android.material.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import kotlin.properties.Delegates

class ItemFragment : Fragment() {

    enum class State {
        EXPAND, COLLAPSE,
    }

    var currentState: State = State.COLLAPSE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        with(view.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(DummyContent.ITEMS)
        }
        view.arrow.setImageResource(R.drawable.ic_arrow_left)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnNextLayout {
            hideRightSideNoAnimation(view)
        }
        view.left_pane.setOnClickListener {
            when (currentState) {
                State.COLLAPSE -> {
                    currentState = State.EXPAND
                    slideIn {
                        view.arrow.setImageResource(R.drawable.ic_arrow_right)
                    }
                }
                State.EXPAND -> {
                    currentState = State.COLLAPSE
                    slideOut {
                        view.arrow.setImageResource(R.drawable.ic_arrow_left)
                    }
                }
            }
        }
    }

    private fun hideRightSideNoAnimation(view: View) {
        val params = (view as ViewGroup).layoutParams
        val leftPadding = view.marginLeft
        val width = params.width + leftPadding
        val leftSidePane = view.findViewById<FrameLayout>(R.id.left_pane)
        val leftSidePaneWidth = (leftSidePane as ViewGroup).layoutParams.width
        view.layout(
            view.left + width - leftSidePaneWidth,
            view.top,
            view.right + width - leftSidePaneWidth,
            view.bottom
        )
        val slideWidth = view.width - leftSidePaneWidth
        initAnimationParam(slideWidth, view)
    }


    private var width by Delegates.notNull<Int>()
    private var currentAnimator: ViewPropertyAnimator? = null
    private lateinit var _child: View

    private fun initAnimationParam(width: Int, child: View) {
        this.width = width
        this._child = child
    }

    private fun slideIn(onAnimationStart: () -> Unit) {
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

    private fun slideOut(onAnimationStart: () -> Unit) {
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
