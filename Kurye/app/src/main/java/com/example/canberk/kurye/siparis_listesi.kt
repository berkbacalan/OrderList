package com.example.canberk.kurye

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import android.widget.Toast
import android.widget.AdapterView





class siparis_listesi : AppCompatActivity() {

    private val mTextMessage: TextView? = null
    private val productnames = arrayListOf<String>(
            "Elma", "Armut", "Peynir", "Süt", "Un", "Kavun", "Dana Pirzola", "Biftek", "Ahududu", "Yoğurt", "Ekmek", "Su", "Tuz", "Fıstık"
    )
    private val shops = arrayListOf<String>(
            "migros", "migros", "migros", "bim", "şok", "şok", "şok", "çağdaş", "çağdaş", "çağdaş", "çağdaş", "Makro Market", "Makro Market", "Makro Market"
    )
    private val evadresi = arrayListOf<String>(
            "Fevzi Çakmak Mah. 124/12", "Arnavut Sok. no:67", "Peynircioğlu Apt. Numara:77", "Sütsever Konak No:3",
            "Uncu Cad. 45/34", "Meneviş Sok. 89/24", "Dilaverdi Caddesi No:572", "Uygur Sokağı 56/45", "Arjantin Cad. no:97",
            "GOP Çilek Apt. 78/67", "Ekmek Caddesi 45/98", "Sucu Sokağı Taksi Durağı", "Tuzbastı Caddesi Numara :87", "Fıstıkçıoğlu Sokak No 25"
    )
    //the market and product lists are going to be the same principle as the list foredeclared list
    //however they are going to be 2 dimentional
    //[customername][shopping list]
    //shopping list contains of 3 arrays [product name][market to buy][amount] that are bound to one counter
    //that counter will place the product name, market, and the amount to every row respectively
    //during the onClick I am planning to reach for the clicked customer's shopping list via the customer's position on the main orders list
    private val customerLat = doubleArrayOf(39.971181,39.944226,40.003365,39.882250,39.914710,39.897883,39.953446,39.947543,39.939863,39.927887,39.921174,39.922890,39.900113,39.947202)
    private val customerLng = doubleArrayOf(32.816978,32.710223,32.763353,32.859744,32.936797,32.758848,32.887901,32.869013,32.861303,32.859825,32.857073,32.845744,32.848818,32.686556)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.s_listesi)
        val bottomNavigationView = findViewById<View>(R.id.navigation) as BottomNavigationView

        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.navigation_dashboard -> {
                    val intent1 = Intent(this@siparis_listesi, siparis_listesi::class.java)
                    startActivity(intent1)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_notifications -> {
                    val intent2 = Intent(this@siparis_listesi, kurye_profil::class.java)
                    startActivity(intent2)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        //populate list with json//
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //val jsonInput = "[\"one\",\"two\",\"three\",\"four\",\"five\",\"six\",\"seven\",\"eight\",\"nine\",\"ten\"]"
        //val jsonArray = JSONArray(jsonInput)
        //val length = jsonArray.length()
        //val listContents = ArrayList<String>(length)
        //for (i in 0 until length) {
        //    listContents.add(jsonArray.getString(i))
        //}
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        val listView = findViewById<ListView>(R.id.siparis_listview)
        //val orderList = Order.getOrdersFromFile("orders.json", this)
        listView.adapter = CustomSiparisListesiAdapter(this)
        listView.setOnItemClickListener { parent, view, position, id ->
            val nameofcutomer=listView.getItemAtPosition(position)
            Toast.makeText(this, "Clicked item :"+" "+nameofcutomer,Toast.LENGTH_SHORT).show()
            val intenttodetail = Intent(this@siparis_listesi, siparis_detay::class.java)
            val productsordered = productnames
            val shopstobuyfrom = shops
            val customerLat2=customerLat
            val customerLng2=customerLng
            val customerhomeaddress= evadresi
            intenttodetail.putExtra("customername",nameofcutomer.toString())
            intenttodetail.putStringArrayListExtra("namesofproducts",productsordered)
            intenttodetail.putStringArrayListExtra("places",shopstobuyfrom)
            intenttodetail.putStringArrayListExtra("evadresi",customerhomeaddress)
            intenttodetail.putExtra("customerLat",customerLat2)
            intenttodetail.putExtra("customerLng",customerLng2)
            intenttodetail.putExtra("customerid",position)
            this.startActivity(intenttodetail)
        }
    }
    class CustomSiparisListesiAdapter(context: Context): BaseAdapter(){
        private val mContext: Context

        private val orders = arrayListOf<String>(
                "Canberk Sakarya", "Azra Kurtaraner", "Utku Çuhadaroğlu", "Burak Durgunsu", "Berk Bacalan", "Steve Jobs", "Bill Gates", "Mark Zuckerber", "Zeki Mazan", "Ali Yılmaz", "Lale Demirkaya", "Sinem Çelik", "Mehmet Yılmazer", "Fikret Yaşar"
        )

        init {
            mContext = context
        }

        override fun getCount(): Int {
            return orders.size
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getItem(position: Int): Any {
            return orders[position]
        }
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.row,viewGroup,false)
            val ordersTextView = rowMain.findViewById<TextView>(R.id.textView4)
            ordersTextView.text = orders.get(position)
            val positionTextView = rowMain.findViewById<TextView>(R.id.textView5)
            positionTextView.text = "Sipariş Numarası: $position"

            val order = getItem(position) as Order
            return rowMain
            //val textView = TextView(mContext)
            //textView.text = "test text for the row for listview"
            //return textView
        }
    }

}
