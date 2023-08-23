package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var position: Int = 232
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.extras != null) {
            binding.flag.text = intent.getStringExtra("flag")
            binding.numberCodeFixed.text = intent.getStringExtra("numberCode")
            position = intent.getIntExtra("position", 232)
        }
        binding.backArrow.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.chooseCountryCode.setOnClickListener {
            showDropdown()
        }
        binding.textForLogin.setOnClickListener {
            createLoginActivity()
        }
    }

    private fun createLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showDropdown() {
        val bottomSheetDialogFragment = FragmentEnterNumberCode()
        val bundle = Bundle()
        bundle.putInt("position", position)
        bottomSheetDialogFragment.arguments = bundle
        bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
    }
}