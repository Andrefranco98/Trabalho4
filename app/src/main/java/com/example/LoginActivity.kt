package com.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho1.R

class LoginActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

}

    fun Button_Notas(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}