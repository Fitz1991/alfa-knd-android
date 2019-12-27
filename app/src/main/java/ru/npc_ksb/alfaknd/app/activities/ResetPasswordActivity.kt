package ru.npc_ksb.alfaknd.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import ru.npc_ksb.alfaknd.BuildConfig
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.pincode
import ru.npc_ksb.alfaknd.data.preferences.BindData
import ru.npc_ksb.alfaknd.data.preferences.BindData.host
import ru.npc_ksb.alfaknd.data.preferences.BindData.token
import ru.npc_ksb.alfaknd.data.preferences.ProfileData
import ru.npc_ksb.alfaknd.domain.requests.pojo.AuthQRResponse

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        if (BuildConfig.AUTH_DATA.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra("result", BuildConfig.AUTH_DATA)
            setResult(QRCodeActivity.RESULT_OK, intent)
            onActivityResult(QRCodeActivity.RESULT_CODE, QRCodeActivity.RESULT_OK, intent)
        } else {
            val intent = Intent(this, QRCodeActivity::class.java)
            startActivityForResult(intent, QRCodeActivity.RESULT_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        val context = this

        when (requestCode) {
            QRCodeActivity.RESULT_CODE -> {
                when (resultCode) {
                    QRCodeActivity.RESULT_OK -> {
                        val resultText = intentData!!.getStringExtra("result")
                        val result = Gson().fromJson(resultText, AuthQRResponse::class.java) as AuthQRResponse

                        val userData = ProfileData.ProfilePreference(context)
                        val bindPreference = BindData.BindPreference(context)

                        if (bindPreference.token == result.token && bindPreference.host == result.host) {
                            userData.pincode = ""
                            startAuthActivity()
                        }
                        else{
                            Toast.makeText(this, "Не верный QR-код", Toast.LENGTH_LONG).show()
                            starResetPasswordActivity()
                        }
                    }
                    QRCodeActivity.RESULT_CANCEL -> {
                    }
                    else -> {
                        Toast.makeText(this, "Ошибка сканирования", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun starResetPasswordActivity() {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }
}
