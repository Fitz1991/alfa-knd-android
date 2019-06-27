package ru.npc_ksb.alfaknd.models

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

class JsonFileParser {
    val TAG : String = "myLog"

    var builder = GsonBuilder()
    var gson = builder.create()

    fun getJSONStringFromAssetFile(context: Context, jsonFile:String): String {
        val am = context.assets
        val `is` = am.open(jsonFile)
        val responseJSON = convertStreamToString(`is`)
        return responseJSON
    }

    fun getObjectFromJSONFile(context: Context, jsonFile: String) : Data{
        var dataPars = gson.fromJson(getJSONStringFromAssetFile(context, jsonFile), Data::class.java)
        return dataPars
    }

    fun getObjectDatumFromJSONFile(context: Context, newJSONFile: String) : Datum{
        var dataPars = gson.fromJson(getJSONStringFromAssetFile(context, newJSONFile), Datum::class.java)
        return dataPars
    }

    private fun convertStreamToString(`is`: InputStream): String {
        var oas: ByteArrayOutputStream = ByteArrayOutputStream()
        copyStream(`is`, oas)
        val t = oas!!.toString()
        oas!!.close()
        return t
    }

    private fun copyStream(`is`: InputStream, os: OutputStream) {
        val buffer_size = 1024
        try {
            val bytes = ByteArray(buffer_size)
            while (true) {
                val count = `is`.read(bytes, 0, buffer_size)
                if (count == -1)
                    break
                os.write(bytes, 0, count)
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.message)
        }
    }

    fun convertToJson(datum:Datum) : String{
        return gson.toJson(datum)
    }
}