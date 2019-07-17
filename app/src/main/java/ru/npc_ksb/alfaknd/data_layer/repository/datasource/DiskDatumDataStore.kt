package ru.npc_ksb.alfaknd.data_layer.repository.datasource

import android.app.Application
import android.content.Context
import com.google.common.collect.Sets
import io.reactivex.Completable
import io.reactivex.Observable
import ru.npc_ksb.alfaknd.data_layer.cache.serializer.JsonFileParser
import ru.npc_ksb.alfaknd.presentation_layer.model.ActualData
import ru.npc_ksb.alfaknd.presentation_layer.model.ChangedDatum
import ru.npc_ksb.alfaknd.presentation_layer.model.Datum as PresentationLayerTypesOfResponsibility
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility as DataLayerTypesOfResponsibility
import ru.npc_ksb.alfaknd.presentation_layer.model.Field
import ru.npc_ksb.alfaknd.data_layer.repository.AlfaKndRepository
import ru.npc_ksb.alfaknd.domain_layer.Fields
import ru.npc_ksb.alfaknd.presentation_layer.view.MainApp
import java.lang.Exception

class DiskDatumDataStore(repository: AlfaKndRepository) : DatumDataStore {
    val TAG: String = "mylog"
    /**
     * Для запросов к БД*/
    private var repository: AlfaKndRepository
    /**
     * Для парсинга json в коллекцию объектов*/
    var jsonParser: JsonFileParser? = null
    /**
     * измененные datums для обновления локальных*/
    var remoteDatums: Observable<List<PresentationLayerTypesOfResponsibility>>? = null
    /**
     * datums для обновления*/
    var localDatums: Observable<List<DataLayerTypesOfResponsibility>>? = null
    /**
     * Для соответствия datym.{field} -> label*/
    var fieldsForDatum: Observable<List<Field>?>? = null
    /**
     * Для работы с gson*/
    var context: Context? = MainApp.applicationContext()

    /**
     * Дата последней обновленной записи*/
    var lastUpdated: Observable<ActualData>? = null


    lateinit var dataToSync: MutableMap<String, MutableSet<ChangedDatum>>

    /**
    коллекция данных для вставки
     */
    lateinit private var dataToInsert: List<PresentationLayerTypesOfResponsibility>

    /**
    коллекция данных для обновления
     */
    lateinit private var dataToUpdate: List<DataLayerTypesOfResponsibility>

    /**
     * Коллекция соответствия */
    lateinit private var fieldValueMatches: MutableMap<String, String>

    /**
     * Список удаленных кандидатов для обновления*/
    private var datumsForUpdateRemote: MutableSet<PresentationLayerTypesOfResponsibility> = mutableSetOf<PresentationLayerTypesOfResponsibility>()

    /**
     * Список локальных кандидатов для обновления*/
    private var datumsForUpdateLocal: MutableSet<DataLayerTypesOfResponsibility> = mutableSetOf<DataLayerTypesOfResponsibility>()
    /**
     * Список удаленных кандидатов для вставки*/
    private var candidadsToInsertRemote: MutableSet<PresentationLayerTypesOfResponsibility> = mutableSetOf<PresentationLayerTypesOfResponsibility>()
    /**
     * Список локальных кандидатов для вставки*/
    private var candidadsToInsertlocal: MutableSet<DataLayerTypesOfResponsibility> = mutableSetOf<DataLayerTypesOfResponsibility>()
    /**
     * mismatchData - несовпадающие данные
     * MutableMap<Int, MutableMap<String, MutableMap<String, MutableMap<String, String>>>> mismatchedFieldsWithValues
     * - список несовпадающих данных
     * Int - произврольный индекс
     * MutableMap<String, MutableMap<String, MutableMap<String, String>>> - список
     * MutableMap<String, MutableMap<String, MutableMap<String, String>>>
     * */
    var mismatchData: MutableSet<MutableMap<String, MutableMap<String, MutableMap<String, String>>>> = mutableSetOf()

    //несовпадающие поля
    /**
    Список c ключем candidateToUpdateLocal.name и значением с информацией о различающихся полях differentValOfRemoteAndLocalRepository, где
    String - это candidateToUpdateLocal.name
    MutableMap<String, String> - differentValOfRemoteAndLocalRepository - Информация о раpличающихся полях и название поля на кириллице
     */
    var mismatchedFieldsWithValues: MutableMap<Enum<Fields>, MutableMap<Enum<Fields>, String>> = mutableMapOf()


