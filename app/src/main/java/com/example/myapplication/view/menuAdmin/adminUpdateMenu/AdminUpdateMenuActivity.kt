package com.example.myapplication.view.menuAdmin.adminUpdateMenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.model.firestore.Menu
import com.example.myapplication.databinding.ActivityAdminUpdateMenuBinding
import com.example.myapplication.util.CalorieCalculator

class AdminUpdateMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminUpdateMenuBinding
    private lateinit var viewModel: AdminUpdateViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUpdateMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AdminUpdateViewModel::class.java]
        viewModel.initializeDBRoom(this)

        setUpTextWatchers()

        with(binding) {
            val menu = intent.getSerializableExtra("menu object") as Menu
            val nama = menu.name
            val cal100 = menu.calAmount
            val carbs = menu.carbsGram
            val protein = menu.proteinGram
            val fat = menu.fatGram

            val editableName = Editable.Factory.getInstance().newEditable(nama)
            txtName.text = editableName

            calculatedAllCal1serving.text = cal100 + " calories in 100 gr"

            val editableGrCarbs = Editable.Factory.getInstance().newEditable(carbs)
            inputCarbs.text = editableGrCarbs
            calculatedCalCarbs.text = CalorieCalculator.getCalCarbs(carbs).toString() + " cal"

            val editableGrProtein = Editable.Factory.getInstance().newEditable(protein)
            inputProtein.text = editableGrProtein
            calculatedCalProtein.text = CalorieCalculator.getCalProtein(protein).toString() + " cal"

            val editableGrFat = Editable.Factory.getInstance().newEditable(fat)
            inputFat.text = editableGrFat
            calculatedCalFat.text = CalorieCalculator.getCalFat(fat).toString() + " cal"


            btnDel.setOnClickListener {
                viewModel.deleteMenu(menu)
                finish()
            }
            btnBack.setOnClickListener {
                finish()
            }

            btnUpdate.setOnClickListener {
                val a = Menu(
                    id = menu.id,
                    name = txtName.text.toString(),
                    fatGram = inputFat.text.toString(),
                    carbsGram = inputCarbs.text.toString(),
                    proteinGram = inputProtein.text.toString(),
                    calAmount = CalorieCalculator.getTotalCal100(
                        inputCarbs.text.toString(),
                        inputProtein.text.toString(),
                        inputFat.text.toString()
                    )
                )
                viewModel.updateMenu(a)

                finish()
            }
        }



        }


    private fun setUpTextWatchers() {
        with(binding) {
            val watchers = arrayOf(
                Pair(inputCarbs, calculatedCalCarbs),
                Pair(inputProtein, calculatedCalProtein),
                Pair(inputFat, calculatedCalFat)
            )
            watchers.forEach { (input, calculatedCal) ->
                input.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val calResult = when (calculatedCal) {
                            calculatedCalCarbs -> CalorieCalculator.getCalCarbs(s.toString()).toString()
                            calculatedCalProtein -> CalorieCalculator.getCalProtein(s.toString()).toString()
                            calculatedCalFat -> CalorieCalculator.getCalFat(s.toString()).toString()
                            else -> "0"
                        }

                        calculatedCal.text = "$calResult cal"
                        updateAllCalories()
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }
    }

    private fun updateAllCalories() {
        with(binding) {
            val result = CalorieCalculator.getCalculatedAllCalories(
                inputCarbs.text.toString(),
                inputProtein.text.toString(),
                inputFat.text.toString()
            )
            calculatedAllCal1serving.text = "$result calories in 100 gr"
        }
    }
}