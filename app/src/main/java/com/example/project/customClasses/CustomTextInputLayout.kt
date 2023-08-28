package com.example.project.customClasses

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class CustomTextInputLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {


    override fun setError(errorText: CharSequence?) {
        super.setError(errorText)
        with(findViewById<TextView>(com.google.android.material.R.id.textinput_error)) {
            textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
        }
    }
}
