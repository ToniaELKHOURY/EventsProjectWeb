/*
    ResponseReturned.kt

    Cette classe, ResponseReturned, représente la réponse retournée par le serveur lors de la récupération des données d'événements.
    Elle encapsule des informations telles que le numéro de page actuel, le numéro de la page suivante, le numéro de la dernière page, le numéro de la première page,
    le nombre de lignes par page et une liste d'objets Event.
*/

package com.example.events_frontend_project

data class ResponseReturned (
    val current : Int,
    val next : Int,
    val last : Int,
    val first : Int,
    val rows : Int,
    val returnData : ArrayList <Event>
): java.io.Serializable
