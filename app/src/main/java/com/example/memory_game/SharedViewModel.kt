package com.example.memory_game

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private var idOne = MutableLiveData<Int>()
    private var guessedArray = MutableLiveData<ArrayList<Int>>()

    fun setId(id: Int) : Boolean {
        if (idOne.value == null) {
            idOne.value = id
            return false
        } else {
            if (idOne.value == id) {
                if (guessedArray.value == null) {
                    guessedArray.value = ArrayList()
                }
                guessedArray.value?.add(id)
                idOne.value = null
                return true
            }
            idOne.value = null
            return false
        }
    }

    fun getGuessedArray() : ArrayList<Int> {
        if (guessedArray.value != null) {
            return guessedArray.value!!
        }
        return ArrayList()
    }

    fun getIdOneValue() : Int? {
        if (idOne.value == null) {
            return null
        }
        return idOne.value
    }
}