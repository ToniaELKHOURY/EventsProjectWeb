/*
    FavoriteFragment.kt

    Ce fragment, FavoriteFragment, est utilisé pour afficher une liste d'événements favoris dans une interface utilisateur.
*/

package com.example.events_frontend_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FavoriteFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var rcvEvent: RecyclerView
    private var event: ArrayList<Event> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            event = it.getSerializable(EVENT) as ArrayList<Event>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        eventAdapter = EventAdapter(event)

        rcvEvent = rootView.findViewById(R.id.rcv_favorite)
        rcvEvent.adapter = eventAdapter

        val linearLayoutManager = LinearLayoutManager(context)
        rcvEvent.layoutManager = linearLayoutManager

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(data: ArrayList<Event>) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EVENT, data)
                }
            }
    }
}