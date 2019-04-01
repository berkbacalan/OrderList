package com.example.burak.orderlist.Pages

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.burak.orderlist.R
import kotlinx.android.synthetic.main.page_order.*
import android.support.v4.os.HandlerCompat.postDelayed
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.JsonReader
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.burak.orderlist.Items.Order_RvAdapter
import com.example.burak.orderlist.MainActivity
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class Order : Fragment(), View.OnClickListener, TextView.OnEditorActionListener{

    companion object {
        var searchtext : String = ""
        var rvAdapter = Order_RvAdapter(("").map {""}.toMutableList())
        val serverurl = "http://192.168.56.1:8080/api/product"
        var counter = 0;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.page_order, container, false)
        val editlayout = rootView.findViewById<LinearLayout>(R.id.edit_layout)
        val searchbar = editlayout.findViewById<EditText>(R.id.search_bar)
        val closesearch = rootView.findViewById<ImageButton>(R.id.close_search)


        searchbar.isCursorVisible = false
        searchbar.requestFocus()
        searchbar.setText(searchtext)

        if (!searchtext.isEmpty()) searchbar.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        searchbar.setOnClickListener(this)
        searchbar.setOnEditorActionListener(this)
        closesearch.setOnClickListener(this)

        val recyclerview = rootView.findViewById(R.id.recyclerOrder) as RecyclerView

        recyclerview.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = rvAdapter

        if(counter == 0) ServerCommunication().execute(serverurl)

        counter++

        return rootView
    }

    override fun onClick(v: View) {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        when (v.id){
            R.id.search_bar -> {
                search_bar.isCursorVisible = true
                close_search.visibility = View.VISIBLE
                search_bar.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            }
            R.id.close_search -> {
                if(search_bar.text.toString().isEmpty()){
                    search_bar.isCursorVisible = false
                    search_bar.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    close_search.visibility = View.INVISIBLE
                    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                }
                else {
                    search_bar.text.clear()
                    searchtext = search_bar.text.toString()
                }
            }
        }
    }

    override fun onEditorAction(v: TextView?, a: Int, e: KeyEvent?): Boolean {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if(a == EditorInfo.IME_ACTION_SEARCH){
            searchtext = search_bar.text.toString()
            close_search.visibility = View.INVISIBLE
            search_bar.isCursorVisible = false
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            return true
        }
        return false
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
        var name:String = ""
        var stock:Int
        var price:String = ""

        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "id" -> id = reader.nextLong()
                    "name" -> name = reader.nextString()
                    "stock" -> stock = reader.nextInt()
                    "price" -> price = reader.nextString()
                    else -> reader.skipValue()
                }
                println("Burda2 => " + name)
            }
            println("Burda => " + name)
            rvAdapter.addItem(name + ":" + price)
            reader.endObject()
        }

        return name
    }

}