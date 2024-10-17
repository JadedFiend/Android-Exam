package com.example.ecommerceapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.InvocationTargetException

class ViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getConstructor(DatabaseRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("ViewModel must have a constructor with Repository parameter", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("ViewModel constructor is not accessible", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of ViewModel", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Exception during ViewModel construction", e)
        }
    }
}