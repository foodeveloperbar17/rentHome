package com.example.carservice.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R
import com.example.carservice.db.FireDatabase
import com.example.carservice.db.FireStorage
import com.example.carservice.models.Apartment
import com.example.carservice.ui.adapters.ApartmentSecondaryImagesAdapter
import com.google.android.gms.maps.model.LatLng
import java.util.*
import java.util.concurrent.CountDownLatch

class TempAddApartmentActivity : AppCompatActivity() {

    private lateinit var saveApartmentButton: Button
    private lateinit var uploadImagesButton: Button

    private lateinit var mainImage: ImageView
    private lateinit var secondaryImageRecyclerView: RecyclerView
    private lateinit var secondaryImagesAdapter: ApartmentSecondaryImagesAdapter

    private lateinit var nameTextView: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var latTextView: EditText
    private lateinit var lngTextView: EditText

    private lateinit var countDownLatch: CountDownLatch

    private var imagesPaths = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_add_apartment)

        initUI()
        initUIActions()
    }

    private fun initUI() {
        saveApartmentButton = findViewById(R.id.add_apartment_button)
        uploadImagesButton = findViewById(R.id.upload_images)

        mainImage = findViewById(R.id.upload_main_image_view)
        initRecyclerView()

        nameTextView = findViewById(R.id.name_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        priceEditText = findViewById(R.id.price_edit_text)
        latTextView = findViewById(R.id.lat_edit_text)
        lngTextView = findViewById(R.id.lng_edit_text)
    }

    private fun initRecyclerView() {
        secondaryImageRecyclerView = findViewById(R.id.upload_secondary_image_recycler_view)
        secondaryImagesAdapter = ApartmentSecondaryImagesAdapter()
        secondaryImageRecyclerView.adapter = secondaryImagesAdapter
        secondaryImageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initUIActions() {
        saveApartmentButton.setOnClickListener {
            initializeForWorker()
            val name = nameTextView.text.toString()
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString().toDouble()
            val lat = latTextView.text.toString().toDouble()
            val lng = lngTextView.text.toString().toDouble()
            val apartment = Apartment(null, name, description, price, LatLng(lat, lng))
            Thread {
                imagesPaths.forEach {
                    FireStorage.uploadImageByUri(it, countDownLatch, apartment)
                }
            }.start()
            countDownLatch.await()
            FireDatabase.addApartment(apartment)
            Toast.makeText(this, "apartments successfully uploaded", Toast.LENGTH_LONG).show()
        }

        uploadImagesButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        RC_READ_STORAGE_PERMISSION
                    )
                }
            }
        }
    }

    private fun initializeForWorker() {
        countDownLatch = CountDownLatch(imagesPaths.size)
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, RC_ADD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ADD_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    imagesPaths.clear()

                    if (it.data != null) {
//                        I got one image
                        showImageOnMainImage(it.data)
                        imagesPaths.add(it.data!!)
                    } else {
                        it.clipData?.let { clipData ->
                            val count = clipData.itemCount
                            if (count > 0) {
                                showImageOnMainImage(clipData.getItemAt(0).uri)

                                for (i in 0 until count) {
                                    val uri = clipData.getItemAt(i).uri
                                    imagesPaths.add(uri)
                                }
                                showSecondaryImages()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showImageOnMainImage(imagePath: Uri?) {
        imagePath?.let {
            val bitmap = getBitmapFromUri(it)
            mainImage.setImageBitmap(bitmap)
        }
    }

    private fun showSecondaryImages() {
        val secondaryBitmapDrawables = ArrayList<Drawable>()
        imagesPaths.forEach {
            val bitmap = getBitmapFromUri(it)
            secondaryBitmapDrawables.add(BitmapDrawable(bitmap))
        }
        secondaryImagesAdapter.setData(secondaryBitmapDrawables)
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= 29) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_READ_STORAGE_PERMISSION) {
            val index = permissions.indexOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (index != -1) {
                val granted = grantResults[index]
                if (granted == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser()
                }
            }
        }
    }

    companion object {
        private const val RC_ADD_IMAGE = 17
        private const val RC_READ_STORAGE_PERMISSION = 777
    }
}
