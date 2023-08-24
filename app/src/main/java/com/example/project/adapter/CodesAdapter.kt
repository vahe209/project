package com.example.project.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.PhoneCodesItem

class CodesAdapter(
    private var codes: ArrayList<PhoneCodesItem>,
    private val closeFragment: CloseFragment,
    private var selectedPosition: Int
) : RecyclerView.Adapter<CodesAdapter.CodesViewHolder>() {
    private var closeOnClick: CloseFragment? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_code_item_row, parent, false)
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
        for (item in codes) {
            item.isSelected = false
        }
    }
    @SuppressLint("NotifyDataSetChanged", "CommitPrefEdits")
    override fun onBindViewHolder(holder: CodesViewHolder, position: Int) {
        val item = codes[position]
        holder.flag.text = item.flag
        holder.countryName.text = item.name
        holder.numberCode.text = item.dialCode
        holder.ifSelected.isVisible = item.isSelected
        holder.background.isVisible = item.isSelected
        if (position == selectedPosition) {
            holder.background.isVisible = true
            holder.ifSelected.isVisible = true
        }
        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            closeFragment.closeFragment(item.flag, item.dialCode, selectedPosition)
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
        fun closeFragment(flag: String, numberCode: String, position: Int?)
    }
}