/*
    Event.kt

    Ce fichier définit une classe de données (data class) nommée "Event" qui représente un événement.
    Chaque instance de cette classe contient des informations telles que l'identifiant, le titre,
    la description, l'URL de l'événement, l'URL de l'image, les mots-clés, les horaires, la latitude,
    la longitude, l'adresse, la ville, le département, la région, le pays, le mode de participation,
    le numéro de téléphone, l'e-mail, le site web et un indicateur "favori".

    La classe implémente l'interface Serializable pour permettre la sérialisation des objets.
*/
package com.example.events_frontend_project

import java.io.Serializable

data class Event(
     val id: String,
     val title: String,
     val description: String,
     val longdescription: String,
     val eventurl: String,
     val imageurl: String,
     val keywords: Array<String>,
     val timings: String,
     val latitude: String,
     val longitude: String,
     val address: String,
     val city: String,
     val department: String,
     val region: String,
     val country: String,
     val attendancemode: String,
     val phoneNumber: String,
     val email: String,
     val website: String,
     val favorite : Boolean): Serializable




