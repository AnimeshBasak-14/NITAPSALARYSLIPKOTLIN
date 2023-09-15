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
    private lateinit var salaryList: Array<SalaryModel>
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
                salaryList = arrayOfNulls<SalaryModel>(itemCount + 15) as Array<SalaryModel>

                var index = 0

                // Fetching Employee Name, Pay Slip Month, and Email-id as the first three items
                salaryList[index++] = SalaryModel(" Employee Name: " + snapshot.child("EmployeeName").getValue(String::class.java))
                salaryList[index++] = SalaryModel(" Pay Slip Month: " + snapshot.child("PaySlipMonth").getValue(String::class.java))
                salaryList[index++] = SalaryModel(" Email-id: " + snapshot.child("EmailId").getValue(String::class.java))
                salaryList[index++] = SalaryModel(" Branch: " + snapshot.child("Branch").getValue(String::class.java))

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
                salaryList[index++] = SalaryModel(" Gross Pay: " + grossPay.toString())

                // Basic Pay
                salaryList[index++] = SalaryModel(" Basic Pay: " + snapshot.child("BasicPay").getValue())

                // Grade Pay
                salaryList[index++] = SalaryModel(" Grade Pay: " + gradePay.toString())

                // Special Pay
                salaryList[index++] = SalaryModel(" Special Pay: " + specialPay.toString())

                // DA
                salaryList[index++] = SalaryModel(" DA (Dearness Allowance): " + DA.toString())


                // TA
                salaryList[index++] = SalaryModel(" TA (Transport Allowance): " + TA.toString())

                // HRA
                salaryList[index++] = SalaryModel(" HRA (House Rent Allowance): " + hra.toString())

                // DA(Arrear)
                salaryList[index++] = SalaryModel(" DA of Arrear: " + snapshot.child("DA(Arrear)").getValue())

                // TA(Arrear)
                salaryList[index++] = SalaryModel(" TA of Arrear: " + snapshot.child("TA(Arrear)").getValue())

                // IA
                salaryList[index++] = SalaryModel(" Income Allowance: " + snapshot.child("IA").getValue())

                // Others
                salaryList[index++] = SalaryModel(" Others: " + snapshot.child("Others").getValue())

                // Extracting existing Values in Firebase
                val grf = snapshot.child("grf").getValue(Double::class.java)
                val grfValue = grf ?: 0.0

                val mca = snapshot.child("MCA").getValue(Double::class.java)
                val mcaValue = mca ?: 0.0

                val tadaAdjustment = snapshot.child("TA&DA_Adjustment").getValue(Double::class.java)
                val tadaAdjustmentValue = tadaAdjustment ?: 0.0

                val lic = snapshot.child("LIC").getValue(Double::class.java)
                val licValue = lic ?: 0.0

                val nectScl = snapshot.child("NECT-SCL").getValue(Double::class.java)
                val nectSclValue = nectScl ?: 0.0

                val intofCom = snapshot.child("Interest Of Computer Adv").getValue(Double::class.java)
                val intofComValue = intofCom ?: 0.0

                val hraRecovery = snapshot.child("HRA Recovery").getValue(Double::class.java)
                val hraRecoveryValue = hraRecovery ?: 0.0

                val deductionOthers = snapshot.child("Others").getValue(Double::class.java)
                val deductionOthersValue = deductionOthers ?: 0.0

                val payRecovery = snapshot.child("Pay Recovery").getValue(Double::class.java)
                val payRecoveryValue = payRecovery ?: 0.0

// Calculating Income Tax
                var incomeTax = 0.0

                if (grossPay <= 300000) {
                    incomeTax = 0.0
                } else if (grossPay <= 600000) {
                    incomeTax = (grossPay - 300000) * 0.05
                } else if (grossPay <= 900000) {
                    incomeTax = 15000 + (grossPay - 600000) * 0.10
                } else if (grossPay <= 1200000) {
                    incomeTax = 45000 + (grossPay - 900000) * 0.15
                } else if (grossPay <= 1500000) {
                    incomeTax = 90000 + (grossPay - 1200000) * 0.20
                } else {
                    incomeTax = 150000 + (grossPay - 1500000) * 0.30
                }

