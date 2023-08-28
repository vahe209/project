package com.example.project.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.RegisterActivity
import com.example.project.data.PhoneCodesItem

class CodesAdapter(
    private var codes: ArrayList<PhoneCodesItem>,
    private val closeFragment: RegisterActivity,
    private val context: Context,
    private val selectedItem: PhoneCodesItem?
) : RecyclerView.Adapter<CodesAdapter.CodesViewHolder>() {
    private var closeOnClick: CloseFragment? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.country_code_item_row, parent, false)
        closeOnClick = closeFragment
        return CodesViewHolder(view)
    }
    override fun getItemCount(): Int {
        return codes.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(list: ArrayList<PhoneCodesItem>) {
        codes = list
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged", "CommitPrefEdits")
    override fun onBindViewHolder(holder: CodesViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = codes[position]
        if (item == selectedItem) {
            item.isSelected = true
        }
        holder.ifSelected.isVisible = item.isSelected
        holder.background.isVisible = item.isSelected
        holder.flag.text = item.flag
        holder.countryName.text = item.name
        holder.numberCode.text = item.dialCode
        holder.itemView.setOnClickListener {
            for (code in codes) {
                item.isSelected = false
            }
            closeFragment.closeFragment(item.flag, item.dialCode, item)
            notifyDataSetChanged()
        }
    }
    class CodesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flag: TextView = itemView.findViewById(R.id.flag)
        val numberCode: TextView = itemView.findViewById(R.id.number_code)
        val countryName: TextView = itemView.findViewById(R.id.country_name)
        val ifSelected: TextView = itemView.findViewById(R.id.if_selected)
        val background: ConstraintLayout = itemView.findViewById(R.id.row_constraint)
    }
    interface CloseFragment {
        fun closeFragment(flag: String, numberCode: String, selectedItem: PhoneCodesItem)
    }
}