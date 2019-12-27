package ru.npc_ksb.alfaknd.app.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.npc_ksb.alfaknd.data.preferences.ProfileData
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.pincode
import ru.npc_ksb.alfaknd.data.preferences.ProfileData.dataTimeBlocking
import ru.npc_ksb.pflockscreen.PFFLockScreenConfiguration
import ru.npc_ksb.pflockscreen.fragments.PFLockScreenFragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import org.threeten.bp.LocalDateTime
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication.Companion.context



class AuthActivity : AppCompatActivity() {

    companion object{
        const val ENCODED_PIN_CODE_TAG = "EncodedPinCode"
        const val COUNTER_BLOCKING = 3
        const val CHECK_COUNTER = "CheckCounter"
    }
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userData = ProfileData.ProfilePreference(this)
        val fragment = PFLockScreenFragment()

        if (userData.pincode.isNotEmpty()){
            val  builder = PFFLockScreenConfiguration.Builder(this)
                    .setUseFingerprint(true)
                    .setMode(PFFLockScreenConfiguration.MODE_AUTH)
                    .setTitle("Введите код для входа")
                    .setLeftButton("Забыли?")
                    .setAdditionalInfo(false)
            fragment.setOnLeftButtonClickListener {
                startResetPasswordActivity()
            }
            fragment.setConfiguration(builder.build())
            fragment.setEncodedPinCode(userData.pincode)
            fragment.setLoginListener(object : PFLockScreenFragment.OnPFLockScreenLoginListener{
                override fun onCodeInputSuccessful(){
                    userData.dataTimeBlocking = ""
                    startMainActivity()
                }
                override fun onFingerprintSuccessful(){
                    startMainActivity()
                }
                override fun onPinLoginFailed(){
                    counter += 1
                    if (counter == 1){
                        val toast = Toast.makeText(context, R.string.number_attempts_available, Toast.LENGTH_SHORT)
                        toast.show()
                    }
                    if (counter > COUNTER_BLOCKING){
                        val builderBlocking = AlertDialog.Builder(this@AuthActivity)
                                .setTitle(R.string.app_block)
                                .setMessage("Попытайтесь снова, когда пройдет 3 мин")
                                .setCancelable(false)
                                .setNegativeButton("Ок") { _: DialogInterface, _: Int -> finishAndRemoveTask() }
                        val dialog = builderBlocking.create()
                        dialog.show()

                        userData.dataTimeBlocking =  LocalDateTime.now().plusMinutes(3).toString()
                    }
                }
                override fun onFingerprintLoginFailed(){
                }
            })
        }
        else{
            val builder = PFFLockScreenConfiguration.Builder(this)
                    .setUseFingerprint(true)
                    .setMode(PFFLockScreenConfiguration.MODE_CREATE)
                    .setAdditionalInfo(true)
            fragment.setConfiguration(builder.build())
            fragment.setCodeCreateListener {
                startCodeConfirmationActivity(it)
            }
        }
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.add(R.id.fragment_container, fragment).commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CHECK_COUNTER, counter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        counter = savedInstanceState.getInt(CHECK_COUNTER)
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startCodeConfirmationActivity(encodedPinCode: String) {
        val intent = Intent(this, CodeConfirmationActivity::class.java)
        intent.putExtra(ENCODED_PIN_CODE_TAG, encodedPinCode)
        startActivity(intent)
        finish()
    }

    private fun startResetPasswordActivity() {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }
}