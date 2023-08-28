package com.example.project.viewModels

import androidx.lifecycle.ViewModel
import com.example.project.data.PhoneCodesItem

class ViewModelRegisterActivity:ViewModel() {
    private var  selectedNumberCodeLiveData= PhoneCodesItem("US","+1", "🇺🇸","United States")

    fun setSelectedNumberCode(item: PhoneCodesItem) {
        selectedNumberCodeLiveData = item
    }
    fun getSelectedNumberCode(): PhoneCodesItem{
        return selectedNumberCodeLiveData
    }
}