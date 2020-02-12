package com.zh.android.maskreservationhelper.ext

import android.widget.EditText

/**
 * 设置EditText数据，并且将光标移动到最后
 */
fun EditText.setTextAndSelection(text: CharSequence) {
    setText(text)
    setSelection(text.length)
}