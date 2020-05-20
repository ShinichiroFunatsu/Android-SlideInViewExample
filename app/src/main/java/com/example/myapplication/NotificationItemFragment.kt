package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.animation.RightSlideInOutAnimation
import com.example.myapplication.dummy.DummyContent
import kotlinx.android.synthetic.main.fragment_item_list.view.*

// FIXME apply data-binding
class NotificationItemFragment : Fragment(R.layout.fragment_item_list) {

    private val animation = RightSlideInOutAnimation()
    private val viewModel by viewModels<NotificationItemViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        observeViewModel(view, viewModel)
        view.doOnNextLayout {
            initViewPositionAndAnimation(view)
        }
    }


    private fun initView(view: View) {
        with(view.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(DummyContent.ITEMS)
        }
        view.arrow.setImageResource(R.drawable.ic_arrow_left)
        // FIXME apply data-binding
        view.left_pane.setOnClickListener {
            viewModel.toggle()
        }
    }

    private fun observeViewModel(view: View, viewModel: NotificationItemViewModel) {
        with(viewModel) {
            collapse.observe(viewLifecycleOwner) { slideOut(view) }
            expand.observe(viewLifecycleOwner) { slideIn(view) }
        }
    }

    private fun initViewPositionAndAnimation(view: View) {
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

    private fun slideIn(view: View) {
        animation.slideIn {
            view.arrow.setImageResource(R.drawable.ic_arrow_right)
        }
    }

    private fun slideOut(view: View) {
        animation.slideOut {
            view.arrow.setImageResource(R.drawable.ic_arrow_left)
        }
    }

}


