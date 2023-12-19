package com.example.myapplication.view.menuAdmin.adminAddMenu

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.model.room.MenuAdmin
import com.example.myapplication.databinding.ActivityAdminAddMenuBinding
import com.example.myapplication.util.CalorieCalculator
import com.example.myapplication.util.Network


class AdminAddMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminAddMenuBinding
    private lateinit var viewModel: AdminAddMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AdminAddMenuViewModel::class.java]
        viewModel.initializeDBRoom(this)
//        viewModel.getData()
//        viewModel.syncToFirestore(this)

//        val connectivityLiveData = Network.startCheckingInternetConnectivity(this)
////
//        connectivityLiveData.observe(this) { isConnected ->
//            if(isConnected){
////                masukkin ke firestore
////                viewModel.syncToFirestore(this)
////                viewModel.syncToFirestore(){
////                    result->
////                    if(result){
////                        viewModel.deleteAllUnsyncedDataFromRoom()
////
////                    }
////                }
//
//            }else{
//
//            }
//        }


//        viewModel.loginUser(email, password) { result ->
//            if (result) {
//                viewModel.fetchUserRoleFromFirestore()
//            }
//        }}

        setupTextWatchers()

        with(binding){

            btnBack.setOnClickListener {
                finish()
            }

            btnDone.setOnClickListener {
                viewModel.initializeDBRoom(this@AdminAddMenuActivity)

                val name = txtName.text.toString()

                // carbsGram
                val c : String =inputCarbs.text.toString().takeIf { it.isNotBlank() } ?: "0"
//                fatGram
                val d: String = inputFat.text.toString().takeIf { it.isNotBlank() } ?: "0"
//                proteinGram
                val e : String = inputProtein.text.toString().takeIf { it.isNotBlank() } ?: "0"

                val totalcal100 = CalorieCalculator.getTotalCal100(c.toIntOrNull() ?: 0 , d.toIntOrNull() ?: 0 , e.toIntOrNull() ?: 0)
                val menuToadd = MenuAdmin(name=name, calAmount = totalcal100.toString(), carbsGram = c, fatGram = d, proteinGram = e)
                viewModel.addMenu(menuToadd, this@AdminAddMenuActivity)
                finish()
            }

        }
    }


    private fun setupTextWatchers() {
        val textWatchers = arrayOf(
            binding.inputCarbs to binding.calculatedCalCarbs,
            binding.inputProtein to binding.calculatedCalProtein,
            binding.inputFat to binding.calculatedCalFat
        )

        textWatchers.forEach { (input, calculatedCal) ->
            input.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val result = when (calculatedCal) {
                        binding.calculatedCalCarbs -> CalorieCalculator.getCalCarbs(s.toString())
                        binding.calculatedCalProtein -> CalorieCalculator.getCalProtein(s.toString())
                        binding.calculatedCalFat -> CalorieCalculator.getCalFat(s.toString())
                        else -> 0
                    }
                    calculatedCal.text = "$result cal"
                    updateCalculatedAllCalories()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun updateCalculatedAllCalories() {
        val carbs = binding.inputCarbs.text.toString()
        val protein = binding.inputProtein.text.toString()
        val fat = binding.inputFat.text.toString()

        val result = CalorieCalculator.getCalculatedAllCalories(carbs, protein, fat)
        binding.calculatedAllCal1serving.text = "$result calories in 100 gr"
    }



}