// Calculating NPS
                val percentage = 0.08 // 8% expressed as a decimal
                val nps = (basicPay * percentage + DA * percentage).toInt()

// Calculating HBA
                val hbaPercentage = 0.079 // 7.9% expressed as a decimal
                val hba = (basicPay * hbaPercentage).toInt()

// Calculating Electricity
                val month = snapshot.child("PaySlipMonth").getValue(String::class.java)
                val electricityAmount: Int = if (month != null && (month == "March" || month == "April" || month == "May" || month == "June" || month == "July" || month == "August")) {
                    5000
                } else {
                    2000
                }

// Calculating CPS Recovery
                val cpsRecoveryPercentage = 0.1 // 10% expressed as a decimal
                val cpsRecovery = (cpsRecoveryPercentage * (gradePay + specialPay + DA)).toInt()

// Calculating Transport Fare
                val transportFare: Int = if (basicPay > 0) {
                    200
                } else {
                    0
                }

// Calculating GSLI
                val gslI: Int = when {
                    basicPay >= 16400 -> 6000
                    basicPay >= 8000 -> 4000
                    basicPay >= 4100 -> 2000
                    else -> 0
                }

// Calculating deduction
                val deduction = incomeTax + nps + grfValue + mcaValue + hba + tadaAdjustmentValue + electricityAmount +
                        cpsRecovery + transportFare + gslI + licValue + nectSclValue + intofComValue + hraRecoveryValue +
                        deductionOthersValue + payRecoveryValue

// Calculating NetPay
                val netPay = grossPay - deduction

// Deduction
                salaryList[index++] = SalaryModel("Total Deduction: ${(grossPay.toInt())}")
// Income Tax
                salaryList[index++] = SalaryModel("Income Tax: ${(incomeTax.toInt())}")
// NPS
                salaryList[index++] = SalaryModel("NPS (Nation Pension Scheme): $nps")
// GRF
                salaryList[index++] = SalaryModel("GRF (Graduate Research Fellowship): ${snapshot.child("grf").getValue()}")
// MCA
                salaryList[index++] = SalaryModel("MCA (Motor Car Allowance): ${snapshot.child("MCA").getValue()}")
// HBA
                salaryList[index++] = SalaryModel("HBA (House Building Advance): $hba")
// TA&DA Adjustment
                salaryList[index++] = SalaryModel("TA&DA Adjustment: ${snapshot.child("TA&DA_Adjustment").getValue()}")
// Electricity
                salaryList[index++] = SalaryModel("Electricity: $electricityAmount")
// CPS Recovery
                salaryList[index++] = SalaryModel("CPS Recovery: $cpsRecovery")
// Transport Fare
                salaryList[index++] = SalaryModel("Transport Fare: $transportFare")
// GSLI
                salaryList[index++] = SalaryModel("GSLI: $gslI")
// LIC
                salaryList[index++] = SalaryModel("LIC: ${snapshot.child("LIC").getValue()}")
// NECT/SCL
                salaryList[index++] = SalaryModel("NECT/SCL: ${snapshot.child("NECT-SCL").getValue()}")
// Interest of Computer Adv
                salaryList[index++] = SalaryModel("Interest of Computer Adv: ${snapshot.child("Interest Of Computer Adv").getValue()}")
// HRA Recovery
                salaryList[index++] = SalaryModel("HRA Recovery: ${snapshot.child("HRA Recovery").getValue()}")
// Others
                salaryList[index++] = SalaryModel("Others: ${snapshot.child("Others").getValue()}")
// Pay Recovery
                salaryList[index++] = SalaryModel("Pay Recovery: ${snapshot.child("Pay Recovery").getValue()}")
// NetPay
                salaryList[index++] = SalaryModel("Net Pay: ${(netPay.toInt())}")


                adapter = MyAdapter(salaryList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })

        salaryList = arrayOf(
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
//        adapter = MyAdapter( salaryList)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter

        val adapter = MyAdapter(salaryList)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

}