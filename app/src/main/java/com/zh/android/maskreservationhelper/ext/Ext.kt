package com.zh.android.maskreservationhelper.ext

import android.content.Context
import android.widget.EditText
import android.widget.Toast

/**
 * 设置EditText数据，并且将光标移动到最后
 */
fun EditText.setTextAndSelection(text: CharSequence) {
    setText(text)
    setSelection(text.length)
}

/**
 * 显示Toast
 */
fun Context.toast(resId: Int) {
    toast(resources.getString(resId))
}

/**
 * 显示Toast
 */
fun Context.toast(text: CharSequence) {
    Toast.makeText(this.applicationContext, text, Toast.LENGTH_SHORT).show()
}