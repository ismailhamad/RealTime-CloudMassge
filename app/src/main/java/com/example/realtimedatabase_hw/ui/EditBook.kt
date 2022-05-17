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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.maxkeppeler.sheets.options.Option
import com.maxkeppeler.sheets.options.OptionsSheet
import kotlinx.android.synthetic.main.activity_add_book.*
import kotlinx.android.synthetic.main.activity_edit_book.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditBook : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
  //  var db: FirebaseFirestore? = null
    lateinit var id:String
    var videoUrl: Uri? = null

    val TAG = "E_Book"
    lateinit var database: DatabaseReference
    lateinit var date: Date

    var imageUri: Uri? = null
    var storge: FirebaseStorage? = null
    var referance: StorageReference? = null
    lateinit var path:String
    lateinit var path2:String
    lateinit var name:String
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)
        progressDialog=ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Upload...")
        val data = intent.getSerializableExtra("data") as Book
        path=String()
        id= data.id
        name=data.BookName
        storge= Firebase.storage
        referance=storge!!.reference
        database = Firebase.database.reference
        EditBookName.append(data.BookName)
        EditAuthor.append(data.BookAuthor)
        EditYear.append(data.year.toString())
        EditPrice.append(data.price.toString())
        Upload_edit.append(data.image)
        path= data.image.toString()
        path2 = data.video.toString()
        ratingBar2.rating=data.rate
        EditYear.setOnClickListener {
            EditYear.text!!.clear()
            val datePickerDialog= DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
        EdiT.setOnClickListener {
            val name=EditBookName.text.toString()
            val Author=EditAuthor.text.toString()
            val year=EditYear.text.toString()
            val price =EditPrice.text.toString()
            val rate=ratingBar2.rating
            val Book= Book(id,name,Author,year,price.toInt(),rate,path!!,path2)
            UpdateBook(Book)
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

        Delete.setOnClickListener {
            DeleteBook()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        Upload_edit.setOnClickListener {
            Upload_edit.text!!.clear()
            OptionsSheet().show(this) {
                title("Choose")
                with(

                    Option(R.drawable.ic_baseline_insert_photo_24, "gallery")
                )
                onPositive { index: Int, option: Option ->
                    if (index == 0) {
                        dispatchTakeGalleryIntent()
                    }

                }


            }
        }
        video_edit.setOnClickListener {
            video_edit.text?.clear()
            dispatchTakevideoIntent()

        }

    }

    private fun DeleteBook() {
        database.child("book").child(id).removeValue() //setValue(null)
            .addOnSuccessListener {
                PushNotification(
                    NotificationData("delete BOOK ${name}","The book has been successfully delete"),
                    AddBook.topic
                ).also {
                    sendNotification(it)
                }
            }
            .addOnFailureListener {}

    }
    val REQUEST_Video_CAPTURE = 1
    val REQUEST_Gallery_CAPTURE = 2
    private fun dispatchTakeGalleryIntent() {
        val takePictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(takePictureIntent, REQUEST_Gallery_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun dispatchTakevideoIntent() {
        val takePictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(takePictureIntent, REQUEST_Video_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }



    private fun UpdateBook(book: Book) {
        val values = hashMapOf(
       "bookName" to book.BookName,
         "bookAuthor" to book.BookAuthor,
        "year" to book.year,
            "price" to book.price,
        "rate" to book.rate,
        "image" to book.image

        ) as Map<String, Book>

        database.child("book").child(id).updateChildren(values)

        PushNotification(
            NotificationData("update BOOK ${name}","The book has been successfully update"),
            AddBook.topic
        ).also {
            sendNotification(it)
        }

    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val format = SimpleDateFormat("yyyy-MM-dd")
        date = format.parse("$year-"+"$month-"+"$day")
        EditYear.append("$date")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_Gallery_CAPTURE && resultCode == RESULT_OK) {
            Upload_edit.append(data!!.data.toString())
            imageUri = data!!.data
            uploadimage1(imageUri)

        }
        else if(requestCode == REQUEST_Video_CAPTURE && resultCode == RESULT_OK) {
            video_edit.append(data!!.data.toString())
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
             //  Toast.makeText(this@AddBook, "nnnnnnnn", Toast.LENGTH_SHORT).show()
            }else{
                Log.e(TAG,response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e(TAG,e.toString())
        }
    }


}