package com.harsh.walkie_talkie.network

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.harsh.walkie_talkie.data.model.Notification
import com.harsh.walkie_talkie.data.model.Profile
import com.harsh.walkie_talkie.network.response.ApiResponse
import com.harsh.walkie_talkie.network.response.DataResponse
import com.harsh.walkie_talkie.util.Constants
import com.harsh.walkie_talkie.util.Constants.BASE_URL
import com.harsh.walkie_talkie.util.PreferencesHelper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

class ApiService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    fun saveFirebase(context: Context) {
        val profile = Profile(Constants.genUid(), Constants.genMID())
        PreferencesHelper.setProfile(context, profile)

        FirebaseDatabase.getInstance().getReference("profiles").child(profile.uid)
            .setValue(profile).addOnSuccessListener {
                PreferencesHelper.setSettled(context, true)
            }.addOnFailureListener {
                PreferencesHelper.setSettled(context, false)
            }
    }

    suspend fun saveProfile(context: Context): ApiResponse {
        return try {
            val profile = Profile(Constants.genUid(), Constants.genMID())
            PreferencesHelper.setProfile(context, profile)

            val response: HttpResponse = client.post("${BASE_URL}profiles/save") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }

            response.body()
        } catch (e: Exception) {
            ApiResponse(error = "Error: ${e.message}")
        }
    }

    fun getFirebase(uid: String, context: Context, success: (Profile?, String) -> Unit) {
        FirebaseDatabase.getInstance().getReference("profiles").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profile = Profile(snapshot.child("uid").value.toString(), snapshot.child("token").value.toString())
                    success(profile, "Found")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Profile not found!", Toast.LENGTH_SHORT).show()
                    success(null, "Not Found")
                }
            })
    }

    suspend fun getProfile(uid: String): DataResponse<Profile> {
        return try {
            val data = client.get("${BASE_URL}profiles/$uid") {
                contentType(ContentType.Application.Json)
            }

            if (data.status == HttpStatusCode.OK) {
                val profile: Profile = data.body()
                DataResponse(success = profile, error = null)
            } else {
                val errorResponse = data.body<ApiResponse>()
                DataResponse(success = null, error = errorResponse.error)
            }
        } catch (e: Exception) {
            DataResponse(success = null, error = "Network error: ${e.message}")
        }
    }

    fun updateFirebase(context: Context, uid: String, updateData: Map<String, String>) {
        val profile = Profile(uid, updateData["token"].toString())
        PreferencesHelper.setProfile(context, profile)

        FirebaseDatabase.getInstance().getReference("profiles").child(profile.uid)
            .updateChildren(updateData)
    }

    suspend fun updateToken(context: Context, uid: String, updateData: Map<String, String>): ApiResponse {
        return try {
            val data = client.patch("${BASE_URL}profiles/$uid") {
                contentType(ContentType.Application.Json)
                setBody(updateData)
            }

            if (data.status == HttpStatusCode.OK) {
                val profile = Profile(uid, updateData["token"].toString())
                PreferencesHelper.setProfile(context, profile)
                ApiResponse(message = data.body<ApiResponse>().message, error = null)
            } else {
                ApiResponse(message = null, error = data.body<ApiResponse>().error)
            }
        } catch (e: Exception) {
            ApiResponse(error = "Error: ${e.message}")
        }
    }

    suspend fun sendRemoteMessage(message: Notification): Result<Notification> {
        return try {
            val acc = FirebaseAccessToken.getAccessToken()
            val response: HttpResponse = client.post("https://fcm.googleapis.com/v1/projects/walttalkie/messages:send") {
                headers {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
                setBody(message)
                header("Authorization", "Bearer $acc")
            }

            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}