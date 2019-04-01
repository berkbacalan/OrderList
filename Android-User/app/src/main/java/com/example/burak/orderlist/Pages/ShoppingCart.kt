package com.example.burak.orderlist.Pages

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.burak.orderlist.Items.ShoppingCart_RvAdapter
import com.example.burak.orderlist.Items.ShoppingCart_SwipeToDeleteCallback
import com.example.burak.orderlist.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ShoppingCart : Fragment(), View.OnClickListener{

    companion object {
        val rvAdapter = ShoppingCart_RvAdapter(("").map {""}.toMutableList())
        val serverurl = "http://192.168.56.1:8080/api/order"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.page_shoppingcart, container, false)
        val orderItem = rootView.findViewById(R.id.orderItem) as TextView
        val recyclerview = rootView.findViewById(R.id.recyclerView) as RecyclerView

        orderItem.setOnClickListener(this)

        recyclerview.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = rvAdapter

        val swipeHandler = object : ShoppingCart_SwipeToDeleteCallback(activity!!.applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerview.adapter as ShoppingCart_RvAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerview)



        return rootView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.orderItem -> {
                ServerCommunication().execute(serverurl)
            }
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
                connection.requestMethod = "POST"

                val post = DataOutputStream(connection.outputStream)

                val ja = JSONArray()

                val json = JSONObject()
                json.put("name", "name")
                json.put("address", "address")
                json.put("price", 26.1)
                json.put("id", null)

                ja.put(json)


                //post.writeBytes("{\"address\": \"address\",\"name\": \"name\",\"price\": 26.0}")

                println(ja.toString())
                post.writeBytes(ja.toString())
                post.flush()
                post.close()

                var inString = streamToString(connection.inputStream)
                publishProgress(inString)

            } catch (ex: Exception) {
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
            }

            return " "
        }

        fun streamToString(inputStream: InputStream): String {
            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            var line: String
            var result = ""

            try {
                do {
                    line = bufferReader.readLine()
                    if (line != null) {
                    }
                } while (line != null)
                inputStream.close()
            } catch (ex: Exception) {

            }

            return ""
        }
    }
}