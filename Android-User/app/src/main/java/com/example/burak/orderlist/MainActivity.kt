package com.example.burak.orderlist

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import com.example.burak.orderlist.Pages.Order
import com.example.burak.orderlist.Pages.Profile
import com.example.burak.orderlist.Pages.ShoppingCart
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.util.JsonReader
import org.json.JSONObject


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    var prevMenuItem: MenuItem? = null
    val ft = supportFragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        ft.replace(R.id.container, Order()).commit()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

/*        if (id == R.id.action_settings) {
            return true
        }*/

        return super.onOptionsItemSelected(item)
    }

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var fragment: Fragment? = null
        val ft = supportFragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.navigation_home -> {
                fragment = Order()
                ft.replace(R.id.container, Order()).commit();
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fragment = ShoppingCart()
                ft.replace(R.id.container, fragment).commit();
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fragment = Profile()
                ft.replace(R.id.container, fragment).commit();
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {

        if (prevMenuItem != null) {
            prevMenuItem?.setChecked(false)
        }
        else
        {
            navigation.menu.getItem(0).isChecked = false
        }
        prevMenuItem = navigation.menu.getItem(position)
        navigation.menu.getItem(0).isChecked = true
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            var fragment: Fragment? = null
            setContentView(R.layout.activity_main)

            if(position == 0) fragment = Order()
            else if(position == 1) fragment = ShoppingCart()
            else if(position == 2) fragment = Profile()

            return fragment
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }


}


