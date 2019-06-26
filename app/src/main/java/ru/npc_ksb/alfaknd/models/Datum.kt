package ru.npc_ksb.alfaknd.models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity
class Datum{
    @PrimaryKey
    var id : Long?=null

    @SerializedName("pk")
    @Expose
    var pk: Long? = null
    @SerializedName("__str__")
    @Expose
    var str: String? = null
    @SerializedName("inn")
    @Expose
    var inn: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("ogrn")
    @Expose
    var ogrn: String? = null
    @SerializedName("subject_type")
    @Expose
    var subjectType: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("change_number")
    @Expose
    var changeNumber: Int? = null

    fun compare(newDatum: Datum?) {
//        val javers  = JaversBuilder.javers().build()
//        val diff : Diff  = javers!!.compare(this, newDatum)
//        val valChange : ValueChange = diff.getChangesByType(ValueChange::class.java).get(0)
        Log.d("myLog", "asdas")
    }
}