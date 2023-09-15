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
            private var salaryTitle:TextView = itemView.findViewById(R.id.textview)

            init {


                itemView.setOnClickListener(){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION) {
                        val selectedSalary = salaryList[position]

                        Toast.makeText(
                            itemView.context,
                            "You choose: ${selectedSalary.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            fun bind(salary: SalaryModel){
                salaryTitle.text = salary.name
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater
            .from(parent.context)
        val itemView = inflater
            .inflate(
            R.layout.recyclerview_item,
            parent,
            false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return salaryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(salaryList[position])
    }
}