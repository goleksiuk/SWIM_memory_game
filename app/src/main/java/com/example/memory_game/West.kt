package com.example.memory_game

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_west.*

class West : Fragment() {

    private var sharedViewModel: SharedViewModel? = null
    private var arrayList: ArrayList<Int>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_west, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)

        arrayList = arguments?.getIntegerArrayList("array")

        updateArray()

        west_IV1.setOnClickListener {
            onClick(west_IV1, 1)
        }
        west_IV2.setOnClickListener {
            onClick(west_IV2, 2)
        }
        west_IV3.setOnClickListener {
            onClick(west_IV3, 3)
        }
        west_IV4.setOnClickListener {
            onClick(west_IV4, 4)
        }
        west_IV5.setOnClickListener {
            onClick(west_IV5, 5)
        }
        west_IV6.setOnClickListener {
            onClick(west_IV6, 6)
        }
    }

    private fun onClick(imageView: ImageView, position: Int) {
        imageView.isClickable = false
        imageView.setImageResource(arrayList!![position-1])
        sharedViewModel?.setId(arrayList!![position-1])
        updateArray()
    }

    private fun updateArray() {
        val guessedArray = sharedViewModel?.getGuessedArray()

        for (i in 1..arrayList!!.size) {
            val id = this.resources.getIdentifier("west_IV$i", "id", activity?.packageName)
            val imageView: ImageView = view!!.findViewById(id)

            if (guessedArray!!.contains(arrayList!![i-1])) {
                imageView.setImageResource(arrayList!![i-1])
                imageView.isClickable = false
            } else {
                if (arrayList!![i-1] != sharedViewModel?.getIdOneValue()) {
                    imageView.setImageResource(R.drawable.card)
                    imageView.isClickable = true
                }
            }
        }
    }
}