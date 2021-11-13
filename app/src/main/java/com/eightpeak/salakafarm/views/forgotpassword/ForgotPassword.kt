package com.eightpeak.salakafarm.views.forgotpassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentCategoriesByIdBinding
import com.eightpeak.salakafarm.viewmodel.CategoriesByIdViewModel
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdAdapter

class ForgotPassword  : AppCompatActivity() {
    private lateinit var viewModel: CategoriesByIdViewModel
    lateinit var categoriesByIdAdapter: CategoriesByIdAdapter


    private var _binding: FragmentCategoriesByIdBinding? = null


    private var layoutManager: GridLayoutManager? = null

    private lateinit var binding: FragmentCategoriesByIdBinding

    lateinit var userPrefManager: UserPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCategoriesByIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefManager = UserPrefManager(this)


    }
}