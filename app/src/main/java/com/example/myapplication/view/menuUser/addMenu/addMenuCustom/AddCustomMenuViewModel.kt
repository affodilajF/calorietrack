package com.example.myapplication.view.menuUser.addMenu.addMenuCustom

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.data.database.MenuDAO
import com.example.myapplication.data.database.MenuRoomDatabase
import com.example.myapplication.data.model.MenuData
import com.example.myapplication.util.SharedPreferencesHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddCustomMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesHelper =
        SharedPreferencesHelper.getInstance(application.applicationContext)

    private lateinit var mMenuDao : MenuDAO
    private lateinit var executorService : ExecutorService


    fun initializeDBRoom(context: Context){
        executorService = Executors.newSingleThreadExecutor()
        val db = MenuRoomDatabase.getDatabase(context)
        mMenuDao = db!!.menuDao()!!
    }
    fun insertRoom(menu : MenuData){
        menu.userId = sharedPreferencesHelper.getUserId().toString()

        val runnable = object : Runnable {
            override fun run() {
                mMenuDao.insert(menu)
            }
        }
        executorService.execute(runnable)
    }


    fun formattedInt(param: String): Int {
        return try {
            val value = param.toIntOrNull() ?: 0
            value
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun formattedDouble2(param: String): Double {
        return try {
            val value = param.toDoubleOrNull() ?: 0.0
            String.format("%.0f", value).toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
    fun formattedDouble(param: String): Double {
        return try {
            val value = param.toDoubleOrNull() ?: 0.0
            String.format("%.1f", value).toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
    fun getUserId(): String {
        return sharedPreferencesHelper.getUserId() ?: ""
    }


    fun getTodayDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }
    fun getFormattedDate(date : Date): String {
        val simpleDateFormat = SimpleDateFormat("EEEE, yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getTotalCal(servings: String?, calories: String?): String {
        val a = servings?.toDoubleOrNull() ?: 0.0
        val b = calories?.toDoubleOrNull() ?: 0.0

        return String.format("%.0f", a * b)
    }
    fun getCalculatedAllCalories(gram: String?, gram2: String?, gram3: String?): String {
        val a = getCalCarbs(gram).toDouble()
        val b = getCalProtein(gram2).toDouble()
        val c = getCalFat(gram3).toDouble()

        val totalCalories = a + b + c
        return totalCalories.toString()
    }


    fun getCalCarbs(gram: String?): String {
        val grams = gram?.toIntOrNull() ?: 0
        val calories = grams * 4
        return calories.toString()
    }


    fun getCalProtein(gram: String?): String {
        val grams = gram?.toIntOrNull() ?: 0
        val calories = grams * 4
        return calories.toString()
    }

    fun getCalFat(gram: String?): String {
        val grams = gram?.toIntOrNull() ?: 0
        val calories = grams * 9
        return calories.toString()
    }


}