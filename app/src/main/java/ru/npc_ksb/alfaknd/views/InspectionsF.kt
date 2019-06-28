package ru.npc_ksb.alfaknd.views

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
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import ru.npc_ksb.alfaknd.models.AlfaKndViewModel
import ru.npc_ksb.alfaknd.models.Datum


class InspectionsF : Fragment() {
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
        val view = inflater.inflate(R.layout.f_inspections,container,false)
        return view
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        alfaKndViewModel = ViewModelProviders.of(this).get(AlfaKndViewModel::class.java)


        //Выводим все данные из таблицы datum
        alfaKndViewModel.allDatum.observe(this, Observer { datas ->
            // Обновляем данные при изменении БД
            datas?.let {
                for (datum in datas)
                    Log.d(TAG, "Все данные из таблицы datum : ${jsonParser!!.convertToJson(datum)}")
            }
        })

        jsonParser = JsonFileParser()
        //парсим все данные из Json файла
        val dataPars: Data = jsonParser!!.getObjectFromJSONFile(context, "response.json")

        //парсим новые данные, которые хотим вставить в БД
        val newData : Datum = jsonParser!!.getObjectDatumFromJSONFile(context, "new_data.json")

        //вставляем в БД
        insertNewData(newData)
    }

    fun insertNewData(newData: Datum) {
        //проверяем есть ли уже данные в БД с newData.pk
        val dataOld = alfaKndViewModel.repository.getById(newData.pk!!)
        dataOld
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:DisposableSingleObserver<Datum>() {
                override fun onSuccess(datum:Datum) {
                    val data = datum

                    val isEquals = data.compare(newData)
                    //если данные идентичны
                    if (isEquals == 0) {
                        Log.d(TAG, "Обновлять нечего")
                    }
                    //если не идентичны, то обновляем
                    else{
                        //делаем метод update отслеживаемым, обновляем данные в БД
                        val updatedDatumId : Flowable<Int> = Flowable.just(alfaKndViewModel.repository.update(newData))
                            //подписываемся на обновленные данные
                        updatedDatumId
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                            //получаем обновленные данные
                            alfaKndViewModel.repository.getById(it.toLong())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object:DisposableSingleObserver<Datum>() {
                                    override fun onSuccess(updatedData:Datum) {
                                        Log.d(TAG, "Состояние строки с pk ${data.pk} до: ${jsonParser!!.convertToJson(data)}")
                                        Log.d(TAG, "Состояние строки с pk ${updatedData.pk} после: ${jsonParser!!.convertToJson(updatedData)}")

                                    }
                                    override fun onError(e:Throwable) {

                                    }
                                })
                        }, {
                            Log.d(TAG, it.message)
                        })
                    }
                }
                override fun onError(e:Throwable) {
                    val insertedDatumId = Flowable.just(alfaKndViewModel.repository.insert(newData))
                    insertedDatumId
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({it ->
//                            Log.d(TAG, "Данных с pk ${newData.pk} не существует записываем в БД..")
                            alfaKndViewModel.repository.getById(it)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object:DisposableSingleObserver<Datum>() {
                                    override fun onSuccess(addedData:Datum) {
                                        Log.d(TAG, "Новые данные объекта с pk ${addedData.pk}: ${jsonParser!!.convertToJson(addedData)}")
                                    }
                                    override fun onError(e:Throwable) {

                                    }
                                })
                        },
                            {
                                Log.d(TAG, it.message)
                            })
                }
            })
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
