package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
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
        println("created")
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
            if (isValidPass(text.toString())) {
                binding.passwordInfo.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@RegisterActivity, R.color.green
                        )
                    )
                )
                binding.passwordInfo.text = "Excellent"
                validPassword = true
            } else {
                binding.passwordInfo.text = ""
                validPassword = false
            }
        }
        binding.confirmEdit.doOnTextChanged { text, _, _, _ ->
            if (text.toString() == binding.passwordEdit.text.toString() && binding.passwordEdit.text.toString()
                    .isNotEmpty()
            ) {
                binding.confirmInputLayout.apply {
                    error = null
                    binding.confirmInfo.setTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@RegisterActivity, R.color.green
                            )
                        )
                    )
                    binding.confirmInfo.text = "Values match"
                }
            } else {
                binding.confirmInputLayout.apply {
                    binding.confirmInfo.setTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@RegisterActivity, R.color.bg_btn
                            )
                        )
                    )
                    binding.confirmInfo.text = "Values do not match"
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
            if (!isFistNameValid || !isLastNameValid || !isEmailValid || !isPhoneValid || !isPasswordValid || !isConfirmPassValid || !isCheckBoxChecked) {
                openErrorFragment()
            }
            if (isFistNameValid && isLastNameValid && isEmailValid && isPhoneValid && isPasswordValid && isConfirmPassValid && isCheckBoxChecked && binding.passwordEdit.text.toString() == binding.confirmEdit.text.toString()) {
                // TODO:  registerCall
                Toast.makeText(this, "Everything is working", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkBoxIsChecked(): Boolean {
        return if (isChecked) {
            binding.agreementCheckboxInfo.isVisible = false
            true
        } else {
            binding.agreementCheckboxInfo.isVisible = true
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkConfirmPass(confPass: String): Boolean {
        return if (confPass.isNotEmpty()) {
            binding.numberInfo.text = ""
            true
        } else {
            binding.confirmInfo.text = "Confirm password"
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkPass(password: String): Boolean {
        validPassword = isValidPass(password)
        return if (password.isNotEmpty()) {
            if (validPassword) {
                true
            } else {
                binding.passwordInfo.text = "Incorrect password type"
                false
            }
        } else {
            binding.passwordInfo.text = "Password is required"
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkPhone(phone: String): Boolean {
        return if (phone.isNotEmpty()) {
            binding.numberInfo.text = ""
            true
        } else {
            binding.numberInfo.text = "Phone is required"
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkLastName(lastName: String): Boolean {
        return if (lastName.isNotEmpty()) {
            binding.lastNameInfo.text = ""
            true
        } else {
            binding.lastNameInfo.text = "Last name is required"
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkFirstName(firstName: String): Boolean {
        return if (firstName.isNotEmpty()) {
            binding.firstNameInfo.text = ""
            true
        } else {
            binding.firstNameInfo.text = "First name is required"
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
        if (email.isNotEmpty()) {
            val regex = "^[A-Za-z\\d+_.-]+@(.+)$"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(email)
            binding.emailInfo.text = ""
            return if (matcher.matches()) {
                binding.emailInfo.text = ""
                true
            } else {
                binding.emailInfo.text = "Incorrect Email"
                false
            }
        } else {
            binding.emailInfo.text = "Email is required"
            return false
        }
    }

    private fun createLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun showDropdown() {
            selectedNumberCode = viewModel.selectedNumberCodeLiveData
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
