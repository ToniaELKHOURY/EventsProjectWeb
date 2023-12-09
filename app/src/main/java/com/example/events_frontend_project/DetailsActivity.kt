/*
    DetailsActivity.kt

    Cette activité, DetailsActivity, affiche les détails d'un événement sélectionné.
    Les détails comprennent des informations telles que l'image de l'événement, le titre,
    l'adresse, le numéro de téléphone, l'e-mail, la description, le site web et la région.

    Notes :
    - L'activité utilise la bibliothèque Picasso pour le chargement et l'affichage des images.
    - Les détails non disponibles sont remplacés par des messages par défaut.

*/

package com.example.events_frontend_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class DetailsActivity : AppCompatActivity() {
    lateinit var data : Event
    lateinit var img: ImageView
    lateinit var title: TextView
    lateinit var address: TextView
    lateinit var number: TextView
    lateinit var email: TextView
    lateinit var website: TextView
    lateinit var region: TextView
    lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        data = getIntent().getSerializableExtra(EXTRA_KEY) as Event

        img = findViewById<ImageView>(R.id.detail_imgView)
        title = findViewById<TextView>(R.id.detail_title)
        address = findViewById<TextView>(R.id.detail_address)
        number = findViewById<TextView>(R.id.detail_number)
        email = findViewById<TextView>(R.id.detail_email)
        description = findViewById<TextView>(R.id.detail_description)
        website = findViewById<TextView>(R.id.detail_website)
        region = findViewById<TextView>(R.id.detail_region)

        Picasso.get().load(data.imageurl).transform(CircleTransform()).resize(300,300).into(img)
        title.setText(data.title).toString()
        address.setText(data.address).toString()
        number.setText(data.phoneNumber).toString()
        email.setText(data.email).toString()
        description.setText(data.description).toString()
        website.setText(data.website).toString()
        region.setText(data.region).toString()


        if(data.title.equals("")){
            title.setText("Title not available".toString())
        }
        if(data.address.equals("")){
            address.setText("Address not available".toString())
        }
        if(data.email.equals("")){
            email.setText("Email not available".toString())
        }
        if(data.phoneNumber.equals("")){
            number.setText("Number not available".toString())
        }
        if(data.description.equals("")){
            description.setText("Description not available".toString())
        }
        if(data.website.equals("")){
            website.setText("Website not available".toString())
        }
        if(data.region.equals("")){
            region.setText("Region not available".toString())
        }

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
