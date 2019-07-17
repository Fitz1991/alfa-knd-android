package ru.npc_ksb.alfaknd.data_layer.cache.serializer

import android.util.Log
import com.fernandocejas.android10.sample.data.cache.FileManager
import com.google.gson.GsonBuilder
import ru.npc_ksb.alfaknd.presentation_layer.model.TypesOfResponsibility
import ru.npc_ksb.alfaknd.presentation_layer.model.Datum
import ru.npc_ksb.alfaknd.presentation_layer.view.MainApp
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility as DataLayerTypesOfResponsibility

class JsonFileParser {
    val TAG : String = "myLog"

    var builder = GsonBuilder()

    var gson = builder.create()

    var fileManager = FileManager()

    var context = MainApp.applicationContext()

    private fun getJSONStringFromAssetFile(jsonFile:String): String {
        val am  = context.assets
        val `is` = am.open(jsonFile)
        val responseJSON = convertStreamToString(`is`)
        return responseJSON
    }

    private fun getObjectFromJSONFile(jsonFile: String) : TypesOfResponsibility {
        var dataPars = gson.fromJson(getJSONStringFromAssetFile(jsonFile), TypesOfResponsibility::class.java)
        return dataPars
    }

    fun getObjectDatumFromJSONFile(newJSONFile: String) : Datum {
        var dataPars = gson.fromJson(getJSONStringFromAssetFile(newJSONFile), Datum::class.java)
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


    fun convertToJson(typesOfResponsibility: DataLayerTypesOfResponsibility) : String{
        return gson.toJson(typesOfResponsibility)
    }



    fun getDatums(path:String) : TypesOfResponsibility {
        return getObjectFromJSONFile(path)
    }
}