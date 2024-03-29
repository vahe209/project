package com.example.project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.adapter.CodesAdapter
import com.example.project.data.PhoneCodesItem
import com.example.project.databinding.FragmentEnterNumberCodeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.util.*

class FragmentEnterNumberCode(private var selectedItem: PhoneCodesItem?,private val closeFragmentEnterNumberCode: RegisterActivity): BottomSheetDialogFragment(){
    private lateinit var binding: FragmentEnterNumberCodeBinding
    private lateinit var items: ArrayList<PhoneCodesItem>
    private lateinit var adapter: CodesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEnterNumberCodeBinding.inflate(layoutInflater, container, false)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        dialog?.setOnShowListener { dialog ->
            val layout: FrameLayout? =
                (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)
            layout?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.addTextChangedListener(textWatcher)
        binding.closeFragmentBtn.setOnClickListener {
            dismiss()
        }
        loadJson()
    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            filterCodes(editable.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    private fun loadJson() {
        val json: String
        try {
            val inputStream: InputStream = requireContext().assets.open("CountryPhoneCodes.json")
            json = inputStream.bufferedReader().use {
                it.readText()
            }
            items = Gson().fromJson(json, Array<PhoneCodesItem>::class.java).toList() as ArrayList<PhoneCodesItem>
            adapter = CodesAdapter(items, closeFragmentEnterNumberCode, requireContext(), selectedItem)
            binding.rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding.rv.adapter = adapter
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun filterCodes(string: String) {
        val filteredCodes = ArrayList<PhoneCodesItem>()

        for (item in items) {
            if (item.name.lowercase(Locale.getDefault())
                    .contains(string.lowercase(Locale.getDefault())) || item.dialCode.contains(string)) {
                filteredCodes.add(item)
                binding.nothingFoundText.isVisible = false
            }
            if (filteredCodes.isEmpty()) {
                binding.nothingFoundText.isVisible = true
            }
            adapter.filterList(filteredCodes)
        }
    }
}