    /**
     * Информация о раpличающихся полях, какое значение поля в локальном репозитории localName и
     * какое значение в удаленном репозитории remoteName
     * Первый String - это ключ или local{Field} или remote{Field}, где Field - Это название поля
     * Второй String - это значение candidateToUpdateLocal.{field} или candidateToUpdateRemote.{field}
     * Первый String "label"
     * Второй String - название поля fieldValueMatches.get("{field}"), где field - ключ объекта datum,
    например fieldValueMatches.get("__str__") вернет в label "Наимееннование"
     *
     * */
    var differentValOfRemoteAndLocalRepository: MutableMap<Enum<Fields>, String> = mutableMapOf()

    /**
     * Локальные Uuid'ды для получения разницы и пересечения uuid'ов*/
    var localIds: MutableSet<Int>? = null

    /**
     * Удаленные Uuid'ды для получения разницы и пересечения uuid'ов*/
    var remoteIds: MutableSet<Int>? = null

    /**
     * Разница uuid'ов в сторону удаленных данных*/
    var differenceRemoteIds: MutableSet<Int>? = null

    /**
     * Разница uuid'ов в сторону локальных данных*/
    var differenceLocalIds: MutableSet<Int>? = null

    /**
     * Пересечение uuid'ов*/
    var intersectionIds: MutableSet<Int>? = null

    /**
     * Для работы с наименованием полей*/
    var field = Field()

    /**
     * Список объектов ChangedDatum, где
     * changedDatums.localTypesOfResponsibility - локальные объекты для обновления
     * changedDatums.remoteTypesOfResponsibility - remote объекты для обновления
     * */
    override var changedDatums = mutableSetOf<ChangedDatum>()


    init {
        this.jsonParser = JsonFileParser()
        this.context = Application()
        this.lastUpdated = getLastUpdatedDate()
        this.repository = repository
    }



    /**
     * Получение изменившихся удаленных datum
     * @param lastUpdated самая свежая дата записи
     * @param part сколько получить записей за раз
     * @return изменившиеся удаленные datum
     * */


    override fun getChangedRemoteData(actualData: Observable<ActualData>, part: Long): Observable<List<PresentationLayerTypesOfResponsibility>>? {
        try {
            remoteDatums = Observable.just(jsonParser!!.getDatums("remote_response.json").data)
        } catch (e: Exception) {
            e.stackTrace
        }
        return remoteDatums
    }


    /**
     * Получение удаленных field : label для соответствия*/
    override fun getRemoteFields(): Observable<List<Field>?>? {
        fieldsForDatum = Observable.just(jsonParser!!.getDatums("remote_response.json").meta!!.fields)
        return fieldsForDatum
    }

    /**
     * */
    override fun sync() {
        //находим свежие remote данные
        remoteDatums = getChangedRemoteData(lastUpdated!!, 1000)!!
        localDatums = remoteDatums!!.map {
             getIdsInDatumsRemote(it)
        }.map {
            it.map {
                it.toLong()
            }
        }.flatMap {
            getOldLocalData(it)
        }

        /**
         * Сравниваем, находим находим кандидатов на обновление, вставку и удаление*/
        compare(localDatums!!, remoteDatums!!, context!!)

        /**
         * Идентифицируем список на обновление*/
        identifyDatumsForUpdate(datumsForUpdateLocal, datumsForUpdateRemote)

    }



    /**
     * Получение устаревшего списка datum при помощи измененных uuid со стороны сервера
     * @param changedDatums список измененных uuid со стороны сервера
     * @return список datum c устаревшими записями
     * */
    override fun getOldLocalData(remoteDatumsIds: List<Long>): Observable<List<DataLayerTypesOfResponsibility>> {
        return Observable.fromCallable { repository.getOldDatum(remoteDatumsIds) }

        //        localDatums = Observable.just(jsonParser!!.getDatums(context!!, "local_response.json").typesOfResponsibilities)
    }


//    override fun getChangedDatums(): MutableSet<ChangedDatum> {
//        return this.changedDatums
//    }

    /**
     * Получить дату последнего обновления для выборки свежих данных с remote*/
    override fun getLastUpdatedDate(): Observable<ActualData> {
        return Observable.fromCallable { repository.getLastUpdatedData() }
    }

    /**
     * Установ*/
    override fun setLastUpdatedData(lastUpdate: org.threeten.bp.LocalDateTime): Completable {
        val actualData = ActualData(lastUpdate)
        return Completable.fromAction { repository.updateLastUpdatedData(actualData) }
    }

