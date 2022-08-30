package com.example.exercise

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var tvCalculate : AutoCompleteTextView
    private lateinit var etName : EditText
    private lateinit var etNim : EditText
    private lateinit var etAge : EditText
    private lateinit var etHeight : EditText
    private lateinit var etWeight : EditText
    private lateinit var etUTS : EditText
    private lateinit var etUAS : EditText
    private lateinit var etTask : EditText
    private lateinit var btnCalculate: Button
    private lateinit var btnReset: Button
    private lateinit var tvResult: TextView
    private var resultDropDown :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getId()
        dropDownMenu()
        supportActionBar?.hide()
    }

    // Set Id
    private fun getId(){
        tvCalculate = findViewById(R.id.tvCalculate)
        etName= findViewById(R.id.etName)
        etNim= findViewById(R.id.etNim)
        etAge= findViewById(R.id.etAge)
        etHeight= findViewById(R.id.etHeight)
        etWeight= findViewById(R.id.etWeight)
        etUTS= findViewById(R.id.etUTS)
        etUAS= findViewById(R.id.etUAS)
        etTask = findViewById(R.id.etTask)
        btnCalculate = findViewById(R.id.btnCalculate)
        btnReset = findViewById(R.id.btnReset)
        tvResult = findViewById(R.id.tvResult)

        btnCalculate.visibility = View.GONE
        btnReset.visibility = View.GONE

    }

    // Set DropDownMenu
    private fun dropDownMenu(){
        val data = resources.getStringArray(R.array.calculate)
        val adapter = ArrayAdapter(this,R.layout.dropdown_item,data)
        with(tvCalculate){
            setAdapter(adapter)
            onItemClickListener = this@MainActivity
        }
    }

    // Set DropDown Menu OnClick
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       val item = parent?.getItemAtPosition(position).toString()
        resultDropDown = item
        showResult()

    }

    // Playing Visibility
    private fun showResult(){
        if(resultDropDown == "BMI"){
            etAge.visibility = View.VISIBLE
            etHeight.visibility = View.VISIBLE
            etWeight.visibility = View.VISIBLE
            btnCalculate.visibility = View.VISIBLE
            btnReset.visibility = View.VISIBLE
            etName.visibility = View.GONE
            etNim.visibility = View.GONE
            etUTS.visibility = View.GONE
            etUAS.visibility = View.GONE
            etTask.visibility = View.GONE

            calculateBMI()
            reset()
        }else{
            etName.visibility = View.VISIBLE
            etNim.visibility = View.VISIBLE
            etUTS.visibility = View.VISIBLE
            etUAS.visibility = View.VISIBLE
            etTask.visibility = View.VISIBLE
            btnCalculate.visibility = View.VISIBLE
            btnReset.visibility = View.VISIBLE
            etAge.visibility = View.GONE
            etHeight.visibility = View.GONE
            etWeight.visibility = View.GONE
            calculateStudentAverage()
            reset()
        }
    }

    // Hitung rumus BMI
    @SuppressLint("SetTextI18n")
    private fun calculateBMI(){
        btnCalculate.setOnClickListener {
            val inputAge = etAge.text.toString().toInt()
            val inputHeight = etHeight.text.toString().toDouble() / 100.0
            val inputWeight = etWeight.text.toString().toInt()
            val result = (inputWeight / (inputHeight * inputHeight)).toInt()
            var kategori = ""
            //checking
            when{
                result < 16 -> kategori = "Too Skinny (Terlalu kurus)"
                result in 16..17-> kategori = "Quite Skinny (Cukup Kurus)"
                result.toDouble() in 17.0..18.5 -> kategori = "A Little Skinny (Sedikit Kurus)"
                result.toDouble() in 18.6 ..24.9 -> kategori = "Normal"
                result in 25..30 -> kategori = "Fat / Mini Obesity (Gemuk)"
                result in 30..35 -> kategori = "Obesity Class I (Obesitas Kelas I)"
                result in 35..40 -> kategori = "Obesity Class II (Obesitas Kelas II)"
                result > 40 -> kategori = "Obesity Class III (Obesitas Kelas III)"
            }

            val textResult = """
            Your Age : $inputAge
            Height : ${(inputHeight * 100.0).toInt()}
            Weight : $inputWeight
            Your BMI : $result
            Category : $kategori
        """.trimIndent()

            //checking for result dialog
            when{
                kategori.contains("Skinny") ->  alertDialog(this,R.layout.dialog_item,R.drawable.skinny,textResult)
                kategori.contains("Normal") ->  alertDialog(this,R.layout.dialog_item,R.drawable.normal,textResult)
                kategori.contains("Obesity") ->  alertDialog(this,R.layout.dialog_item,R.drawable.fat,textResult)
            }

        }
    }


    // Hitung rumus rata rata
    @SuppressLint("SetTextI18n")
    private fun calculateStudentAverage(){
        btnCalculate.setOnClickListener {
            val name = etName.text.toString()
            val nim = etNim.text.toString()
            val uts = etUTS.text.toString().toInt()
            val uas = etUAS.text.toString().toInt()
            val task = etTask.text.toString().toInt()
            val average = (uts+uas+task) / 3
            val count = uas+uts+task
            var grade = ' '
            var description = "Not Graduate (Tidak Lulus)"

            // Checking
            if(name.isNotBlank() && nim.isNotBlank()){
                when(average){
                    in 91..100 -> {
                        grade = 'A'
                        description = "Graduate (Lulus)"
                    }
                    in 81..90 ->{
                        grade = 'B'
                        description = "Graduate (Lulus)"
                    }
                    in 71..80 ->{
                        grade = 'C'
                        description = "Graduate (Lulus)"
                    }
                    in 61..70 ->{
                        grade = 'D'
                    }
                    in 0..60 ->{
                        grade = 'E'
                    }

                    else -> {
                        grade = 'E'
                        description = "Not a valid value"
                    }

                }
                val textResult  = """
                    Name : $name
                    Nim : $nim
                    UAS : $uas
                    UTS : $uts
                    Task : $task
                    Count : $count
                    Average Score : $average
                    Grade : $grade
                    Description : $description
                """.trimIndent()

                // checking for result dialog
               when(grade){
                   'A','B','C' ->  alertDialog(this,R.layout.dialog_item,R.drawable.graduate,textResult)
                   else ->  alertDialog(this,R.layout.dialog_item,R.drawable.no_graduate,textResult)
               }
            }else{
                Toast.makeText(this, "Name or nim cannot be empty", Toast.LENGTH_SHORT).show()
            }
            
        }
    }

    // Reset Data
    private fun reset(){
        btnReset.setOnClickListener {
            etAge.setText("")
            etHeight.setText("")
            etWeight.setText("")
            etName.setText("")
            etNim.setText("")
            etUTS.setText("")
            etUAS.setText("")
            etTask.setText("")
        }
    }

    // Custom Alert Dialog
    private fun alertDialog(context: Context, layoutResId : Int,  images :Int, text : String){
        val view = LayoutInflater.from(this).inflate(layoutResId,null)
        val dialog = AlertDialog.Builder(context).create()
        val image = view.findViewById<ImageView>(R.id.imageResult)
        val textResult = view.findViewById<TextView>(R.id.tvResultDialog)
        val close = view.findViewById<ImageView>(R.id.imgClose)
        image.setImageResource(images)
        textResult.text = text
        dialog.apply{
            setView(view)
        }
        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }





}