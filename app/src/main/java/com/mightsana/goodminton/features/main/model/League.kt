package com.mightsana.goodminton.features.main.model

import com.google.firebase.Timestamp
import com.mightsana.goodminton.model.repository.users.MyUser

data class League(
    val id: String = "",
    val name: String = "",
    val matchPoints: Int = 0,
    val private: Boolean = true,
    val deuceEnabled: Boolean = true,
    val double: Boolean = false,
    val fixedDouble: Boolean? = null,
    val createdById: String = "",
    val createdAt: Timestamp = Timestamp.now()
)

data class LeagueJoint(
    val id: String = "",
    val name: String = "",
    val matchPoints: Int = 0,
    val private: Boolean = true,
    val deuceEnabled: Boolean = true,
    val double: Boolean = false,
    val fixedDouble: Boolean? = null,
    val createdBy: MyUser = MyUser(),
    val createdAt: Timestamp = Timestamp.now()
)