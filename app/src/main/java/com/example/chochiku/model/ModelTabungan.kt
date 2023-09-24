package com.example.chochiku.model

class ModelTabungan(
    val id: String?,
    val label: String,
    val waktu: String,
    val tanggal: String,
    val urutan: Int ,
    val keterangan: String


)
{
    constructor() : this("", "", "","",0,"")
}