    fun compare(localDatums: Observable<List<DataLayerTypesOfResponsibility>>, remoteDatums: Observable<List<PresentationLayerTypesOfResponsibility>>, context: Context) {
        val jsonParser = JsonFileParser()

        dataToInsert = jsonParser.getDatums("data_to_insert.json").data!!
//        dataToUpdate = jsonParser.getDatums(context, "data_to_update.json").typesOfResponsibilities!!

//        Находим коллекцию localDatumsIds по списку datum
        var localDatumsIds = localDatums.map {
            getIdsInDatumsLocal(it)
        }
//        Находим коллекцию remoteDatumsIds по списку datum
        var remoteDatumsIds = remoteDatums.map {
            getIdsInDatumsRemote(it)
        }

        //находим пересечение коллекций, тк некторых новых записей из remote может и не быть в local
        localDatumsIds.map {
            //            findIntersectionDatum(it, remoteDatums)
            val local = it
            remoteDatumsIds.map {
                val remote = it
                //initial - intersectionIds
                findIntersectionDatum(local, remote)
            }.map {
                it.toList()
            }
        }

        //находим разность коллекций со стороны local, записи, которые не были найдены в remote
        localDatumsIds.map {
            val local = it
            remoteDatumsIds.map {
                val remote = it
                findDifferenceDatumLocal(local, remote)
            }.map {
                it.toList()
            }
        }
        //находим разность коллекций со стороны remote, те записи, которые не были найдены в local
        remoteDatumsIds.map {
            val remote = it
            localDatumsIds.map {
                val local = it
                //записи, которые не были найдены в local
                findDifferenceDatumRemote(local, remote)
            }.map {
                it.toList()
            }
        }

        //находим объекты datumsForUpdateRemote по коллекции- кандидаты на обновление
        remoteDatums.map {
            datumsForUpdateRemote = findDatumObjectsByIdRemote(intersectionIds!!, it.toList()!!)
        }

        //находим объекты datumsForUpdateLocal по коллекции- кандидаты на обновление
        localDatums.map {
            datumsForUpdateLocal = findDatumObjectsByIdLocal(intersectionIds!!, it.toList()!!)
        }
    }

    /**
    Если поля с local и remote не совпадают, например localObj.inn != candidatToUpdateRemote.inn. то  добавляем в коллекцию несовпадающих данных mismatchData.get(pk)!!.put(fieldValueMatches["inn"], candidatToUpdateRemote.inn!!),
    где fieldValueMatches коллекция соответствия ключа его названию на кириллице например: inn -> ИНН
     */
    private fun identifyDatumsForUpdate(datumsForUpdateLocal: MutableSet<DataLayerTypesOfResponsibility>, datumsForUpdateRemote: MutableSet<PresentationLayerTypesOfResponsibility>) {
        for (datumForUpdateLocal in datumsForUpdateLocal) {
            for (datumForUpdateRemote in datumsForUpdateRemote) {
                //если ключи равны, сравниваем объекты
                when {
                    (datumForUpdateRemote.uuid == datumForUpdateLocal.uuid) -> {
                        val uuid = datumForUpdateLocal.uuid
                        when {
                            (datumForUpdateLocal.name != datumForUpdateRemote.name) -> {
                                var localName: String = datumForUpdateLocal.name!!
                                var remoteName: String = datumForUpdateRemote.name!!
                                var label = Observable.fromCallable { repository.getLabel("name") }
                                //
                                differentValOfRemoteAndLocalRepository?.put(Fields.LOCAL_NAME, localName)
                                differentValOfRemoteAndLocalRepository?.put(Fields.REMOTE_NAME, remoteName)
                                label.map {
                                    differentValOfRemoteAndLocalRepository?.put(Fields.LABEL, it)
                                }
                                /**
                                 *
                                 * */
                                mismatchedFieldsWithValues?.put(Fields.NAME, differentValOfRemoteAndLocalRepository)
                                /**
                                 * @param mismatchedFieldsWithValues - коллекция в которой ключ это название поля,
                                 * а значение это какие значению у remote и local данных
                                 * */
                                var mismatchData: MutableSet<MutableMap<String, MutableMap<String, MutableMap<String, String>>>> = mutableSetOf()

                                mismatchData.add(mismatchedFieldsWithValues as MutableMap<String, MutableMap<String, MutableMap<String, String>>>)

                                /*
                                * */
                                var changedDatum = ChangedDatum(datumForUpdateLocal, datumForUpdateRemote, mismatchData)
                                changedDatums.add(changedDatum)
                            }


                            /* (candidateToUpdateLocal.str != candidateToUpdateRemote.str) -> {
                                 var localStr : String = candidateToUpdateLocal.str!!
                                 var remoteStr: String = candidateToUpdateRemote.str!!
                                 var label: String = fieldValueMatches.get("__str__")!!
                                 differentValOfRemoteAndLocalRepository?.put("localName", localStr)
                                 differentValOfRemoteAndLocalRepository?.put("remoteName", remoteStr)
                                 mismatchedFieldsWithValues?.put(label, differentValOfRemoteAndLocalRepository)
                                 mismatchData.put(pk!!, mismatchedFieldsWithValues as MutableMap<String, MutableMap<String, MutableMap<String, String>>>)
                                 var changedDatum = ChangedDatum(candidateToUpdateLocal, candidateToUpdateRemote, mismatchData.get(pk)!!)
                                 changedDatums.add(changedDatum)
                             }*/
                        }

                    }
                }
            }
        }
    }

