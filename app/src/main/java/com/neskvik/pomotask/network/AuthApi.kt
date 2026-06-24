package com.neskvik.pomotask.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// в эмуляторе 10.0.2.2  для локальных
const val BASE_URL = "http://10.0.2.2:5087"

data class AuthResponse(val token: String, val username: String, val email: String)

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

object AuthApi {

    suspend fun login(email: String, password: String): ApiResult<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                val conn = openJson("$BASE_URL/api/auth/login", "POST")
                val body = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }.toString()
                conn.outputStream.bufferedWriter().use { it.write(body) }
                parseResponse(conn)
            } catch (e: Exception) {
                ApiResult.Error("Нет соединения с сервером")
            }
        }

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): ApiResult<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val conn = openJson("$BASE_URL/api/auth/register", "POST")
            val body = JSONObject().apply {
                put("username", username)
                put("email", email)
                put("password", password)
            }.toString()
            conn.outputStream.bufferedWriter().use { it.write(body) }
            parseResponse(conn)
        } catch (e: Exception) {
            ApiResult.Error("Нет соединения с сервером")
        }
    }

    private fun openJson(urlStr: String, method: String): HttpURLConnection {
        val conn = URL(urlStr).openConnection() as HttpURLConnection
        conn.requestMethod = method
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Accept", "application/json")
        conn.doOutput = true
        conn.connectTimeout = 10_000
        conn.readTimeout = 10_000
        return conn
    }

    private fun parseResponse(conn: HttpURLConnection): ApiResult<AuthResponse> {
        val code = conn.responseCode
        val body = if (code == 200) {
            conn.inputStream.bufferedReader().readText()
        } else {
            conn.errorStream?.bufferedReader()?.readText() ?: "{}"
        }
        val json = JSONObject(body)
        return if (code == 200) {
            ApiResult.Success(
                AuthResponse(
                    token = json.getString("token"),
                    username = json.getString("username"),
                    email = json.getString("email")
                )
            )
        } else {
            ApiResult.Error(json.optString("message", "Произошла ошибка"))
        }
    }
}
