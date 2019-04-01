package com.example.canberk.kurye

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class kurye_profil : AppCompatActivity() {

    private val mTextMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kurye_profil)
        val bottomNavigationView = findViewById<View>(R.id.navigation) as BottomNavigationView

        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(1)
        menuItem.isChecked = true
        val button = findViewById<Button>(R.id.logoutbutton)
        button.setOnClickListener {
            val intentlogout = Intent(this@kurye_profil, Login::class.java)
            startActivity(intentlogout)
        }
        val button2 = findViewById<Button>(R.id.emergency)
        button2.setOnClickListener {
            Toast.makeText(this, "Acil Durum Sinyali Göderildi!", Toast.LENGTH_SHORT).show()
        }
        var kuryeData: Array<String> = arrayOf("Canberk Sakarya", "131101078", "Ayrancı - ANKARA", "06 MBL 572")
        var textView = findViewById<TextView>(R.id.textView)
        textView.text = kuryeData[0]
        var textView2 = findViewById<TextView>(R.id.textView2)
        textView2.text = kuryeData[1]
        var textView7 = findViewById<TextView>(R.id.textView7)
        textView7.text = kuryeData[2]
        var textView8 = findViewById<TextView>(R.id.textView8)
        textView8.text = kuryeData[3]

        //Setting the text from the strings.xml file.
        //textView.text = resources.getString(R.string.app_name)

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.navigation_dashboard -> {
                    val intent1 = Intent(this@kurye_profil, siparis_listesi::class.java)
                    startActivity(intent1)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_notifications -> {
                    val intent2 = Intent(this@kurye_profil, kurye_profil::class.java)
                    startActivity(intent2)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }
}
