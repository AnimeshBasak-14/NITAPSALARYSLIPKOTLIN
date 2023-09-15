package com.example.rv3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val salaryList: Array<SalaryModel>)
    :RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
        inner class MyViewHolder(itemView: View)
            :RecyclerView.ViewHolder(itemView){
            lateinit var salaryTitle:TextView

            init {

                salaryTitle = itemView.findViewById(R.id.textview)

                itemView.setOnClickListener(){
                    Toast.makeText(itemView.context,
                        "You choose: ${salaryList[adapterPosition].name}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent,false)

        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {

        return salaryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.salaryTitle.setText(salaryList[position].name)
    }
}