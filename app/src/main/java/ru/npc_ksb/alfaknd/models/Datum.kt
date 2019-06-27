package ru.npc_ksb.alfaknd.models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity
class Datum{
    @PrimaryKey
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

    fun compare(newDatum: Datum?) : Int {
        val comparator = compareBy(
            Datum::pk,
            Datum::str,
            Datum::inn,
            Datum::address,
            Datum::ogrn,
            Datum::subjectType,
            Datum::name,
            Datum::changeNumber)

        return comparator.compare(this, newDatum)
    }
}