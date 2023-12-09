/*
    EventAdapter.kt

    Cet adaptateur (EventAdapter) est utilisé pour lier les données d'événements à un RecyclerView.
    Chaque élément de la liste d'événements est affiché dans une vue de ligne (row_event) du RecyclerView.
*/

package com.example.events_frontend_project

import retrofit2.Call
import android.content.Intent
//import android.telecom.Call
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response

val EXTRA_KEY = "extra-key"

class EventAdapter(private var eventList : List<Event>) : RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.row_event, parent,
            false
        )
        return EventViewHolder(layout)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]

        holder.address.text = event.address
        holder.title.text = event.title
        holder.description.text = event.description
        holder.fav.isChecked = event.favorite
        holder.fav.setOnCheckedChangeListener { buttonView, isChecked ->
            val Event = eventList.find {
                it.title.lowercase() == holder.title.text.toString().lowercase()
            }
            if (Event != null) {
                eventService.toggleFavorite(Event.id).enqueue(object : Callback<JSONObject> {
                    override fun onResponse(
                        call: Call<JSONObject>,
                        response: Response<JSONObject>
                    ) {
                    }

                    override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                        Toast.makeText(holder.btn.context, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

        val img = Picasso.get().load(event.imageurl).transform(CircleTransform()).resize(300,300).into(holder.imageURL)

        holder.btn.setOnClickListener {
            var allData: Event?

            val Event = eventList.find {
                it.title.lowercase() == holder.title.text.toString().lowercase()
            }
            if (Event != null) {
                eventService.findOneById(Event.id).enqueue(object : Callback<Event> {
                    override fun onResponse(
                        call: Call<Event>,
                        response: Response<Event>
                    ) {
                        allData = response.body()
                        val returnIntent = Intent(holder.btn.context, DetailsActivity::class.java)
                        returnIntent.putExtra(EXTRA_KEY, allData)
                        holder.btn.context.startActivity(returnIntent)
                    }

                    override fun onFailure(call: Call<Event>, t: Throwable) {
                        Toast.makeText(holder.btn.context, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }


    }

    override fun getItemCount(): Int {
        return eventList.size
    }

}