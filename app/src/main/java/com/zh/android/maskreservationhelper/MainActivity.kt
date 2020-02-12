package com.zh.android.maskreservationhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.blankj.utilcode.util.ToastUtils
import com.zh.android.maskreservationhelper.ext.setTextAndSelection
import com.zh.android.maskreservationhelper.util.ClipboardUtils

class MainActivity : AppCompatActivity() {
    private lateinit var vYourNameInput: EditText
    private lateinit var vCopyYourName: Button
    private lateinit var vYourPhoneNumberInput: EditText
    private lateinit var vCopyYourPhoneNumber: Button
    private lateinit var vYourIdentityCardIdInput: EditText
    private lateinit var vCopyYourIdentityCardId: Button
    private lateinit var vSaveData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val contentView = findViewById<View>(android.R.id.content)
        findView(contentView)
        bindView(contentView)
        setData()
    }

    private fun findView(view: View) {
        vYourNameInput = view.findViewById(R.id.your_name_input)
        vCopyYourName = view.findViewById(R.id.copy_your_name)
        vYourPhoneNumberInput = view.findViewById(R.id.your_phone_number_input)
        vCopyYourPhoneNumber = view.findViewById(R.id.copy_your_phone_number)
        vYourIdentityCardIdInput = view.findViewById(R.id.your_identity_card_id_input)
        vCopyYourIdentityCardId = view.findViewById(R.id.copy_your_identity_card_id)
        vSaveData = view.findViewById(R.id.save_data)
    }

    private fun bindView(view: View) {
        //复制姓名
        vCopyYourName.setOnClickListener {
            val input = vYourNameInput.text.toString()
            if (input.isBlank()) {
                ToastUtils.showShort(R.string.app_please_input_your_name)
                return@setOnClickListener
            }
            copyTextToClipboard(input)
        }
        //复制手机号
        vCopyYourPhoneNumber.setOnClickListener {
            val input = vYourPhoneNumberInput.text.toString()
            if (input.isBlank()) {
                ToastUtils.showShort(R.string.app_please_input_your_phone_number)
                return@setOnClickListener
            }
            copyTextToClipboard(input)
        }
        //复制身份证
        vCopyYourIdentityCardId.setOnClickListener {
            val input = vYourIdentityCardIdInput.text.toString()
            if (input.isBlank()) {
                ToastUtils.showShort(R.string.app_please_input_your_identity_card_id)
                return@setOnClickListener
            }
            copyTextToClipboard(input)
        }
        //保存数据
        vSaveData.setOnClickListener {
            val nameInput = vYourNameInput.text.toString()
            if (nameInput.isBlank()) {
                ToastUtils.showShort(R.string.app_please_input_your_name)
                return@setOnClickListener
            }
            val phoneNumberInput = vYourPhoneNumberInput.text.toString()
            if (phoneNumberInput.isBlank()) {
                ToastUtils.showShort(R.string.app_please_input_your_phone_number)
                return@setOnClickListener
            }
            val identityCardIdInput = vYourIdentityCardIdInput.text.toString()
            if (identityCardIdInput.isBlank()) {
                ToastUtils.showShort(R.string.app_please_input_your_identity_card_id)
                return@setOnClickListener
            }
            PeopleDataStorage.savePeopleData(nameInput, phoneNumberInput, identityCardIdInput)
            ToastUtils.showShort(R.string.app_save_success)
        }
    }

    private fun setData() {
        //回显保存的数据
        val peopleData = PeopleDataStorage.getPeopleData()
        vYourNameInput.setTextAndSelection(peopleData.name)
        vYourPhoneNumberInput.setTextAndSelection(peopleData.phoneNumber)
        vYourIdentityCardIdInput.setTextAndSelection(peopleData.identityCardId)
    }

    /**
     * 拷贝文本到剪切板
     */
    private fun copyTextToClipboard(text: String) {
        ClipboardUtils.copyText(text)
        ToastUtils.showShort(R.string.app_copy_success)
    }
}