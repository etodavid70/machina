package com.example.machina.data.repository

import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import com.example.machina.data.remote.AuthApi
import com.example.machina.data.remote.DashBoardApi

class DashBoardRepository(

    private val api: DashBoardApi
) {

    suspend fun sendEmail(email: String) {
        api.sendEmail(EmailRequest(email))
    }

    suspend fun verifyCode(email: String, code: String) {
        api.verifyCode(VerifyCodeRequest(email, code))
    }

}