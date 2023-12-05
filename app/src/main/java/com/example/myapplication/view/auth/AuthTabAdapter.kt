package com.example.myapplication.view.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.view.auth.login.LoginFragment
import com.example.myapplication.view.auth.register.RegisterFragment

class AuthTabAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    val page = arrayOf(RegisterFragment(), LoginFragment())

    override fun getItemCount(): Int {
        return page.size

    }

    override fun createFragment(position: Int): Fragment {
        return page[position]
    }
}