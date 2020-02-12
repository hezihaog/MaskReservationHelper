package com.zh.android.maskreservationhelper

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.zh.android.floatwindow.FloatWindowManager
import com.zh.android.floatwindow.FloatWindowPermissionCallback
import com.zh.android.floatwindow.WindowPermissionAgent
import com.zh.android.floatwindow.WindowPermissionUtil
import com.zh.android.maskreservationhelper.ext.setTextAndSelection
import com.zh.android.maskreservationhelper.ext.toast
import com.zh.android.maskreservationhelper.util.ClipboardUtils

class MainActivity : AppCompatActivity() {
    private lateinit var vYourNameInput: EditText
    private lateinit var vCopyYourName: Button
    private lateinit var vYourPhoneNumberInput: EditText
    private lateinit var vCopyYourPhoneNumber: Button
    private lateinit var vYourIdentityCardIdInput: EditText
    private lateinit var vCopyYourIdentityCardId: Button
    private lateinit var vSaveData: Button

    /**
     * 悬浮窗权限申请
     */
    private lateinit var mPermissionAgent: WindowPermissionAgent
    /**
     * 悬浮窗管理器
     */
    private val mFloatWindowController by lazy {
        FloatWindowController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val contentView = findViewById<View>(android.R.id.content)
        findView(contentView)
        bindView()
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

    private fun bindView() {
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
        //显示悬浮窗
        requestFloatWindowPermission {
            showFloatWindow()
        }
    }

    /**
     * 显示悬浮窗
     */
    private fun showFloatWindow() {
        mFloatWindowController.makeFloatWindow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.mPermissionAgent.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 申请悬浮窗权限
     */
    private fun requestFloatWindowPermission(onAllowBlock: () -> Unit) {
        if (!this::mPermissionAgent.isInitialized) {
            this.mPermissionAgent =
                WindowPermissionAgent(this, PermissionCallback(this, Runnable {
                    onAllowBlock()
                }))
        }
        if (WindowPermissionUtil.hasPermission(this)) {
            mPermissionAgent.callback.onPermissionAllow()
        } else {
            //请求权限
            mPermissionAgent.requestPermission()
        }
    }

    private class PermissionCallback(
        private val mActivity: Activity,
        private val mAction: Runnable
    ) :
        FloatWindowPermissionCallback {
        override fun onPermissionAllow() {
            mAction.run()
        }

        override fun onPermissionReject() {
            mActivity.toast("请允许" + mActivity.resources.getString(R.string.app_name) + "出现在顶部")
        }
    }

    /**
     * 拷贝文本到剪切板
     */
    private fun copyTextToClipboard(text: String) {
        ClipboardUtils.copyText(text)
        toast(R.string.app_copy_success)
    }
}