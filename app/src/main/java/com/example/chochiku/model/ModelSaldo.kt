package com.example.chochiku.model

class ModelSaldo(
    val id: String?,
    val total:String?,
    val limit:String?,
    val hjoyo:String?,
    val hvra:String?,
    val kjoyo:String?,
    val kvra:String?,
    val nominalvra:String?,
    val nominaljoyo:String,
    val stampjoyo:String,
    val stampvra:String,


){
    constructor() : this("", "", "","","","","","","","","")
}