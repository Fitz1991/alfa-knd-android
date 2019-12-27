package ru.npc_ksb.alfaknd.app.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import ru.npc_ksb.alfaknd.BuildConfig
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication.Companion.context
import ru.npc_ksb.alfaknd.data.preferences.BindData.BindPreference
import ru.npc_ksb.alfaknd.data.preferences.BindData.host
import ru.npc_ksb.alfaknd.data.preferences.BindData.token
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.ProfilePreference
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.authority
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.authorityGuid
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.dataTimeBlocking
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.email
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.userGuid
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.username
import ru.npc_ksb.alfaknd.domain.requests.NetworkService
import ru.npc_ksb.alfaknd.domain.requests.pojo.AuthQRResponse
import ru.npc_ksb.alfaknd.utils.localDateTimeInMillis


class BindActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var btnScan: Button
    lateinit var txtInfo: TextView

    private val userData = ProfilePreference(context)
    private val bindPreference = BindPreference(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind)

        progressBar = this.findViewById(R.id.progressBar)
        btnScan = this.findViewById(R.id.btnScan)
        txtInfo = this.findViewById(R.id.textInfo)

        if(userData.dataTimeBlocking.isNotEmpty()) {

            val localDataTimeBlocking = LocalDateTime.parse(userData.dataTimeBlocking)

            if (localDataTimeBlocking < LocalDateTime.now()) {
                onCheckTokenForEmptiness()
            } else {
                val timeDifference = localDateTimeInMillis(localDataTimeBlocking) - localDateTimeInMillis(LocalDateTime.now())

                btnScan.visibility = View.INVISIBLE
                txtInfo.visibility = View.INVISIBLE

                val time =
                        if ((timeDifference/1000/60) >= 1){
                            "когда пройдет " + (timeDifference/1000/60).toString()+" мин"
                        }else "через пару секунд"

                val builderBlocking = AlertDialog.Builder(this)
                        .setTitle(R.string.app_block)
                        .setMessage("Попытайтесь снова, $time")
                        .setCancelable(false)
                        .setNegativeButton("Ок") { _: DialogInterface, _: Int -> finishAndRemoveTask() }
                val dialog = builderBlocking.create()
                dialog.show()
            }
        }else{
            onCheckTokenForEmptiness()
        }

        btnScan.setOnClickListener {
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
    }

    private fun onCheckTokenForEmptiness(){
        if (bindPreference.token.isNotEmpty() && bindPreference.host.isNotEmpty()) {
            startAuthActivity()
        }
    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Suppress("DEPRECATION")
    private fun progressStart() {
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), PorterDuff.Mode.SRC_ATOP)
        progressBar.visibility = View.VISIBLE
        btnScan.visibility = View.GONE
        txtInfo.text = resources.getString(R.string.auth_process)
    }

    private fun progressStop() {
        progressBar.visibility = View.GONE
        btnScan.visibility = View.VISIBLE
        txtInfo.text = resources.getString(R.string.auth_info)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        val context = this

        when (requestCode) {
            QRCodeActivity.RESULT_CODE -> {
                when (resultCode) {
                    QRCodeActivity.RESULT_OK -> {
                        val resultText = intentData!!.getStringExtra("result")
                        val result = Gson().fromJson<AuthQRResponse>(resultText, AuthQRResponse::class.java)

                        bindPreference.token = result.token
                        bindPreference.host = result.host

                        progressStart()
                        Handler().postDelayed({
                            NetworkService.instance.getDeviceApi().bindComplete()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        userData.username = it.userFio
                                        userData.userGuid = it.userGuid
                                        userData.email = it.userEmail
                                        userData.authority = it.authorityName
                                        userData.authorityGuid = it.authorityGuid
                                        startAuthActivity()
                                    }, {
                                        progressStop()
                                        bindPreference.token = ""
                                        bindPreference.host = ""
                                        Toast.makeText(context, "Ошибка авторизации", Toast.LENGTH_LONG).show()
                                    })
                        }, 1000)
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
}