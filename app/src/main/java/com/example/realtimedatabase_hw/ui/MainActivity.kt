package com.example.realtimedatabase_hw.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimedatabase_hw.adapter.BookAdapter
import com.example.realtimedatabase_hw.R
import com.example.realtimedatabase_hw.model.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var database: DatabaseReference;
    val TAG="E_Book"
    lateinit var bookAdapter: BookAdapter
    lateinit var ArrayList:ArrayList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ArrayList= arrayListOf()
        database = Firebase.database.reference
        getAllBook()
        floatingActionButton.setOnClickListener {
            val i = Intent(this, AddBook::class.java)
            startActivity(i)
        }


    }


   private fun getAllBook() {

       val userListener = object : ValueEventListener {
           override fun onDataChange(dataSnapshot: DataSnapshot) {

               for (doc in dataSnapshot.children) {
                   val bookAuthor = doc.child("bookAuthor").value
                   val id = doc.child("id").value
                   val bookName= doc.child("bookName").value
                   val image = doc.child("image").value
                   val price = doc.child("price").value
                   val rate = doc.child("rate").value
                   val year = doc.child("year").value
                    val book= Book(id.toString(),bookName.toString(),bookAuthor.toString(),year.toString(),price.toString().toInt(),rate.toString().toFloat(),image.toString())

                  // Toast.makeText(this@MainActivity, "${book.BookAuthor}", Toast.LENGTH_SHORT).show()
                  ArrayList.remove(book)
                 ArrayList.addAll(listOf(book))


               }
               bookAdapter= BookAdapter(this@MainActivity,ArrayList)
               recyclerView.layoutManager= LinearLayoutManager(this@MainActivity)
               recyclerView.adapter=bookAdapter

           }


           override fun onCancelled(databaseError: DatabaseError) {

               Log.w(TAG, "loadUser:onCancelled", databaseError.toException())
           }
       }


       database.child("book").addValueEventListener(userListener)


   }



//        db.collection("book").addSnapshotListener{ snapshot,e->
//            if(e!=null){
//                Log.w(TAG,"listen faild",e)
//                return@addSnapshotListener
//            }
//            for (doc in snapshot!!.documentChanges){
//                when(doc.type){
//                    DocumentChange.Type.ADDED -> setUpRec(doc, DocumentChange.Type.ADDED)
//                    DocumentChange.Type.REMOVED ->DeleteDoc(doc)
//                    DocumentChange.Type.MODIFIED ->Update(doc)
//                }
//
//
//            }
//
//            bookAdapter= BookAdapter(this,ArrayList)
//            recyclerView.layoutManager= LinearLayoutManager(this)
//            recyclerView.adapter=bookAdapter
//            recyclerView.recycledViewPool.clear()
//            recyclerView.post {
//                bookAdapter.notifyDataSetChanged()
//            }
//
//        }
//
//    }
//
//    private fun Update(doc: DocumentChange?) {
//        val books =doc!!.document.toObject<Book>()
//        update=books
//    }
//
//
//    fun DeleteDoc(doc: DocumentChange?) {
//        val books =doc!!.document.toObject<Book>()
//        Boook=books
//
//    }
    companion object{
        var Boook: Book?=null
        var update: Book?=null
    }

//
//    private fun setUpRec(doc: DocumentChange?, type: DocumentChange.Type) {
//        val books =doc!!.document.toObject<Book>()
//        ArrayList.add(books)
//
//    }





}