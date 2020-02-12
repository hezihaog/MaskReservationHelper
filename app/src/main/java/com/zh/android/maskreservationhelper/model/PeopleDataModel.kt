package com.zh.android.maskreservationhelper.model

import java.io.Serializable

/**
 * 人员数据模型
 */
data class PeopleDataModel(
    val name: String,
    val phoneNumber: String,
    val identityCardId: String
) : Serializable {
    companion object {
        private const val serialVersionUID = -3095941595234992499L
    }
}