package com.zh.android.maskreservationhelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.zh.android.floatwindow.FloatMoveEnum
import com.zh.android.floatwindow.FloatWindowManager
import com.zh.android.floatwindow.FloatWindowOption
import com.zh.android.floatwindow.ScreenUtil
import com.zh.android.maskreservationhelper.ext.setTextAndSelection
import com.zh.android.maskreservationhelper.ext.toast
import com.zh.android.maskreservationhelper.model.PeopleDataModel
import com.zh.android.maskreservationhelper.util.AppBroadcastManager
import com.zh.android.maskreservationhelper.util.ClipboardUtils

class FloatWindowController(private val context: Context) {
    private lateinit var vYourNameInput: EditText
    private lateinit var vCopyYourName: Button
    private lateinit var vYourPhoneNumberInput: EditText
    private lateinit var vCopyYourPhoneNumber: Button
    private lateinit var vYourIdentityCardIdInput: EditText
    private lateinit var vCopyYourIdentityCardId: Button

    companion object {
        private const val TAG_FLOAT = "float_tag"
    }

    /**
     * 悬浮窗管理器
     */
    private val mFloatWindowManager by lazy {
        FloatWindowManager(context)
    }

    /**
     * 显示悬浮窗
     */
    fun makeFloatWindow() {
        mFloatWindowManager.makeFloatWindow(
            createFloatView(), TAG_FLOAT, FloatWindowOption.create(
                FloatWindowOption.Builder()
                    .setX(0)
                    .setY(0)
                    .setWidth(ScreenUtil.getScreenWidth(context))
                    .desktopShow(true)
                    .setFloatMoveType(FloatMoveEnum.SLIDE)
                    .setDuration(250)
                    .setBoundOffset(ScreenUtil.dip2px(context.applicationContext, 5f))
            )
        )
    }

    /**
     * 创建悬浮窗View
     */
    private fun createFloatView(): View {
        val layout: ViewGroup = LayoutInflater.from(context).inflate(
            R.layout.view_people_data_layout,
            null
        ) as ViewGroup
        findView(layout)
        bindView(layout)
        setData()
        return layout
    }

    private fun findView(view: View) {
        vYourNameInput = view.findViewById(R.id.your_name_input)
        vCopyYourName = view.findViewById(R.id.copy_your_name)
        vYourPhoneNumberInput = view.findViewById(R.id.your_phone_number_input)
        vCopyYourPhoneNumber = view.findViewById(R.id.copy_your_phone_number)
        vYourIdentityCardIdInput = view.findViewById(R.id.your_identity_card_id_input)
        vCopyYourIdentityCardId = view.findViewById(R.id.copy_your_identity_card_id)
    }

    private fun bindView(view: ViewGroup) {
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
        //设置半透明和白色字体
        view.setBackgroundColor(Color.argb(80, 0, 0, 0))
        setWhiteTheme(view)
    }

    /**
     * 设置白色主题
     */
    private fun setWhiteTheme(view: ViewGroup) {
        for (index in 0..view.childCount) {
            val childView = view.getChildAt(index)
            //遇到布局，递归进内部的子View
            if (childView is ViewGroup) {
                setWhiteTheme(childView)
            }
            if (childView is Button) {
                continue
            }
            if (childView is TextView) {
                childView.setTextColor(context.resources.getColor(R.color.white))
                childView.isEnabled = false
            }
        }
    }

    private fun setData() {
        //回显保存的数据
        val peopleData = PeopleDataStorage.getPeopleData()
        vYourNameInput.setTextAndSelection(peopleData.name)
        vYourPhoneNumberInput.setTextAndSelection(peopleData.phoneNumber)
        vYourIdentityCardIdInput.setTextAndSelection(peopleData.identityCardId)
        //注册广播接收资料更新
        AppBroadcastManager.registerReceiver(context, object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.run {
                    val model = getSerializableExtra(Constant.Key.PEOPLE_DATA) as PeopleDataModel
                    model.run {
                        vYourNameInput.setTextAndSelection(name)
                        vYourPhoneNumberInput.setTextAndSelection(phoneNumber)
                        vYourIdentityCardIdInput.setTextAndSelection(identityCardId)
                    }
                }
            }
        }, Constant.Action.UPDATE_PEOPLE_DATA)
    }

    /**
     * 拷贝文本到剪切板
     */
    private fun copyTextToClipboard(text: String) {
        ClipboardUtils.copyText(text)
        context.toast(R.string.app_copy_success)
    }
}