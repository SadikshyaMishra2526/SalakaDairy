package com.eightpeak.salakafarm.views.settings

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eightpeak.salakafarm.databinding.FragmentSettingsBinding

import androidx.lifecycle.Observer
import android.app.Dialog
import android.content.Intent
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCartBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.comparelist.CompareListActivity
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.pages.PageDetailsView
import com.eightpeak.salakafarm.views.splash.SplashActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import java.util.*


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding


    lateinit var userPrefManager: UserPrefManager


    private lateinit var viewModel: GetResponseViewModel
    private var _binding: FragmentSettingsBinding? = null


    private var tokenManager: TokenManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.returnHome.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }
        tokenManager = TokenManager.getInstance(
           requireContext().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )

        userPrefManager = UserPrefManager(requireContext())

        setLocal(requireActivity())

     binding.ourBranches.setOnClickListener {
         view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_navigation_setting_to_mapsFragment) }

     }

        binding.aboutUs.setOnClickListener {
            val intent = Intent(requireContext(), PageDetailsView::class.java)
            intent.putExtra("page_id","1")
            requireContext().startActivity(intent)

        }
        binding.fragmentFaq.setOnClickListener {
            val intent = Intent(requireContext(), PageDetailsView::class.java)
            intent.putExtra("page_id","4")
            requireContext().startActivity(intent)

        }
        binding.privacyPolicy.setOnClickListener {

            val intent = Intent(requireContext(), PageDetailsView::class.java)
            intent.putExtra("page_id","2")
            requireContext().startActivity(intent)

        }

        binding.termsConditions.setOnClickListener {

            val intent = Intent(requireContext(), PageDetailsView::class.java)
            intent.putExtra("page_id","3")
            requireContext().startActivity(intent)

        }

        binding.compareProducts.setOnClickListener {
            if(App.getData().size!=0){

                val intent = Intent(requireContext(), CompareListActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(requireContext(),getString(R.string.compare_list_empty), Toast.LENGTH_SHORT).show()
            }
        }
        binding.contactUs.setOnClickListener {

            showDialogContactUs(",,,")
        }
        getLanguageChange()
        setupViewModel()
        return binding.fragmentSetting
    }
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
    }
    private fun showDialogContactUs(msg: String?) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_contact_us)
        val customerName = dialog.findViewById<EditText>(R.id.editTextName)
        val customerSubject = dialog.findViewById<EditText>(R.id.edtSubject)
        val customerDescription = dialog.findViewById<EditText>(R.id.edtDescription)
        val btSummit = dialog.findViewById<Button>(R.id.btSummit)

        customerName.setText(userPrefManager.firstName+" "+userPrefManager.lastName)
        btSummit.setOnClickListener {

            if(customerSubject.text.isNotEmpty() && customerDescription.text.isNotEmpty()){
            val body = RequestBodies.AddComplain(
                customerSubject.text.toString(),customerDescription.text.toString())
            tokenManager?.let { viewModel.addComplain(it,body ) }
            getAddComplainResponse(dialog)
        }
        }

        dialog.show()
    }

    private fun getAddComplainResponse(dialog: Dialog) {
        viewModel.addComplain.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { _ ->
                        Toast.makeText(context,"We got your complain..We will get back to you as soon as possible",Toast.LENGTH_SHORT).show()
                       dialog.dismiss()

                            }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        dialog.dismiss()
                       binding.fragmentSetting.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun getLanguageChange() {
        val languages = arrayOf(getString(R.string.language_change), "English", "नेपाली")
        val adapter1 = ArrayAdapter(requireContext(), R.layout.languages, languages)
        adapter1.setDropDownViewResource(R.layout.language)
        binding.languageChange.adapter = adapter1
          binding.languageChange.setSelection(0)
          binding.languageChange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val selectedLang = adapterView.getItemAtPosition(i).toString()
                if (selectedLang == "English") {
                    userPrefManager.language = "en"
                    setLocal(requireActivity())
                    val intent = Intent(requireContext(), SplashActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK //this will always start your activity as a new task
                    startActivity(intent)
                } else if (selectedLang == "नेपाली") {
                    userPrefManager.language = "ne"
                    setLocal(requireActivity())
                    val intent = Intent(requireContext(), SplashActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK //this will always start your activity as a new task
                    startActivity(intent)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }
    fun setLocal(activity: Activity) {
        val locale = Locale(userPrefManager.language)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}