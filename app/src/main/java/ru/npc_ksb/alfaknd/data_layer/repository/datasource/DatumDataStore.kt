package ru.npc_ksb.alfaknd.data_layer.repository.datasource

import io.reactivex.Completable
import io.reactivex.Observable
import ru.npc_ksb.alfaknd.presentation_layer.model.ActualData
import ru.npc_ksb.alfaknd.presentation_layer.model.ChangedDatum
import ru.npc_ksb.alfaknd.presentation_layer.model.Datum as PresentationLayerTypesOfResponsibility
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility as DataLayerTypesOfResponsibility
import ru.npc_ksb.alfaknd.presentation_layer.model.Field

interface DatumDataStore {
    /**
     * Получить данные из локальныой БД
     * @param changedDatumsUuids айдишники измененных remote-данных
     * @return наблюдаемый список устаревших Datum
     * */
    fun getOldLocalData(remoteDatums: List<Long>): Observable<List<DataLayerTypesOfResponsibility>>
    /**
     * Получить удаленные данные, где
     * @param part задаем по каким порциям полуяать данные
     * @param lastUpdated дата последнего обновления
     * @return наблюдаемый список Datum*/
    fun getChangedRemoteData(actualData: Observable<ActualData>, part: Long): Observable<List<PresentationLayerTypesOfResponsibility>>?

    /**
     * Получить название полей datum на кириллице
     * @return Наблюдаемый список полей
     * */
    fun getRemoteFields(): Observable<List<Field>?>?

    fun getLastUpdatedDate() : Observable<ActualData>

    fun setLastUpdatedData(lastUpdate: org.threeten.bp.LocalDateTime) : Completable

    fun sync()

    var changedDatums : MutableSet<ChangedDatum>
}