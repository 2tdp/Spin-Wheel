package com.spin.wheel.chooser.wheeloffortune.custom

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class PageFragment: Fragment() {

    private var layoutRes: Int = -1

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            layoutRes = arguments!!.getInt(KEY_LAYOUT_RESOURCE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }


    companion object {
        private const val KEY_LAYOUT_RESOURCE =  "KEY_LAYOUT_RESOURCE"

        fun newInstance(layoutRes: Int) = PageFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_LAYOUT_RESOURCE, layoutRes)
            }
        }
    }
}