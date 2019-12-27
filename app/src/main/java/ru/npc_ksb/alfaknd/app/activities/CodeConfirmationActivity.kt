package ru.npc_ksb.alfaknd.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.data.preferences.ProfileData
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.pincode
import ru.npc_ksb.pflockscreen.PFFLockScreenConfiguration
import ru.npc_ksb.pflockscreen.fragments.PFLockScreenFragment


class CodeConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userData = ProfileData.ProfilePreference(this)
        val fragment = PFLockScreenFragment()

        val primaryEncodedPinCode = intent.getStringExtra(AuthActivity.ENCODED_PIN_CODE_TAG)

        val builder = PFFLockScreenConfiguration.Builder(this)
                .setUseFingerprint(true)
                .setTitle("Повторите 4-значный код")
                .setAdditionalInfo(true)
                .setMode(PFFLockScreenConfiguration.MODE_AUTH)
        fragment.setConfiguration(builder.build())
        fragment.setEncodedPinCode(primaryEncodedPinCode)
        fragment.setLoginListener(object : PFLockScreenFragment.OnPFLockScreenLoginListener{
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            override fun onCodeInputSuccessful(){
                userData.pincode = primaryEncodedPinCode
                startMainActivity()
            }
            override fun onFingerprintSuccessful(){
                startMainActivity()
            }
            override fun onPinLoginFailed(){
                val toast = Toast.makeText(MyApplication.context, R.string.lock_screen_, Toast.LENGTH_LONG)
                toast.show()
                startAuthActivity()
            }
            override fun onFingerprintLoginFailed(){
                val toast = Toast.makeText(MyApplication.context, R.string.fingerprint_not_recognized_pf, Toast.LENGTH_LONG)
                toast.show()
                startAuthActivity()
            }
        })
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.add(R.id.fragment_container, fragment).commit()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
