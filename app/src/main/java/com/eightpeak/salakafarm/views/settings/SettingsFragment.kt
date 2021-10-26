package com.eightpeak.salakafarm.views.settings

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eightpeak.salakafarm.databinding.FragmentSettingsBinding

import android.app.Dialog
import android.content.Intent
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.splash.SplashActivity
import com.github.barteksc.pdfviewer.PDFView
import java.util.*


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

//    var language_change: Spinner? = null


    lateinit var userPrefManager: UserPrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.returnHome.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
//            finish()
        }


        userPrefManager = UserPrefManager(requireContext())

        setLocal(requireActivity())


        binding.fragmentFaq.setOnClickListener {

            showDialog(",,,")
        }
        binding.privacyPolicy.setOnClickListener {

            showDialog(",,,")
        }

        binding.termsConditions.setOnClickListener {

            showDialog(",,,")
        }

        binding.contactUs.setOnClickListener {

            showDialogContactUs(",,,")
        }
        getLanguageChange()
        return binding.fragmentSetting
    }
        private fun showDialog(msg: String?) {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.layout_open_pdf)
            val text = dialog.findViewById(R.id.pdfv) as PDFView
            text.fromAsset("policy.pdf").load();
            dialog.show()
        }


    private fun showDialogContactUs(msg: String?) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_contact_us)
        dialog.show()
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
        val locale = Locale(userPrefManager.getLanguage())
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}