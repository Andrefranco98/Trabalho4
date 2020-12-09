package com.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.api.EndPoints
import com.example.api.OutputPost

import com.example.api.ServiceBuilder
import com.example.trabalho1.R
import kotlinx.android.synthetic.main.activity_remove_marker2.*
import retrofit2.Call
import retrofit2.Response


class RemoveMarkerActivity : AppCompatActivity() {

    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_marker2)
    }


    fun remove(view: View) {
        id = idremover.text.toString().toInt()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.removeMarcador(id)

        call.enqueue(object : retrofit2.Callback<OutputPost> {

            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {

                if (response.isSuccessful) {
                    val intent = Intent(this@RemoveMarkerActivity, MapsActivity::class.java)
                    Toast.makeText(this@RemoveMarkerActivity, "Point removido com sucesso", Toast.LENGTH_SHORT).show()
                    intent.putExtra("userid",id)
                    startActivity(intent)

                }
            }
            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@RemoveMarkerActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }






}