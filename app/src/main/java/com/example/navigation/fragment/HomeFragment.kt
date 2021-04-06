package com.example.navigation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.navigation.MainActivity
import com.example.navigation.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val mypref = MainActivity.MY_PREFERENCES
    private val namekey = MainActivity.NAME_KEY
    private lateinit var navController: NavController
    private val TAG = HomeFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        checkLogin()
        setListeners()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(MainActivity.DESCRIPTION_KEY)
            ?.observe(viewLifecycleOwner, Observer {
                // Do something with result
                Log.i(TAG, "Description is $it")
                updateUIForDescription(it)
            })
    }

    private fun updateUIForDescription(description: String) {
        description_TV.text = description
    }

    /**
     * Check if user logged in
     * If not then redirect to Login fragment
     */
    private fun checkLogin() {
        if(isUserLoggedIn())
        {
            //Toast.makeText(activity,"you are already loged in",Toast.LENGTH_SHORT).show()
        }

        else {
            Toast.makeText(activity, getString(R.string.name_must_enter_info), Toast.LENGTH_LONG).show()
            navigateToLoginDestination()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = activity?.getSharedPreferences(mypref, Context.MODE_PRIVATE)
        val name = sharedPreferences?.getString(this.namekey, "")
        return name != ""
    }

    private fun setListeners() {
        openAccount_BTN.setOnClickListener {
            navigateToAccountDestination()
        }

        addDescription_BTN.setOnClickListener {
            if (!isTitleEntered()) {
                val title = title_ET.text.toString()
                navigateToInputDescriptionDialogFragmentDestination(title)
            } else {
                title_ET.error = "You must enter title"
            }
        }
    }

    private fun navigateToInputDescriptionDialogFragmentDestination(title: String) {
        // Navigate to a destination
        val action =
            HomeFragmentDirections.actionHomeFragmentToInputDescriptionDialogFragment(title) // Sending data using Safe args
        navController.navigate(action)
    }

    private fun isTitleEntered(): Boolean {
        return title_ET.text.toString().isEmpty()
    }

    private fun navigateToAccountDestination() {
        // Navigate to a destination
        val action =
            HomeFragmentDirections.actionHomeFragmentToAccountFragment()
        navController.navigate(action)
    }

    private fun navigateToLoginDestination() {
        // Navigate to a destination
        navController.navigate(R.id.action_global_loginFragment)
    }
}

