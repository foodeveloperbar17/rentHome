package com.example.carservice.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.carservice.R
import com.example.carservice.db.FireDatabase
import com.example.carservice.models.Apartment
import com.example.carservice.models.User
import com.example.carservice.presenters.MainActivityPresenter
import com.example.carservice.ui.fragments.ApartmentsFeedFragment
import com.example.carservice.ui.fragments.ProfileFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentFragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initUIActions()

        displayDefaultFragment()

        MainActivityPresenter.onActivityCreated(this)
    }

    private fun initUI() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view_id)
        loadingProgressBar = findViewById(R.id.loading_progress_bar)
    }

    private fun initUIActions() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.feed_menu_id) {
                replaceFragment(ApartmentsFeedFragment())
            } else if (it.itemId == R.id.profile_menu_id) {
                replaceFragment(ProfileFragment.getInstance())
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun displayDefaultFragment() {
        val fragments = supportFragmentManager.fragments
        if (fragments.size == 0) {
            addFragment(ApartmentsFeedFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container_id,
            fragment
        ).commit()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container_id,
            fragment
        ).commit()
    }

    fun apartmentChosen(apartment: Apartment) {
        addFragment(ApartmentFragment.getInstance())
        ApartmentFragment.getInstance().setModel(apartment)
    }

    fun promptUserSignIn() {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).build(), SIGN_IN_REQUEST_CODE
        )
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                startLoading()

                val firebaseUser = FirebaseAuth.getInstance().currentUser!!
                val newUser = User(firebaseUser.uid,
                    firebaseUser.displayName.let { firebaseUser.displayName }.run { "carieli" },
                    firebaseUser.phoneNumber,
                    firebaseUser.email, null, null
                )
                FireDatabase.createOrFetchUser(newUser)
                Log.d("luka", "useria $newUser")
            }
        }
    }

    fun signInFinishedSuccessfully(status: String, user: User){
        updateUIForUser(user)
        signInFinished(status)
    }

    private fun updateUIForUser(user: User){
        val fragments = supportFragmentManager.fragments
        if(fragments.size > 0){
            val lastFragment = fragments[fragments.size - 1]
            if(lastFragment is ApartmentsFeedFragment){
                lastFragment.updateUIForUser(user)
            }
        }
        val badge = bottomNavigationView.getOrCreateBadge(R.id.favourite_menu_id)
        badge.isVisible = true
        user.favourites?.size.let { badge.number = it!! }
    }

    fun addCountToBadge(){
        val badge = bottomNavigationView.getOrCreateBadge(R.id.favourite_menu_id)
        badge.isVisible = true
        badge.number++
    }

    fun decreaseCountToBadge(){
        val badge = bottomNavigationView.getOrCreateBadge(R.id.favourite_menu_id)
        badge.isVisible = true
        badge.number--
    }

    fun signInFinished(status: String) {
        stopLoading()
        Toast.makeText(this, status, Toast.LENGTH_LONG).show()
    }


    fun startLoading(){
        loadingProgressBar.visibility = View.VISIBLE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun stopLoading(){
        loadingProgressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    @Override
    override fun onDestroy() {
        MainActivityPresenter.onActivityDestroyed()
        super.onDestroy()
    }

    //    temp
    fun startAddApartmentActivity(view: View) {
        val intent = Intent(this, TempAddApartmentActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val SIGN_IN_REQUEST_CODE = 0
    }
}
