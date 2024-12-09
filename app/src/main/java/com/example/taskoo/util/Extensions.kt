package com.example.taskoo.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.taskoo.R
import com.example.taskoo.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

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

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog )
    val binding = BottomSheetBinding.inflate(layoutInflater, null, false)

    binding.txtTitle.text = getText(titleDialog ?: R.string.text_title_warning)
    binding.btnOK.text = getText(titleDialog ?: R.string.text_button_warning)
    binding.txtMessage.text = message
    binding.btnOK.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(binding.root)
    bottomSheetDialog.show()
}