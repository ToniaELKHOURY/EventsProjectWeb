/*
    EventFragment.kt

    Ce fragment, EventFragment, est utilisé pour afficher une liste d'événements dans une interface utilisateur.
*/

package com.example.events_frontend_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val EVENT = "event"

class EventFragment:Fragment() {
    private lateinit var eventAdapter: EventAdapter
    private lateinit var rcvEvent: RecyclerView
    private var events: ArrayList<Event> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            events = it.getSerializable(EVENT) as ArrayList<Event>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)

        eventAdapter = EventAdapter(events)
        rcvEvent = view.findViewById(R.id.rcv_event_list_fragment)

        rcvEvent.adapter = eventAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        rcvEvent.layoutManager = linearLayoutManager

        return view
    }
    companion object {
        @JvmStatic
        fun newInstance(events: ArrayList<Event>) =
            EventFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EVENT, events)
                }
            }
    }
}

