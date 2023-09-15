package com.example.rv3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myListData: Array<SalaryModel>
    private lateinit var adapter: MyAdapter
    private lateinit var mDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        mDatabase = FirebaseDatabase
            .getInstance()
            .getReference("17m2SXM94KhBDJ3tdHR3lI4HLOZK6CjHQgzg8Zq_gX9Q")
            .child("Sheet1")
            .child("John")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemCount = snapshot.childrenCount.toInt()
                myListData = arrayOfNulls<SalaryModel>(itemCount + 15) as Array<SalaryModel>

                var index = 0

                // Fetching Employee Name, Pay Slip Month, and Email-id as the first three items
                myListData[index++] = SalaryModel(" Employee Name: " + snapshot.child("EmployeeName").getValue(String::class.java))
                myListData[index++] = SalaryModel(" Pay Slip Month: " + snapshot.child("PaySlipMonth").getValue(String::class.java))
                myListData[index++] = SalaryModel(" Email-id: " + snapshot.child("EmailId").getValue(String::class.java))
                myListData[index++] = SalaryModel(" Branch: " + snapshot.child("Branch").getValue(String::class.java))

                // Extracting existing Values in firebase
                val da_arrear = snapshot.child("DA(Arrear)").getValue(Double::class.java)
                val da_arrearValue = da_arrear ?: 0.0

                val ta_arrear = snapshot.child("TA(Arrear)").getValue(Double::class.java)
                val ta_arrearValue = ta_arrear ?: 0.0

                val ia = snapshot.child("IA").getValue(Double::class.java)
                val iaValue = ia ?: 0.0

                val others = snapshot.child("Others").getValue(Double::class.java)
                val othersValue = others ?: 0.0

                val basicPay = snapshot.child("BasicPay").getValue(Double::class.java)
                val basicPayValue = basicPay ?: 0.0

                // Calculating Grade Pay
                val gradePay = basicPay?.let {
                    when {
                        it <= 21600 -> 0
                        it <= 25790 -> 6000
                        it <= 29900 -> 7000
                        it <= 49200 -> 8000
                        it <= 53000 -> 9000
                        else -> 10000
                    }
                } ?: 0


                // Calculating Special Pay
                val specialPay = when {
                    basicPay!! <= 37000 -> 0
                    basicPay <= 75000 -> 2000
                    else -> 5000
                }

                // Calculating DA
                val multiplier = 0.42
                val DA = (basicPay.times(multiplier))?.toInt()

                // Calculating TA
                val TA = if (basicPay > 0) 2000 else 0

                // Calculating HRA
                val hraMultiplier = 0.3
                val hra = (basicPay * hraMultiplier).toInt()

                // Gross Pay
                val grossPay = basicPay + gradePay + specialPay + DA!! + TA + hra + da_arrearValue +
                        ta_arrearValue + iaValue + othersValue
                myListData[index++] = SalaryModel(" Gross Pay: " + grossPay.toString())

                // Basic Pay
                myListData[index++] = SalaryModel(" Basic Pay: " + snapshot.child("BasicPay").getValue())

                // Grade Pay
                myListData[index++] = SalaryModel(" Grade Pay: " + gradePay.toString())

                // Special Pay
                myListData[index++] = SalaryModel(" Special Pay: " + specialPay.toString())

                // DA
                myListData[index++] = SalaryModel(" DA (Dearness Allowance): " + DA.toString())


                // TA
                myListData[index++] = SalaryModel(" TA (Transport Allowance): " + TA.toString())

                // HRA
                myListData[index++] = SalaryModel(" HRA (House Rent Allowance): " + hra.toString())

                // DA(Arrear)
                myListData[index++] = SalaryModel(" DA of Arrear: " + snapshot.child("DA(Arrear)").getValue())

                // TA(Arrear)
                myListData[index++] = SalaryModel(" TA of Arrear: " + snapshot.child("TA(Arrear)").getValue())

                // IA
                myListData[index++] = SalaryModel(" Income Allowance: " + snapshot.child("IA").getValue())

                // Others
                myListData[index++] = SalaryModel(" Others: " + snapshot.child("Others").getValue())

                // ... (other calculations)

                adapter = MyAdapter(myListData)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })

        myListData = arrayOf(
            SalaryModel(" Employee Name: "),
            SalaryModel(" Pay Slip Month:"),
            SalaryModel(" Email-id:"),
            SalaryModel(" BRANCH:"),
            SalaryModel(" GROSS PAY (A)"),
            SalaryModel(" Pay"),
            SalaryModel(" Grade Pay"),
            SalaryModel(" Special Pay"),
            SalaryModel(" Dearness allowance"),
            SalaryModel(" Transport Allowance"),
            SalaryModel(" HRA"),
            SalaryModel(" Arrear of TA"),
            SalaryModel(" DA Arrear"),
            SalaryModel(" Inc-Allowance"),
            SalaryModel(" Others"),
            SalaryModel(" Gross"),
            SalaryModel(" TOTAL DEDUCTION (B)"),
            SalaryModel(" Income Tax"),
            SalaryModel(" NPS"),
            SalaryModel(" GPF"),
            SalaryModel(" Motor Car Advance"),
            SalaryModel(" House Building Advance"),
            SalaryModel(" TA/DA adjustment"),
            SalaryModel(" Electricity"),
            SalaryModel(" CPS Recovery"),
            SalaryModel(" Bus fare/Hire charges"),
            SalaryModel(" LTCITA Medical"),
            SalaryModel(" GSLI"),
            SalaryModel(" LIC"),
            SalaryModel(" NECT and SCL"),
            SalaryModel(" LTC/TA/MEDICAL"),
            SalaryModel(" Interest of Computer Adv"),
            SalaryModel(" HRA Recovery"),
            SalaryModel(" Other"),
            SalaryModel(" Pay Recovery"),
            SalaryModel(" TOTAL DEDUCTION(B)"),
            SalaryModel(" NET PAYABLE(A-B)")
        )

        // Adapter
//        adapter = MyAdapter(myListData)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter

        val adapter = MyAdapter(myListData)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

}