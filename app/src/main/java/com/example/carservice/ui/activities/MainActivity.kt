package com.example.carservice.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.carservice.R
import com.example.carservice.ui.ApartmentsFeed
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private val SIGN_IN_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initUIActions()
        addFeedFragment()
    }

    private fun initUI() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view_id)
    }

    private fun initUIActions() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            if(it.itemId == R.id.feed_menu_id){
                addFeedFragment()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun addFeedFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_id,
            ApartmentsFeed.getInstance()
        ).commit()
    }

    private fun authenticateUser() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            promptUserSignIn()
        } else{
            drawCurrentUser()
        }
    }

    private fun promptUserSignIn() {
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
        if (requestCode == SIGN_IN_REQUEST_CODE){
            drawCurrentUser()
        }
    }

    private fun drawCurrentUser(){
        if (FirebaseAuth.getInstance().currentUser == null){
            Toast.makeText(this, "no user", Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(this, FirebaseAuth.getInstance().currentUser.toString(), Toast.LENGTH_LONG).show()
        }
    }

    @Override
    override fun onDestroy() {
        FirebaseAuth.getInstance().signOut()
        super.onDestroy()
    }

    fun startAddApartmentActivity(view: View) {
        val intent = Intent(this, TempAddApartmentActivity::class.java);
        startActivity(intent)
    }
}
