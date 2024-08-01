package com.example.simplemediasearchapp

import android.content.Context
import com.example.simplemediasearchapp.model.Media
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataStorage(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val key = "favorite"

    fun saveArrayList(list: List<Media>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getArrayList(): List<Media> {
        val json = sharedPreferences.getString(key, null)
        val type = object : TypeToken<ArrayList<Media>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

}