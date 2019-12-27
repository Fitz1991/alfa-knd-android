@file:Suppress("DEPRECATION")

package ru.npc_ksb.alfaknd.app

import android.app.Application
import android.content.res.Resources
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.parcel.Parcelize
import ru.npc_ksb.alfaknd.BuildConfig
import ru.npc_ksb.alfaknd.data.models.MainModel
import timber.log.Timber

@Parcelize
class MyApplication : Application(),Parcelable {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
        AndroidThreeTen.init(this)
        context = this
        resource = this.resources
    }

    companion object {
        lateinit var context: MyApplication
            private set
        lateinit var resource: Resources
            private set

        fun getMainModel(activity: FragmentActivity) : MainModel {
            return ViewModelProviders.of(activity).get(MainModel::class.java)
        }
    }

    inner class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }
}

