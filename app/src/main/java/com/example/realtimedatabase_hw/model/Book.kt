package com.example.realtimedatabase_hw.model

import java.io.Serializable
import java.util.*

class Book:Serializable{
    var id:String=""
    var BookName:String=""
    var BookAuthor:String=""
    var year: String? =null
    var price:Int=0
    var rate:Float=0F
    var image:String?=null
    constructor()
    constructor(id:String,BookName:String,BookAuthor:String,year:String,price:Int,rate:Float,image:String){
        this.id=id
        this.BookName=BookName
        this.BookAuthor=BookAuthor
        this.year=year
        this.price=price
        this.rate=rate
        this.image=image
    }
}