package com.example.burak.orderlist.Pages

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.JsonReader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.burak.orderlist.Items.Profile_RvAdapter
import com.example.burak.orderlist.R
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Profile : Fragment(), View.OnClickListener{

    companion object {
        val rvAdapter = Profile_RvAdapter(("").map {""}.toMutableList())
        val serverurl = "http://192.168.56.1:8080/api/user/52"
        var counter = 0;
        var profile_name:TextView ?= null
        var profile_email:TextView ?= null
        var your_address:TextView ?= null
        var name:String = ""
        var address:String = ""
        var email:String = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.page_profile, container, false)
        val profilepicture = rootView.findViewById<ImageView>(R.id.profile_picture)
        val recyclerview = rootView.findViewById(R.id.recyclerAddress) as RecyclerView
        val addAddress = rootView.findViewById<TextView>(R.id.addAddress)
        profile_name = rootView.findViewById<TextView>(R.id.profile_name)
        your_address = rootView.findViewById<TextView>(R.id.your_address)
        profile_email = rootView.findViewById<TextView>(R.id.profile_email)

        //recyclerview.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = rvAdapter

        val img = BitmapFactory.decodeResource(resources, R.drawable.dummy_user_image)
        val round = RoundedBitmapDrawableFactory.create(resources, img)

        round.isCircular = true
        profilepicture.setImageDrawable(round)
        addAddress.setOnClickListener(this)

        if(counter == 0) ServerCommunication().execute(serverurl)

        counter++

        profile_name?.text= name
        profile_email?.text = email
        your_address?.text = address

        return rootView
    }
    override fun onClick(v: View) {
        when (v.id) {
        }
    }

    inner class ServerCommunication : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            // Before doInBackground
        }

        override fun doInBackground(vararg urls: String?): String? {

            var connection: HttpURLConnection? = null
            try {
                val url = URL(urls[0])

                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                var inString = translate(connection.inputStream)
                publishProgress(inString)
                connection.disconnect()
            } catch (ex: Exception) {
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
            }

            return " "
        }
    }

    fun translate(inputStream: InputStream): String {
        val reader = JsonReader(InputStreamReader(inputStream));
        var id:Long



            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> id = reader.nextLong()
                    "name" -> name = reader.nextString()
                    "address" -> address = reader.nextString()
                    "email" -> email = reader.nextString()
                    else -> reader.skipValue()
                }
                println("Burda2 => " + name)
            }
            println("Burda => " + name)
            //rvAdapter.addItem( address)
            your_address?.text =address
            profile_name?.text= name
            profile_email?.text = email
            reader.endObject()


        return name
    }
}
