package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import retrofit2.Call
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.api.EndPoints
import com.example.api.OutputPost
import com.example.api.ServiceBuilder
import com.example.api.User
import com.example.trabalho1.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response


class LoginActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)




    }

    fun Button_Notas(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    fun Button_Login(view: View) {
        val username = user.text.toString().trim()
        val password = password.text.toString().trim()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.loginPost(username,password)



        call.enqueue(object : retrofit2.Callback<OutputPost> {
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                if (response.isSuccessful) {
                    if (response.body()?.error == false) {
                        val intent = Intent(this@LoginActivity, MapsActivity::class.java)
                        Toast.makeText(this@LoginActivity, "Login efectuado", Toast.LENGTH_SHORT).show()

                        /// GET NAME SHARED PREFERENCES ////

                        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                        var editor = token.edit()
                        editor.putString("username_login_atual",username)
                        editor.commit()

                        startActivity(intent)
                    }else{
                        val c: OutputPost = response.body()!!
                        Toast.makeText(this@LoginActivity, "Login falhou, credenciais erradas.", Toast.LENGTH_SHORT).show()



                    }

                }
            }
            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onStart() {
        super.onStart()
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)      // declaras o token -> username
        if(token.getString("username_login_atual"," ") != " ") {            //


            val intent = Intent(this@LoginActivity, MapsActivity::class.java)       // ENTRA NA ATIVIDADE

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

}

