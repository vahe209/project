package com.example.project.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project.data.PhoneCodesItem

class ViewModelRegisterActivity:ViewModel() {
    private var  selectedNumberCodeLiveData  : PhoneCodesItem = PhoneCodesItem("US","+1", "🇺🇸","United States") as PhoneCodesItem

    fun setSelectedNumberCode(item: PhoneCodesItem) {
        selectedNumberCodeLiveData = item
    }
    fun getSelectedNumberCode(): PhoneCodesItem{
        return selectedNumberCodeLiveData
    }
}