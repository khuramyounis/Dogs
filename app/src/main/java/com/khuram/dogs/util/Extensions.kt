package com.khuram.dogs.util


fun List<String>.convertToString(): String {
    val string = StringBuilder()
    for(item in this){
        string.append("$item,")
    }
    return string.toString()
}

fun String.convertToList(): List<String>{
    val list: ArrayList<String> = ArrayList()
    for(item in this.split(",")){
        if(item.isNotEmpty()) list.add(item)
    }
    return list
}