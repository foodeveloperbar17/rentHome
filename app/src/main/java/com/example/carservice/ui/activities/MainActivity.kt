package com.example.carservice.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.carservice.R
import com.example.carservice.db.FireDatabase
import com.example.carservice.models.Apartment
import com.example.carservice.models.User
import com.example.carservice.presenters.MainActivityPresenter
import com.example.carservice.ui.fragments.ApartmentsFeedFragment
import com.example.carservice.ui.fragments.FavouritesFragment
import com.example.carservice.ui.fragments.ProfileFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentFragment
import com.example.carservice.utils.LanguageUtils
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var mainDrawerLayout: DrawerLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var leftNavigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initUIActions()

        displayDefaultFragment()

        MainActivityPresenter.onActivityCreated(this)
    }

    private fun initUI() {
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mainDrawerLayout = findViewById(R.id.main_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            mainDrawerLayout,
            toolbar,
            R.string.nav_opened_description,
            R.string.nav_closed_description
        )

        mainDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        leftNavigationView = findViewById(R.id.nav_view)
        centerMenuItems()

        loadingProgressBar = findViewById(R.id.loading_progress_bar)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view_id)
    }

    private fun centerMenuItems() {
        leftNavigationView.menu.iterator().forEach {
            centerMenuItem(it)
        }
//        this is a xack
        leftNavigationView.menu.getItem(0).subMenu.children.forEach {
            centerMenuItem(it)
        }
    }

    private fun centerMenuItem(menuItem: MenuItem) {
        val spannableString = SpannableString(menuItem.title)
        spannableString.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            0,
            menuItem.title.length,
            0
        )
        menuItem.title = spannableString
    }

    private fun initUIActions() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.feed_menu_id -> {
                    replaceFragment(ApartmentsFeedFragment())
                }
                R.id.favourite_menu_id -> {
                    MainActivityPresenter.favouritesButtonClicked()
                }
                R.id.profile_menu_id -> {
                    MainActivityPresenter.profileButtonClicked()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        leftNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.georgian_language_item -> {
                    LanguageUtils.setLanguage(LanguageUtils.GEORGIAN_LANGUAGE_CODE, this)
                }
                R.id.english_language_item -> {
                    LanguageUtils.setLanguage(LanguageUtils.ENGLISH_LANGUAGE_CODE, this)
                }
                R.id.contacts_menu_item_id -> {

                }
                R.id.terms_and_conditions_menu_item_id -> {

                }
                R.id.parameters_menu_item_id -> {

                }
                R.id.log_in_menu_item_id -> {
                    promptUserSignIn()
                }
                R.id.log_out_menu_item_id -> {
                    FireDatabase.signOut()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun displayDefaultFragment() {
        val fragments = supportFragmentManager.fragments
        if (fragments.size == 0) {
            replaceFragment(ApartmentsFeedFragment())
        }
    }

    fun showFavouritesFragment(favouriteApartments: ArrayList<Apartment>?) {
        val favouritesFragment =
            FavouritesFragment()
        replaceFragment(favouritesFragment)
        favouritesFragment.setApartments(favouriteApartments)
    }

    fun showProfileFragment(user: User) {
        val profileFragment = ProfileFragment()
        replaceFragment(profileFragment)
        profileFragment.setUser(user)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container_id,
            fragment
        ).addToBackStack(null).commit()
    }

    private fun replaceFragmentWithoutBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container_id,
            fragment
        ).commit()
    }

    fun apartmentChosen(apartment: Apartment) {
        val apartmentFragment = ApartmentFragment()
        apartmentFragment.setApartment(apartment)
        showBackArrow(true)
        setToolbarTitle(apartment.name ?: "Apartment")

        val fragments = supportFragmentManager.fragments
        if (fragments.size > 0) {
            if (fragments.last() is ApartmentFragment) {
                replaceFragmentWithoutBackStack(apartmentFragment)
            } else {
                replaceFragment(apartmentFragment)
            }
        }
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
                    firebaseUser.displayName.let { firebaseUser.displayName } ?: run { "carieli" },
                    firebaseUser.phoneNumber,
                    firebaseUser.email, null, null
                )
                FireDatabase.createOrFetchUser(newUser)
            }
        }
    }

    fun signInFinishedSuccessfully(status: String, user: User) {
        updateUIForUser(user)
        signInFinished(status)
    }

    private fun updateUIForUser(user: User) {
        val fragments = supportFragmentManager.fragments
        if (fragments.size > 0) {
            val lastFragment = fragments[fragments.size - 1]
            if (lastFragment is ApartmentsFeedFragment) {
                lastFragment.updateUIForUser(user)
            }
        }
        val badge = bottomNavigationView.getOrCreateBadge(R.id.favourite_menu_id)
        badge.isVisible = true
        user.favourites?.let {
            badge.number = it.size
        }
    }

    fun addCountToBadge() {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.favourite_menu_id)
        badge.isVisible = true
        badge.number++
    }

    fun decreaseCountToBadge() {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.favourite_menu_id)
        badge.isVisible = true
        badge.number--
    }

    fun signInFinished(status: String) {
        stopLoading()
        Toast.makeText(this, status, Toast.LENGTH_LONG).show()
    }


    fun startLoading() {
        loadingProgressBar.visibility = View.VISIBLE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun stopLoading() {
        loadingProgressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showBackArrow(show: Boolean) {
        if (show) {
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            actionBarDrawerToggle.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            actionBarDrawerToggle.setToolbarNavigationClickListener {
                onBackPressed()
            }
        } else {
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        }
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
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
