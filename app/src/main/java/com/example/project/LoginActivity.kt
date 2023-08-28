package com.example.project

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.project.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textForRegistration.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val isLoginValid = checkEmail()
            val isPasswordValid = checkPassword()
            if (isLoginValid && isPasswordValid){
                // TODO:  login call
            }else{
                println("Error")
            }
        }
    }

    private fun checkPassword(): Boolean {
        val drawable : GradientDrawable = binding.passwordEdit.background as GradientDrawable
        return if(binding.passwordEdit.text.toString().isEmpty()){
            binding.passwordInputLayout.error = "Password is required"
            drawable.setStroke(1, ContextCompat.getColor(this, R.color.bg_btn))
            false
        }else{
            drawable.setStroke(1, ContextCompat.getColor(this, R.color.green))
            binding.passwordInputLayout.error = null
            true
        }
    }

    private fun checkEmail(): Boolean {
        val drawable : GradientDrawable = binding.emailEdit.background as GradientDrawable
        return if(binding.emailEdit.text.toString().isEmpty()){
            drawable.setStroke(1, ContextCompat.getColor(this, R.color.bg_btn))
            binding.emailInputLayout.error = "Email is required"
            false
        }else{
            drawable.setStroke(1, ContextCompat.getColor(this, R.color.green))
            binding.emailInputLayout.error = null
            true
        }
    }
}