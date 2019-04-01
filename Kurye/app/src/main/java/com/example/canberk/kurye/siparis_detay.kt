package com.example.canberk.kurye
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.Manifest
import android.os.Build
import android.widget.Toast
import android.content.pm.PackageManager
import android.util.Log
import android.annotation.SuppressLint
import android.provider.Settings
import com.google.android.gms.maps.model.CameraPosition

private const val PERMISSION_REQUEST = 10
class siparis_detay : AppCompatActivity(), OnMapReadyCallback {

    private var mMapView: MapView? = null

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.s_detay)
        mMapView = findViewById<View>(R.id.mapView) as MapView?
        val profileName=intent.getStringExtra("customername")
        namesofproducts= intent.getStringArrayListExtra("namesofproducts")
        places2= intent.getStringArrayListExtra("places")
        //adres="customer address"//need to get this from JSON
        //adresLat=0.0 //need to get this from JSON
        //adresLng=0.0 //need to get this from JSON

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                getLocation()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            getLocation()
        }



        //for presentation purposes//////////
        //adres= intent.getStringArrayListExtra("customeraddress")
        adres=intent.getStringArrayListExtra("evadresi")
        adresLat=intent.getDoubleArrayExtra("customerLat")
        //adresLat= 39.907149
        adresLng=intent.getDoubleArrayExtra("customerLng")
        //adresLng= 32.852846
        id=intent.getIntExtra("customerid",0)
        adresLat2= adresLat[id]
        adresLng2= adresLng[id]
        adres2=adres[id]
        /////////////////////////////////////
        val customernameplace = findViewById<TextView>(R.id.textView16)
        customernameplace.text = profileName
        initGoogleMap(savedInstanceState)
        val bottomNavigationView = findViewById<View>(R.id.navigation) as BottomNavigationView

        val menu = bottomNavigationView.menu
        val menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.navigation_dashboard -> {
                    val intent1 = Intent(this@siparis_detay, siparis_listesi::class.java)
                    startActivity(intent1)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_notifications -> {
                    val intent2 = Intent(this@siparis_detay, kurye_profil::class.java)
                    startActivity(intent2)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        val listView = findViewById<ListView>(R.id.siparis_detay_listview)
        listView.adapter = CustomSiparisListesiAdapter(this)
    }

    /////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?){
                        if (location != null) {
                            locationGps = location
                            adresLat2=locationGps!!.latitude
                            adresLng2=locationGps!!.longitude
                        }
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String?) {}
                    override fun onProviderDisabled(provider: String?) {}
                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null) {
                    locationGps = localGpsLocation
                }
            }
            if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location
                            adresLat2=locationNetwork!!.latitude
                            adresLng2=locationNetwork!!.longitude
                        }
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?){}
                    override fun onProviderEnabled(provider: String?){}
                    override fun onProviderDisabled(provider: String?){}
                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null){
                    locationNetwork = localNetworkLocation
                }
            }
            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                    adresLat2=locationNetwork!!.latitude
                    adresLng2=locationNetwork!!.longitude
                }
                else{
                    adresLat2=locationNetwork!!.latitude
                    adresLng2=locationNetwork!!.longitude
                }
            }
        }
        else{
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices){
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED) {
                allSuccess = false
            }
        }
        return allSuccess
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST){
            var allSuccess = true
            for(i in permissions.indices){
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if(requestAgain){
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if(allSuccess){
                getLocation()
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////



    companion object {
        private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        fun newIntent(context: Context, order: Order): Intent {
            val detailIntent = Intent(context, siparis_detay::class.java)

            return detailIntent
        }
        var namesofproducts=arrayListOf<String>()
        var places2=arrayListOf<String>()//marketler
        //var adres=""//müşteri ev adresleri
        var adres= arrayListOf<String>()
        var adresLat= doubleArrayOf()
        var adresLng= doubleArrayOf()
        var id=0
        var namesofproducts2=""
        var places22=""
        var adres2=""
        var adresLat2=0.0
        var adresLng2=0.0
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        mMapView!!.onCreate(mapViewBundle)

        mMapView!!.getMapAsync(this)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mMapView!!.onSaveInstanceState(mapViewBundle)
    }

    public override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    public override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
    }

    public override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
    }

    override fun onMapReady(map: GoogleMap) {

        map.addMarker(MarkerOptions().position(LatLng(adresLat2, adresLng2)).title(adres2))
        val Turkey = LatLngBounds( LatLng(39.720058, 32.546597),  LatLng(40.081523, 33.019929))

        //map.moveCamera(CameraUpdateFactory.newLatLngBounds(Turkey, 0))
        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(adresLat2, adresLng2),12.0F)))
        map.setMinZoomPreference(10.0F)
    }
    public override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    public override fun onDestroy() {
        mMapView!!.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }
    private class CustomSiparisListesiAdapter(context: Context): BaseAdapter(){
        private val mContext: Context

        init{ mContext = context }

        override fun getCount(): Int {
            return namesofproducts.size
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getItem(position: Int): Any {
            return "test"
        }
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.row2,viewGroup,false)
            val nameTextView = rowMain.findViewById<TextView>(R.id.textView12)

            nameTextView.text = namesofproducts.get(position)
            val placeTextView = rowMain.findViewById<TextView>(R.id.textView13)
            placeTextView.text = places2.get(position)
            val positionTextView = rowMain.findViewById<TextView>(R.id.textView14)
            positionTextView.text = "$position"
            return rowMain
            //val textView = TextView(mContext)
            //textView.text = "test text for the row for listview"
            //return textView
        }
    }
}
