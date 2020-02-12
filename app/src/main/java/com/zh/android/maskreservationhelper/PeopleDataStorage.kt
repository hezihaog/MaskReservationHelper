package com.zh.android.maskreservationhelper

import com.blankj.utilcode.util.SPUtils
import com.zh.android.maskreservationhelper.model.PeopleDataModel

/**
 * 人员数据存储类
 */
class PeopleDataStorage {
    companion object {
        private const val KEY_YOUR_NAME = "your_name"
        private const val KEY_YOUR_PHONE_NUMBER = "your_phone_number"
        private const val KEY_YOUR_IDENTITY_CARD_ID = "your_identity_card_id"

        /**
         * 保存人员的数据
         */
        fun savePeopleData(name: String, phoneNumber: String, identityCardId: String) {
            SPUtils.getInstance().apply {
                put(KEY_YOUR_NAME, name)
                put(KEY_YOUR_PHONE_NUMBER, phoneNumber)
                put(KEY_YOUR_IDENTITY_CARD_ID, identityCardId)
            }
        }

        /**
         * 获取保存的人员数据
         */
        fun getPeopleData(): PeopleDataModel {
            val instance = SPUtils.getInstance()
            return PeopleDataModel(
                instance.getString(KEY_YOUR_NAME, ""),
                instance.getString(KEY_YOUR_PHONE_NUMBER, ""),
                instance.getString(KEY_YOUR_IDENTITY_CARD_ID, "")
            )
        }
    }
}