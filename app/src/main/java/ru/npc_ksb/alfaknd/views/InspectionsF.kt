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
import androidx.lifecycle.LiveDataReactiveStreams
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
import kotlinx.coroutines.cancelAndJoin
import org.intellij.lang.annotations.Flow

import ru.npc_ksb.alfaknd.models.AlfaKndViewModel
import ru.npc_ksb.alfaknd.models.Datum


class InspectionsF : Fragment() {
    val TAG: String = "myLog"
    var jsonParser: JsonFileParser? = null
    private lateinit var alfaKndViewModel: AlfaKndViewModel

    companion object {
        const val newAlfaKndActivityRequestCode = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater.inflate(R.layout.f_inspections, container, false)
        return view
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        alfaKndViewModel = ViewModelProviders.of(this).get(AlfaKndViewModel::class.java)


        //Выводим все данные из таблицы datum
        //Получаем LiveData данные из репозитория(LiveData<List<Datum>>) и подписываемся на них
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
        val newData: Datum = jsonParser!!.getObjectDatumFromJSONFile(context, "new_data.json")

        //вставляем в БД
        insertNewData(newData)
    }

    fun insertNewData(newData: Datum) {
        //проверяем есть ли уже данные в БД с newData.pk
        val liveDataDatum : LiveData<Datum> = alfaKndViewModel.repository.getById(newData.pk!!)
        val flowableDataDatum: Flowable<Datum> = Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(this, liveDataDatum))
        flowableDataDatum
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //если есть данные в БД с newData.pk
                        {
                            val data = it
                            val isEquals = data.compare(newData)
                            //если данные идентичны
                            if (isEquals == 0) {
                                Log.d(TAG, "Обновлять нечего")
                            }
                            //если не идентичны, то обновляем
                            else {
                                //делаем метод update наблюдаемым, обновляем данные в БД
                                val flowableDataUpdated : Flowable<Int> = alfaKndViewModel.repository.update(newData)
                                flowableDataUpdated
                                //подписываемся на обновленные данные
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            //получаем обновленные данные
                                            val liveDataDatum : LiveData<Datum> = alfaKndViewModel.repository.getById(it)
                                            val flowableData: Flowable<Datum> = Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(this, liveDataDatum))
                                            flowableData
                                                    .subscribeOn(Schedulers.newThread())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe({
                                                        Log.d(TAG, "Состояние строки с pk ${data.pk} до: ${jsonParser!!.convertToJson(data)}")
                                                            Log.d(TAG, "Состояние строки с pk ${it.pk} после: ${jsonParser!!.convertToJson(it)}")
                                                    }, {
                                                        Log.d(TAG, it.message)
                                                    })
                                        }, {
                                            Log.d(TAG, it.message)
                                        })
                            }
                        },
                        {
                            //если не нашли данные с newData.pk
                            val flowableDataDatumId : Flowable<Long> = alfaKndViewModel.repository.insert(newData)
                                flowableDataDatumId
//                                    .subscribeOn(Schedulers.newThread())
//                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        Log.d(TAG, "Данных с pk ${newData.pk} не существует записываем в БД..")
                                        val liveDataDatum : LiveData<Datum> = alfaKndViewModel.repository.getById(it.toInt())
                                        val flowableDataDatum: Flowable<Datum> = Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(this, liveDataDatum))
                                        flowableDataDatum
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(
                                                        {
                                                            Log.d(TAG, "Новые данные объекта с pk ${it.pk}: ${jsonParser!!.convertToJson(it)}")
                                                        }, {

                                                }
                                                )},
                                            {
                                                Log.d(TAG, it.message)
                                            })
                        }
                )
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
