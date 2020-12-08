package com.example

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.api.EndPoints
import com.example.api.OutputPost
import com.example.api.ServiceBuilder
import com.example.trabalho1.R
import kotlinx.android.synthetic.main.activity_add_marker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMarkerActivity : AppCompatActivity() {
    private var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_marker)

        var latitude = intent.getStringExtra("latitude")
        var longitude = intent.getStringExtra("longitude")

        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        id = token.getInt("id_login_atual", 0)

        findViewById<TextView>(R.id.lat).setText(latitude)          // coloca valor da latitude no campo Lat do XML
        findViewById<TextView>(R.id.lng).setText(longitude)         // coloca valor da longitude no campo Lng do XML
        findViewById<TextView>(R.id.iduser).setText(id.toString()) // coloca valor do user_id no campo iduser do XML



    }

    fun addMarker(view: View) {


        val latitude = lat.text.toString().trim()           // variavel latitude toma o valor do campo lat do xml
        val longitude = lng.text.toString().trim()          // variavel longitude toma o valor do campo lng do xml
        val descr = descr.text.toString().trim()            // variavel descr toma o valor do campo descr do xml

        val request = ServiceBuilder.buildService(EndPoints::class.java)     // crio o request
        val call = request.postMarker(latitude,longitude,descr,id)     // crio a call


        call.enqueue(object : Callback<OutputPost> {

            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {

                if (response.isSuccessful) {
                    val intent = Intent(this@AddMarkerActivity, MapsActivity::class.java)
                    Toast.makeText(this@AddMarkerActivity, "Novo Marcador inserido com sucesso", Toast.LENGTH_SHORT).show()
                    intent.putExtra("user_id",id)
                    startActivity(intent)

                }
            }
            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@AddMarkerActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}