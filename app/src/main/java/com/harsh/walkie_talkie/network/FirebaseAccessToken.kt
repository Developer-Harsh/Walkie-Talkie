package com.harsh.walkie_talkie.network

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.harsh.walkie_talkie.util.Constants
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object FirebaseAccessToken {
    fun getAccessToken(): String? {
        try {
            val json = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"walttalkie\",\n" +
                    "  \"private_key_id\": \"558c91392934ed6a09cb5a0b04d7a2e14b895bf4\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC3d1q/cJVpBjsf\\nzx2UFPPH3tMN8/q+6+5o3atgkXb+xjnLs6a3NA30uDtSGzA6kADgQx0G3Qu11twh\\nrYFBooep1GcFClSCNjbQDdX+++wFB7l5sXrkzeXHUjf+BDUi7gRzsROMLg83AeDE\\nmpOPA8+R68iQGxwkjh/XBTCpvtAvRyVdfPHywUHIy6Bidaf/T5CD/YVj0f63mYGv\\np5L+qw/LXprTGNGqEQIi6/YSbt73YeYtCPgVvAp0xkjYhPJuZ0rtV9i7+TwvIGHy\\nBeweFDoC4hTch9PfDaDuA6IdSLSh+FCpMv47MwfL7VCR4hrBAckIughbPWT7HzD9\\nZEHeLbMfAgMBAAECggEAGvznL/8XM25Tty+n6bcVT0LzZ/uKWJhrJyjog1mSmBK/\\nwLvKG+v/245YR5ExTIQPZO3qcK6GgPa8xgV4XJfUfgJ9Lwwgd1ETShKm9o3HX8wN\\nmB2n9s1k7ieljeu/h0ZdCEm1lurVmSFhF3qnlyOX3p7BD5W1U5lGjMI6MNYdWL7N\\n5jkkTwWSd7Mg9AMxVCzmQmUgY4bLbvz/87sRoJmpeTZJs9zEqHbs93Lh/925HJvy\\nlkNioUBJwbdaJFYcWZ+sgL2H2WIPvJ629PLFnlH0ReqB6ZIz8uNceCF5iupNIgkE\\nKlplUJoh/X1HH8iAjNmi6XTfbjSYws8djZiElYJcJQKBgQDvtfifulqtpV70OVLv\\nR51b6fWYlRs1oXVA0cIq9/C6spVaH5536hkPZgMOwfeam6WLWkmO7aKQ3JOSWuIs\\n4AF9lBGy2haj7zrvz/IitbGwOM3xBo1eF3spN/Y+N0KI4w54wrMAcPEx2bXeneQ+\\nGhlGv5bzwrUYOBjeZH/EuCyZkwKBgQDD7vKg2CFI3K4twcEp/WjFNd6xAJr1ERWw\\nk3aBMhIUi7v243MdKMcJUA2Tr+ke4YQ2i0Nqrg57LKYfgmVK8n6nZ4hwcziWt8dK\\nfvj9yMojgRzC8vx6Spxt7QxnMbjlRiphC5hdM3EFV8tTLLWBRKNJjJrU46DS7n93\\n9DmvRQWHxQKBgQDqAFG8W0a5zH1tG6iCPP64AyPk12nAaOT1ac6J84/kMQDrAYLI\\nvJXKGmNRvLfhDaAE8RZtmDzh/PFyJZY1CFdsJ2c/mPYRAjAeenh8ubHDvTAM4cCO\\n4Y0xJ5gSFA+3YS93Y1kqTRam+0g3ERsGJKQYFF9fhesSwXBFaQXxG3kuUQKBgEAY\\nZ9jEO0c5oeUoEAerdV2mPL5Mg2SN/TlxU//5oD6GY6vlRiAQ2eOrA3X/wtQuU/D/\\n58L7Q5N5zLJcFsTIxTQAQ3r9xlJPtmshVFS5HZ81unHvyTY8OCYhuL/9XQ3rwStT\\n/rjmfh+QU9xWsN2j1YLW80OXX2IBUUOtaj1y59c9AoGAXUKw0aAOoiuHD9/SYwtz\\nJFyhlQczBr7wXnb6jh5FN33DXmDwUSeANL/P/61I1UdnYUSyagKQ2pf20cLflsW3\\nMBGqg+WWQGBUQPxtfssufN1BT/xKq/KhTxR+b2KC5rp6tS5yC3zKcjCc89bBRgLf\\np4Gi6cvp5lKVSQW9KPFaeMA=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-fbsvc@walttalkie.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"101370937496401760309\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40walttalkie.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"

            val stream = ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8))
            val googleCreds = GoogleCredentials.fromStream(stream).createScoped(arrayListOf(
                Constants.FCM_SCOPE))
            googleCreds.refresh()

            return googleCreds.accessToken.tokenValue
        } catch (e: IOException) {
            Log.e("TAG", e.message.toString())
            return null
        }
    }
}