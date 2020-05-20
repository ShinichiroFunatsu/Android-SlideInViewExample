package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.animation.RightSlideInOutAnimation
import com.example.myapplication.dummy.DummyContent
import kotlinx.android.synthetic.main.fragment_item_list.view.*

class NotificationItemFragment : Fragment() {

    enum class State {
        EXPAND, COLLAPSE,
    }

    private val animation = RightSlideInOutAnimation()
    private var currentState: State = State.COLLAPSE

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
            initViewAndAnimation(view)
        }
        view.left_pane.setOnClickListener {
            when (currentState) {
                State.COLLAPSE -> {
                    currentState = State.EXPAND
                    animation.slideIn {
                        view.arrow.setImageResource(R.drawable.ic_arrow_right)
                    }
                }
                State.EXPAND -> {
                    currentState = State.COLLAPSE
                    animation.slideOut {
                        view.arrow.setImageResource(R.drawable.ic_arrow_left)
                    }
                }
            }
        }
    }

    private fun initViewAndAnimation(view: View) {
        hidRightSidePaneWithNoAnimation(view)
        val rightSidePaneWidth = view.findViewById<RecyclerView>(R.id.list).width
        animation.initAnimationParam(rightSidePaneWidth, view)
    }

    private fun hidRightSidePaneWithNoAnimation(view: View) {
        val rightSidePaneWidth = view.findViewById<RecyclerView>(R.id.list).width
        view.layout(
            view.left + rightSidePaneWidth,
            view.top,
            view.right + rightSidePaneWidth,
            view.bottom
        )
    }

}


