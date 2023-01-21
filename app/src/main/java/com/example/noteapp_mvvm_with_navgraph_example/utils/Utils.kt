package com.example.noteapp_mvvm_with_navgraph_example.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun makeVisible(vararg views: View) {
    for (view in views) {
        view.visibility = View.VISIBLE
    }
}

fun makeInvisible(vararg views: View) {
    for (view in views) {
        view.visibility = View.INVISIBLE
    }
}

fun makeGone(vararg views: View) {
    for (view in views) {
        view.visibility = View.INVISIBLE
    }
}

fun Context.toast(message: String){
    Toast.makeText(this, message , Toast.LENGTH_SHORT).show()
}

fun String.log(key: String = "LOG") {
    Log.e(key, this)
}

fun String.toast(context: Context, show: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, show).show()
}

fun String.snackBar(view: View, show: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(view, this, show).show()
}