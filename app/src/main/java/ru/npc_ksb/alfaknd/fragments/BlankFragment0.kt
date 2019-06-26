package ru.npc_ksb.alfaknd.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import ru.npc_ksb.alfaknd.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ru.npc_ksb.alfaknd.models.*
import java.io.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.npc_ksb.alfaknd.models.AppDatabase
import androidx.room.Room
import com.google.gson.JsonParser

import ru.npc_ksb.alfaknd.Application
import ru.npc_ksb.alfaknd.models.AlfaKndViewModel
import ru.npc_ksb.alfaknd.models.Datum


class BlankFragment0 : Fragment() {
    val TAG : String = "myLog"
    var jsonParser : JsonFileParser? = null
    private lateinit var alfaKndViewModel: AlfaKndViewModel

    companion object {
        const val newAlfaKndActivityRequestCode = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater.inflate(R.layout.fragment_blank0,container,false)
        return view
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        alfaKndViewModel = ViewModelProviders.of(this).get(AlfaKndViewModel::class.java)


        //подписываемся на изменение данных в БД
        alfaKndViewModel.allDatum.observe(this, Observer { datas ->
            // Обновляем данные при изменении БД
            datas?.let {
                for (word in datas)
                    Log.d(TAG, "Произошли изменения")
            }
        })



        jsonParser = JsonFileParser()
        val dataPars: Data = jsonParser!!.getObjectFromJSONFile(context, "response.json")
        val newData : Datum = jsonParser!!.getObjectDatumFromJSONFile(context, "new_data.json")


        alfaKndViewModel.repository.getById(newData.pk!!).observe(this, Observer { data ->
            // Обновляем данные при изменении БД
            data?.let {
                data.compare(newData)
            }
        })


//        for (data in dataPars.data!!.iterator()) {
//            alfaKndViewModel.insert(data)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }


}
