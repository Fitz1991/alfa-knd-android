package ru.npc_ksb.alfaknd.presentation_layer.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.npc_ksb.alfaknd.R
import androidx.lifecycle.ViewModelProviders
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers

import ru.npc_ksb.alfaknd.data_layer.cache.serializer.JsonFileParser

import ru.npc_ksb.alfaknd.data_layer.repository.datasource.AlfaKndViewModel
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility
import ru.npc_ksb.alfaknd.presentation_layer.presenter.InspectionsPresentor
import ru.npc_ksb.alfaknd.presentation_layer.presenter.InspectionsPresentorImpl


class InspectionsF : Fragment(),InspectionsFView {


    private var ispectionPresentor: InspectionsPresentor?=null
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
        jsonParser = JsonFileParser()
        ispectionPresentor = InspectionsPresentorImpl(this, this, context)
        alfaKndViewModel = ViewModelProviders.of(this).get(AlfaKndViewModel::class.java)

        //Выводим все данные из таблицы datum
        //Получаем LiveData данные из репозитория(LiveData<List<Datum>>) и подписываемся на них

                //вывести в отдельный класс, привяхзать к OnAttach
//        alfaKndViewModel.allTypesOfResponsibility
//                .observe(this, Observer { datas ->
//            // Обновляем данные при изменении БД
//                    try {
//                        datas?.let {
//                            Log.d(TAG, "ВСЕ ДАННЫЕ ИЗ ТАБЛИЦЫ DATUM :")
//                            for (datum in it) {
//                                Log.d(TAG, "${jsonParser!!.convertToJson(datum)}")
//                            }
//                        }
//                    } catch (e: NullPointerException) {
//                        Log.d(TAG, e.message)
//                    }
//
//        })



//        jsonParser = JsonFileParser()
//        //парсим все данные из Json файла
//
//                // вынести в domain_layer
//        val dataPars: TypesOfResponsibility = jsonParser!!.getObjectFromJSONFile(context, "response.json")
//                // вынести в domain_layer
//        //парсим новые данные, которые хотим вставить в БД
//        val newData: Datum = jsonParser!!.getObjectDatumFromJSONFile(context, "new_data.json")

        //вставляем в БД
        //insertNewData(newData)
        synchronize(context)
    }

    fun synchronize(context: Context) {
        ispectionPresentor?.requestSync(context)
    }

    override fun showInsertedData(insertedDatas: Single<MutableList<TypesOfResponsibility>>?) {
        if (insertedDatas != null) {
            insertedDatas
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it != null) {
                            Log.d(TAG, "НОВЫЕ ДАННЫЕ:")
                            for (it in it) {
                                   Log.d(TAG, jsonParser!!.convertToJson(it))
                            }
                        } else Log.d(TAG, "Данных для обновления нет")

                    },{
                        Log.d(TAG, it.message)
                    })
        }else{
            Log.d(TAG, "Новых данных нет")
        }

    }



    override fun showUpdatedData(updatedDatas: Single<MutableList<TypesOfResponsibility>>?) {
        if (updatedDatas != null) {
            updatedDatas
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d(TAG, "ОБНОВОЛЕННЫЕ ДАННЫЕ:")
                        for (it in it) {
                            Log.d(TAG, jsonParser!!.convertToJson(it))
                        }
                    },{
                        Log.d(TAG, it.message)
                    })
        } else {
            Log.d(TAG, "Данных для обновления нет")
        }
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