    /**
     * преобразование коллекции с id в коллекцию объектов
     * @param datumIds - колекция c uuid'ами bp из которых нужно сделать коллекию datum
     * @param typesOfResponsibilityCollections - коллекция из которой будут выбираться datum по uuid
     * */

    fun findDatumObjectsByIdLocal(datumIds: MutableSet<Int>, typesOfResponsibilityCollections: List<DataLayerTypesOfResponsibility>): MutableSet<DataLayerTypesOfResponsibility> {
        var datumObjectsById = mutableSetOf<DataLayerTypesOfResponsibility>()
        for (id in datumIds) {
            datumCollections@ for (datum in typesOfResponsibilityCollections) {
                when {
                    (datum.uuid == id) -> {
                        datumObjectsById.add(datum)
                        break@datumCollections
                    }
                }
            }
        }
        return datumObjectsById
    }

    fun findDatumObjectsByIdRemote(datumIds: MutableSet<Int>, typesOfResponsibilityCollections: List<PresentationLayerTypesOfResponsibility>): MutableSet<PresentationLayerTypesOfResponsibility> {
        var datumObjectsById = mutableSetOf<PresentationLayerTypesOfResponsibility>()
        for (id in datumIds) {
            datumCollections@ for (datum in typesOfResponsibilityCollections) {
                when {
                    (datum.uuid == id) -> {
                        datumObjectsById.add(datum)
                        break@datumCollections
                    }
                }
            }
        }
        return datumObjectsById
    }


    /**
     * Получить список uuid из списка datum*/
    //создаем коллекции c id Datum-Объектов, для вычесления разности, пересечения и т.п
    private fun getIdsInDatumsLocal(typesOfResponsibilities: List<DataLayerTypesOfResponsibility>): MutableSet<Int> {
        var datumlIds = mutableSetOf<Int>()
        for (datum in typesOfResponsibilities) {
            datumlIds!!.add(datum.uuid!!)
        }
        return datumlIds
    }

    private fun getIdsInDatumsRemote(typesOfResponsibilities: List<PresentationLayerTypesOfResponsibility>): MutableSet<Int> {
        var datumlIds = mutableSetOf<Int>()
        for (datum in typesOfResponsibilities) {
            datumlIds!!.add(datum.uuid!!)
        }
        return datumlIds
    }


    //вычисляем пересечение коллекций
    private fun findIntersectionDatum(localIds: MutableSet<Int>, remoteIds: MutableSet<Int>): Sets.SetView<Int> {
        intersectionIds = Sets.intersection(localIds, remoteIds)
        return intersectionIds as Sets.SetView<Int>
    }

    //вычисляем разность коллекций, получаем разность из коллекции localDatums
    private fun findDifferenceDatumLocal(localIds: MutableSet<Int>, remoteIds: MutableSet<Int>): Sets.SetView<Int> {
        differenceLocalIds = Sets.difference(localIds, remoteIds)
        return differenceLocalIds as Sets.SetView<Int>
    }

    //вычисляем разность коллекций, получаем разность из коллекции remoteDatums
    private fun findDifferenceDatumRemote(localIds: MutableSet<Int>, remoteIds: MutableSet<Int>): Sets.SetView<Int> {
        differenceRemoteIds = Sets.difference(remoteIds, localIds)
        return differenceRemoteIds as Sets.SetView<Int>
    }


}



