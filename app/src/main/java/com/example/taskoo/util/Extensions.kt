package com.example.taskoo.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

fun Fragment.initToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""

    //Exibir botão de voltar
    (activity as AppCompatActivity).supportActionBar?.setDisplayUseLogoEnabled(true)

    // Ação de Voltar
    toolbar.setNavigationOnClickListener{
        activity?.onBackPressedDispatcher?.onBackPressed()
    }



}