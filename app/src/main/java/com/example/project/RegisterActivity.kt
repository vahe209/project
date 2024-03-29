package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.project.adapter.CodesAdapter
import com.example.project.data.PhoneCodesItem
import com.example.project.databinding.RegisterActivityBinding
import com.example.project.viewModels.ViewModelRegisterActivity
import java.util.regex.Matcher
import java.util.regex.Pattern

@Suppress("UNREACHABLE_CODE")
class RegisterActivity : AppCompatActivity(), WrongDataFragment.FragmentInteractionListener,
    CodesAdapter.CloseFragment {
    private lateinit var binding: RegisterActivityBinding
    private var validPassword: Boolean = false
    private var isChecked: Boolean = false
    private lateinit var viewModel: ViewModelRegisterActivity
    private lateinit var selectedNumberCode: PhoneCodesItem
    private lateinit var fragment: FragmentEnterNumberCode

    @SuppressLint("UseCompatLoadingForColorStateLists", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ViewModelRegisterActivity::class.java]
        if (intent.extras != null) {
            binding.flag.text = intent.getStringExtra("flag")
            binding.numberCodeFixed.text = intent.getStringExtra("numberCode")
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
        binding.passwordEdit.doOnTextChanged { text, _, _, _ ->
            val drawable : GradientDrawable = binding.passwordEdit.background as GradientDrawable
            if (isValidPass(text.toString())) {
                binding.passwordInputLayout.setErrorTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.green)))
                binding.passwordInputLayout.error = "Excellent"
                drawable.setStroke(1, ContextCompat.getColor(this@RegisterActivity, R.color.green))
                validPassword = true
            } else {
                binding.passwordInputLayout.error = null
                validPassword = false
            }
        }
        binding.confirmEdit.doOnTextChanged { text, _, _, _ ->
            val drawable : GradientDrawable = binding.confirmEdit.background as GradientDrawable
            if (text.toString() == binding.passwordEdit.text.toString() && binding.passwordEdit.text.toString().isNotEmpty())
            {
                binding.confirmInputLayout.apply {
                    binding.confirmInputLayout.setErrorTextColor(
                        ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.green)))
                        drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.green))
                    binding.confirmInputLayout.error = "Values match"
                }
            } else {
                binding.confirmInputLayout.apply {
                    binding.confirmInputLayout.setErrorTextColor(
                        ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
                    drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
                    binding.confirmInputLayout.error = "Values do not match"
                }
            }
        }
        binding.agreementCheckbox.setOnCheckedChangeListener { _, isChecked ->
            this.isChecked = isChecked
        }

        binding.btnJoin.setOnClickListener {
            val isFistNameValid = checkFirstName(binding.fNameEdit.text.toString())
            val isLastNameValid = checkLastName(binding.lNameEdit.text.toString())
            val isEmailValid = checkEmail(binding.emailEdit.text.toString())
            val isPhoneValid = checkPhone(binding.phoneEdit.text.toString())
            val isPasswordValid = checkPass(binding.passwordEdit.text.toString())
            val isConfirmPassValid = checkConfirmPass(binding.confirmEdit.text.toString())
            val isCheckBoxChecked = checkBoxIsChecked()
            if (!isFistNameValid ||
                !isLastNameValid ||
                !isEmailValid ||
                !isPhoneValid ||
                !isPasswordValid ||
                !isConfirmPassValid ||
                !isCheckBoxChecked ||
                binding.passwordEdit.text.toString() != binding.confirmEdit.text.toString()) {
                openErrorFragment()
            }
            if (isFistNameValid &&
                isLastNameValid &&
                isEmailValid &&
                isPhoneValid &&
                isPasswordValid &&
                isConfirmPassValid &&
                isCheckBoxChecked &&
                binding.passwordEdit.text.toString() == binding.confirmEdit.text.toString()) {
                // TODO:  registerCall
                Toast.makeText(this, "Everything is working", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkBoxIsChecked(): Boolean {
        return if (isChecked) {
            binding.agreementText1.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.white))
            binding.agreementText2.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.blue))
            true
        } else {
            binding.agreementText1.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            binding.agreementText2.setTextColor(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            false
        }
    }
    @SuppressLint("SetTextI18n")
    private fun checkConfirmPass(confPass: String): Boolean {
        val drawable : GradientDrawable = binding.confirmEdit.background as GradientDrawable
        return if (confPass.isNotEmpty()) {
            binding.confirmInputLayout.error = null
            true
        } else {
            binding.confirmInputLayout.setErrorTextColor(
                ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
            drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            binding.confirmInputLayout.error = "Confirm password"
            false
        }
    }
    @SuppressLint("SetTextI18n")
    private fun checkPass(password: String): Boolean {
        validPassword = isValidPass(password)
        val drawable : GradientDrawable = binding.passwordEdit.background as GradientDrawable
        return if (password.isNotEmpty()) {
            if (validPassword) {
                drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.green))
                true
            } else {
                binding.passwordInputLayout.setErrorTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
                drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
                binding.passwordInputLayout.error = "Incorrect password type"
                false
            }
        } else {
            binding.passwordInputLayout.setErrorTextColor(
                ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
            binding.passwordInputLayout.error = "Password is required"
            drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            false
        }
    }
    @SuppressLint("SetTextI18n")
    private fun checkPhone(phone: String): Boolean {
        val border = GradientDrawable()
        border.setColor(ContextCompat.getColor(this@RegisterActivity, R.color.grey))
        return if (phone.isNotEmpty()) {
            border.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.green))
            binding.numberEditLayout.background = border
            binding.phoneInputLayout.error= null
            true
        } else {
            border.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            binding.numberEditLayout.background = border
            binding.phoneInputLayout.error = "Phone is required"
            false
        }
    }
    @SuppressLint("SetTextI18n")
    private fun checkLastName(lastName: String): Boolean {
        val drawable : GradientDrawable = binding.lNameEdit.background as GradientDrawable
        return if (lastName.isNotEmpty()) {
            drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.green))
            binding.lNameInputLayout.error = null
            true
        } else {
            binding.lNameInputLayout.setErrorTextColor(
                ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
            binding.lNameInputLayout.error = "Last name is required"
            drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            false
        }
    }
    @SuppressLint("SetTextI18n")
    private fun checkFirstName(firstName: String): Boolean {
        val drawable : GradientDrawable = binding.fNameEdit.background as GradientDrawable
        return if (firstName.isNotEmpty()) {
            drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.green))
            binding.fNameInputLayout.error = null
            true
        } else {
            binding.fNameInputLayout.setErrorTextColor(
                ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
            drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            binding.fNameInputLayout.error = "First name is required"
            false
        }
    }
    @Suppress("UNREACHABLE_CODE")
    private fun isValidPass(password: String): Boolean {
        val regex =
            "^(?=.*\\d)" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[!@#$%^&*()_+~`<>?:{}])" + "(?=\\S+$).{8,20}$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(password)
        return m.matches()
    }
    @SuppressLint("SetTextI18n")
    private fun checkEmail(email: String): Boolean {
        val drawable : GradientDrawable = binding.emailEdit.background as GradientDrawable
        if (email.isNotEmpty()) {
            val regex = "^[A-Za-z\\d+_.-]+@(.+)$"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(email)
            binding.emailInputLayout.error = null
            return if (matcher.matches()) {
                binding.emailInputLayout.error = null
                drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.green))
                true
            } else {
                binding.emailInputLayout.setErrorTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
                binding.emailInputLayout.error = "Incorrect Email"
                drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
                false
            }
        } else {
            binding.emailInputLayout.setErrorTextColor(
                ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn)))
            binding.emailInputLayout.error = "Email is required"
            drawable.setStroke(1,ContextCompat.getColor(this@RegisterActivity, R.color.bg_btn))
            return false
        }
    }
    private fun createLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun showDropdown() {
            selectedNumberCode = viewModel.getSelectedNumberCode()
            fragment = FragmentEnterNumberCode(selectedNumberCode,this)
            fragment.show(supportFragmentManager, fragment.tag)
    }
    private fun openErrorFragment() {
        binding.backArrow.isVisible = false
        binding.registerLayoutToolbar.isVisible = false
        val wrongDataFragment = WrongDataFragment()
        wrongDataFragment.setFragmentInteractionListener(this@RegisterActivity)
        supportFragmentManager.beginTransaction().replace(R.id.toolbar, wrongDataFragment).commit()
    }
    override fun onCloseButtonPressed() {
        binding.backArrow.isVisible = true
        binding.registerLayoutToolbar.isVisible = true
        val fragment = supportFragmentManager.findFragmentById(R.id.toolbar)
        if (fragment is WrongDataFragment) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }
    override fun closeFragment(flag: String, numberCode: String, selectedItem: PhoneCodesItem) {
       fragment.dismiss()
        viewModel.setSelectedNumberCode(selectedItem)
        binding.flag.text = flag
        binding.numberCodeFixed.text = numberCode
    }
}
