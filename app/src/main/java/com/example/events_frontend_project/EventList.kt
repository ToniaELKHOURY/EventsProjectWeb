/*
    EventList.kt

    Cette classe, EventList, gère une liste d'événements en utilisant une structure de données
    HashMap pour stocker les événements.

    Méthodes :
    - addEvent(event: Event): Ajoute un nouvel événement à la liste.
    - getAllEvents(): Renvoie une ArrayList d'événements triée par titre.
    - clean(): Efface tous les événements de la liste.
*/

package com.example.events_frontend_project

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class EventList {

    private val storage : HashMap<String, Event> = HashMap()

    fun addEvent(event: Event) {
        storage[event.title] = event
    }

    fun getAllEvents(): ArrayList<Event> {
        return  ArrayList(storage.values
            .sortedBy { it.title })
    }

    fun clean() {
        storage.clear()
    }
}