package com.example.realtimedatabase_hw.ui

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import com.example.realtimedatabase_hw.*
import com.example.realtimedatabase_hw.FireBase.NotificationData
import com.example.realtimedatabase_hw.FireBase.PushNotification
import com.example.realtimedatabase_hw.FireBase.RetrofitIInstance
import com.example.realtimedatabase_hw.model.Book
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.maxkeppeler.sheets.options.Option
import com.maxkeppeler.sheets.options.OptionsSheet
import kotlinx.android.synthetic.main.activity_add_book.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddBook : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    val TAG = "E_Book"
    var videoUrl: Uri? = null
    lateinit var database: DatabaseReference
   lateinit var date: String
    companion object {
        const val topic = "/topics/myTopic"
    }
    var imageUri: Uri? = null
    var storge: FirebaseStorage? = null
    var referance: StorageReference? = null
    lateinit var path:String
    lateinit var path2:String
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        progressDialog=ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Upload...")
        //db = Firebase.firestore
        path=String()
       storge= Firebase.storage
        referance=storge!!.reference
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
        database = Firebase.database.reference
        yearAdd.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        upload.setOnClickListener {
            OptionsSheet().show(this) {
                title("Choose")
                with(
                    //Option(R.drawable.ic_baseline_photo_camera_24, "camera"),
                    Option(R.drawable.ic_baseline_insert_photo_24, "gallery")
                )
                onPositive { index: Int, option: Option ->
                    if (index == 0) {
                        dispatchTakeGalleryIntent()
                    }

                }


            }
        }
        videoo.setOnClickListener {
            dispatchTakeVideoIntent()
        }


        ButAdd.setOnClickListener {
            ///    Toast.makeText(this, "$path", Toast.LENGTH_SHORT).show()
            val Bookname = textAdd.text.toString()
            val Author = textAuthor.text.toString()
            val year= yearAdd.text.toString()
            val Price = textPrice.text.toString()
            val rate = ratingBar.rating

            val Book =
                Book(UUID.randomUUID().toString(), Bookname, Author, date, Price.toInt(),rate,path,path2)
              AddBoook(Book)
//
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)


        }

    }

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_Gallery_CAPTURE = 2
    val REQUEST_Video_CAPTURE = 3
    private fun dispatchTakeGalleryIntent() {
        val takePictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(takePictureIntent, REQUEST_Gallery_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun dispatchTakeVideoIntent() {
        val takePictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(takePictureIntent, REQUEST_Video_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }


    private fun AddBoook(book: Book) {
        database.child("book/${book.id}").setValue(book)
            .addOnSuccessListener {
                PushNotification(
                    NotificationData("ADD BOOK ${book.BookName}","The book has been successfully added"),
                    topic
                ).also {
                    sendNotification(it)
                }
            }
            .addOnFailureListener {}
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val format = SimpleDateFormat("yyyy-MM-dd")
        date = format.parse("$year-" + "$month-" + "$day").toString()

        yearAdd.append("$date")


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_Gallery_CAPTURE && resultCode == RESULT_OK) {
           upload.append(data!!.data.toString())
            imageUri = data!!.data
            uploadimage1(imageUri)

        }
         else if(requestCode == REQUEST_Video_CAPTURE && resultCode == RESULT_OK) {
            videoo.append(data!!.data.toString())
            videoUrl = data!!.data
            uploadimage1(videoUrl)

        }

    }

    fun uploadimage1(uri: Uri?) {
        progressDialog.show()
  if (videoUrl==uri){
            referance!!.child("book/"+ UUID.randomUUID().toString()).putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri ->
                    path2=uri.toString()
                    progressDialog.dismiss()
                }
            }.addOnFailureListener {exception ->

            }
        }else{
            referance!!.child("book/"+ UUID.randomUUID().toString()).putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri ->
                    path=uri.toString()
                    progressDialog.dismiss()
                }
            }.addOnFailureListener {exception ->

            }
        }

    }






    private  fun sendNotification(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitIInstance.api.postNotifiacation(notification)
            if (response.isSuccessful){
                Toast.makeText(this@AddBook, "nnnnnnnn", Toast.LENGTH_SHORT).show()
            }else{
                Log.e(TAG,response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e(TAG,e.toString())
        }
    }
}