package com.example

import android.content.Intent
import android.os.Bundle
import retrofit2.Call
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.example.api.EndPoints
import com.example.api.ServiceBuilder
import com.example.api.User
import com.example.trabalho1.R

class LoginActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

       val request = ServiceBuilder.ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUsers()


    }

    fun Button_Notas(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    fun Button_Login(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
     startActivity(intent)
}

}