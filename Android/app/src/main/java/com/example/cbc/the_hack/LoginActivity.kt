package com.example.cbc.the_hack

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cbc.the_hack.changeavaterview.MainActivity
import com.example.cbc.the_hack.util.OkHttp3Util
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Call
import okhttp3.Response
import shem.com.materiallogin.DefaultLoginView
import shem.com.materiallogin.DefaultRegisterView
import shem.com.materiallogin.MaterialLoginView
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private val preferences by lazy { SharedPreferencesUtils(this) }

    private fun View.yum(text: String, length: Int = Snackbar.LENGTH_SHORT): Snackbar {
        return Snackbar.make(this, text, length)
    }

    private fun onRegister(user: String, password: String) {
        preferences.username = user
        OkHttp3Util.doPost("http://47.103.21.70/register", mapOf(
            "username" to user,
            "password" to password
        ), object : okhttp3.Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                login.yum("Error! Please try again!").show()
            }
            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    var parser = JsonParser()
                    var is_success = parser.parse(response.body().toString()).asJsonObject["is_success"].asInt
                    if (is_success == -1){
                        login.yum("Username has already exited!").show()
                    }
                    else {
                        preferences.isLogin = true
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                }
                else {
                    login.yum("Error!").show()
                }
            }
        }
        )
    }

    private fun onLogin(user: String, password: String) {
        preferences.username = user
        OkHttp3Util.doPost("http://47.103.21.70/login", mapOf(
            "username" to user,
            "password" to password
        ), object : okhttp3.Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                login.yum("Error! Please try again!").show()
            }
            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    var parser = JsonParser()
                    var is_success = parser.parse(response.body().toString()).asJsonObject["is_success"].asInt
                    if (is_success == -1){
                        login.yum("Username or password is wrong!").show()
                    }
                    else {
                        preferences.isLogin = true
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                }
                else {
                    login.yum("Username or password is wrong!").show()
                }
            }
        }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (preferences.isLogin){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        ((login as MaterialLoginView).registerView as DefaultRegisterView).setListener { registerUser, registerPass,
                                                                                         registerPassRep ->
            val passRep = registerPassRep.editText!!.text.toString()
            val pass = registerPass.editText!!.text.toString()
            val user = registerUser.editText!!.text.toString()
            if (passRep != pass) {
                login.yum("Two passwords didn't match").show()
            } else if (pass == "" || user == "") {
                login.yum("Invalid Username or Password").show()
            } else {
                onRegister(user, pass)
            }
        }
        ((login as MaterialLoginView).loginView as DefaultLoginView).setListener { loginUser, loginPass ->
            val pass = loginPass.editText!!.text.toString()
            val user = loginUser.editText!!.text.toString()
            if (user == "" || pass == "") {
                login.yum("Invalid Username or Password").show()
            } else {
                onLogin(user, pass)
            }
        }
    }
}
