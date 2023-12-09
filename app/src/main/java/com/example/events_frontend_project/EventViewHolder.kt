/*
    EventViewHolder.kt

    Cette classe, EventViewHolder, est utilisée pour représenter la vue d'un élément dans un RecyclerView.
    Elle contient des références aux différents composants de la vue de ligne (row_event) qui affiche les détails d'un événement.
*/

package com.example.events_frontend_project

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventViewHolder (rootView: View) : RecyclerView.ViewHolder(rootView) {


    var address = rootView.findViewById<TextView>(R.id.row_event_address)
    var description = rootView.findViewById<TextView>(R.id.row_event_description)
    var title = rootView.findViewById<TextView>(R.id.row_event_title)
    var imageURL = rootView.findViewById<ImageView>(R.id.row_event_imgview)
    var btn = rootView.findViewById<ImageButton>(R.id.row_event_imgbtn)
    var fav = rootView.findViewById<CheckBox>(R.id.row_event_checkbox)


}