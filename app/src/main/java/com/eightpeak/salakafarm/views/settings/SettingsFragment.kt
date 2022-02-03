package com.eightpeak.salakafarm.views.settings

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Observer
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.text.util.Linkify
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.comparelist.CompareListActivity
import com.eightpeak.salakafarm.views.gallery.GalleryActivity
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.pages.PageDetailsView
import com.eightpeak.salakafarm.views.pages.videos.VideoViewActivity
import com.eightpeak.salakafarm.views.splash.SplashActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*
import android.widget.Toast
import android.content.pm.PackageManager
import com.eightpeak.salakafarm.databinding.FragmentSettingsBinding
import com.eightpeak.salakafarm.mapfunctions.BranchActivity
import com.eightpeak.salakafarm.utils.subutils.errorSnack


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
         val intent = Intent(requireContext(), BranchActivity::class.java)
         requireContext().startActivity(intent)

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

        binding.galleryList.setOnClickListener {

            val intent = Intent(requireContext(), GalleryActivity::class.java)
            requireContext().startActivity(intent)

        }

        binding.termsConditions.setOnClickListener {

            val intent = Intent(requireContext(), PageDetailsView::class.java)
            intent.putExtra("page_id","3")
            requireContext().startActivity(intent)

        }

        binding.videosList.setOnClickListener {
            val intent = Intent(requireContext(), VideoViewActivity::class.java)
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

            showDialogContactUs()
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
    private fun showDialogContactUs() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_contact_us)
        val customerName = dialog.findViewById<EditText>(R.id.editTextName)
        val customerContact = dialog.findViewById<EditText>(R.id.editTextContact)
        val customerEmail = dialog.findViewById<EditText>(R.id.editTextEmail)
        val customerSubject = dialog.findViewById<EditText>(R.id.edtSubject)
        val customerDescription = dialog.findViewById<EditText>(R.id.edtDescription)
        val btSummit = dialog.findViewById<Button>(R.id.btSummit)

        val companyFacebook = dialog.findViewById<ImageView>(R.id.company_facebook)
        val companyYoutube = dialog.findViewById<ImageView>(R.id.company_youtube)
        val companyInstagram = dialog.findViewById<ImageView>(R.id.company_instagram)
        Linkify.addLinks(customerContact, Linkify.PHONE_NUMBERS)

        companyFacebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.facebook.com/salakaorganic")

            val packageManager: PackageManager = requireActivity().packageManager
            val list = packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            if (list.size == 0) {
                val urlBrowser = "https://www.facebook.com/salakaorganic"
                intent.data = Uri.parse(urlBrowser)
            }
            startActivity(intent)
        }
        companyYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.youtube.com/channel/UCIx6fxkutSyo-I6KxsSV8zg")

            val packageManager: PackageManager = requireActivity().packageManager
            val list = packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            if (list.size == 0) {
                val urlBrowser = "https://www.youtube.com/channel/UCIx6fxkutSyo-I6KxsSV8zg"
                intent.data = Uri.parse(urlBrowser)
            }
            startActivity(intent)
        }

        companyInstagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.instagram.com/salaka.milk/")
            val packageManager: PackageManager = requireActivity().packageManager
            val list = packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            if (list.size == 0) {
                val urlBrowser = "https://www.instagram.com/salaka.milk/"
                intent.data = Uri.parse(urlBrowser)
            }
            startActivity(intent)
        }

        btSummit.setOnClickListener {
            if(customerSubject.text.isNotEmpty() && customerDescription.text.isNotEmpty()){
            val body = RequestBodies.AddContactUs(customerName.text.toString(),customerContact.text.toString(),customerEmail.text.toString(),customerSubject.text.toString(),customerDescription.text.toString())
            tokenManager?.let { viewModel.addContactUs(body)}
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