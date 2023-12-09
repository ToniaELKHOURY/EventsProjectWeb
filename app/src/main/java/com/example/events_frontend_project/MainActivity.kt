/*
    MainActivity.kt

    Cette classe, MainActivity, représente l'activité principale de l'application. Elle gère l'affichage
    des fragments, la navigation entre les onglets, la récupération des données depuis le serveur, et la gestion
    de la pagination.
*/

package com.example.events_frontend_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var eventService: EventService

private const val SERVER_BASE_URL = "https://events-aej-tek.cleverapps.io/"


class MainActivity : AppCompatActivity() {

    lateinit var next_btn: Button
    lateinit var back_btn: Button

    var menu_item_search: MenuItem? = null
    var menu_item_reload: MenuItem? = null

    lateinit var first: TextView
    lateinit var current: TextView
    lateinit var last: TextView

    var tabLayout: TabLayout? = null

    var sortBy: String = "website"
    private var dropDownLabels = arrayOf( "title", "address", "website")
    var counter: Int = 0;
    private val eventList = EventList()

    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SERVER_BASE_URL)
            .build()

        eventService = retrofit.create(EventService::class.java)

        initTabs()
        initButtons()
        initPaginationView()
        readDataFromServer(counter, sortBy,0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var m: Menu? = initMenu(menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                readDataFromServer(counter,sortBy,1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initTabs(){
        tabLayout = findViewById<TabLayout>(R.id.a_main_tabs)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // creating cases for fragment
                when (tab.position) {
                    0 -> readDataFromServer(counter,sortBy,0)
                    1 -> readMapInfoFromServer()
                    2 -> readFavFromServer()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun initButtons(){
        next_btn = findViewById<Button>(R.id.button_next)
        back_btn = findViewById<Button>(R.id.button_back)
        back_btn.isEnabled = false
        next_btn.setOnClickListener{
            counter++
            back_btn.isEnabled = true
            readDataFromServer(counter,sortBy,0)
        }
        back_btn.setOnClickListener{
            counter--
            back_btn.isEnabled = counter != 0
            readDataFromServer(counter,sortBy, 0)
        }
    }

    private fun initPaginationView(){
        first = findViewById<TextView>(R.id.first_page)
        current = findViewById<TextView>(R.id.current_page)
        last = findViewById<TextView>(R.id.last_page)
    }

    private fun initMenu(menu: Menu?): Menu?{
        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            menu_item_search = menu.getItem(0)
            menu_item_reload = menu.getItem(1)
        };

        return menu
    }

    private fun readDataFromServer(counter: Int, sortby: String, refresh: Int){
        menu_item_search?.let{it.isVisible = true}
        menu_item_reload?.let{it.isVisible = true}
        var allData: ResponseReturned?
        eventList.clean()
        eventService.findAll(counter,10,0,sortby,0,0,refresh).enqueue(object : Callback<ResponseReturned> {
            override fun onResponse(
                call: Call<ResponseReturned>,
                response: Response<ResponseReturned>
            ) {
                allData = response.body()
                allData?.returnData?.forEach{eventList.addEvent(it)}
                displayEventListFragment()
                allData?.let { first.setText(it.first.toString() + " .. ") }
                allData?.let { current.setText(" .. " + it.current.toString() +" .. ") }
                allData?.let { last.setText(" .. " +it.last.toString())}
            }
            override fun onFailure(call: Call<ResponseReturned>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun readFavFromServer(){
        menu_item_search?.let{it.isVisible = false}
        menu_item_reload?.let{it.isVisible = false}

        var allData: ResponseReturned?
        eventList.clean()
        eventService.findAll(0,10000,0,"title",1,0,0).enqueue(object : Callback<ResponseReturned> {
            override fun onResponse(
                call: Call<ResponseReturned>,
                response: Response<ResponseReturned>
            ) {
                allData = response.body()
                allData?.returnData?.let { displayFavFragment(it) }
            }
            override fun onFailure(call: Call<ResponseReturned>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun readMapInfoFromServer(){
        menu_item_search?.let{it.isVisible = false}
        menu_item_reload?.let{it.isVisible = false}
        var allMapsInfo: List<MapsInfo>? = listOf()
        eventService.findMapsInfo(0,10000,0,"title",0,1).enqueue(object : Callback<List<MapsInfo>> {
            override fun onResponse(
                call: Call<List<MapsInfo>>,
                response: Response<List<MapsInfo>>
            ) {
                allMapsInfo = response.body()
                allMapsInfo?.let { displayMapFragment(it) }
            }
            override fun onFailure(call: Call<List<MapsInfo>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayEventListFragment() {
        val event = EventFragment.newInstance(eventList.getAllEvents())
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, event)
            .commit()

        setPaginationViewState(View.VISIBLE)
    }

    private fun displayMapFragment(allMapsInfo:List<MapsInfo>) {
        val mapFragment = MapsFragment.newInstance(ArrayList(allMapsInfo))
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout, mapFragment)
            .commit()

        setPaginationViewState(View.GONE)
    }
    private fun displayFavFragment(data: ArrayList<Event>) {
        val favFragment = FavoriteFragment.newInstance(data)
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_frame_layout,favFragment)
            .commit()

        setPaginationViewState(View.GONE)
    }

    fun setPaginationViewState(state: Int){
        back_btn.visibility = state
        next_btn.visibility = state
        first.visibility = state
        current.visibility = state
        last.visibility = state
    }
}