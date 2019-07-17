package ru.npc_ksb.alfaknd.presentation_layer.model
import ru.npc_ksb.alfaknd.presentation_layer.model.Datum as PresentationLayerTypesOfResponsibility
import ru.npc_ksb.alfaknd.data_layer.repository.datasource.TypesOfResponsibility as DataLayerTypesOfResponsibility

/**
 * Класс показывающий изменения
 * либо удаленных данных,
 * либо локальных*/
class ChangedDatum(
        localTypesOfResponsibility: DataLayerTypesOfResponsibility,
        remoteTypesOfResponsibility: PresentationLayerTypesOfResponsibility,
        changedInfo: MutableSet<MutableMap<String, MutableMap<String, MutableMap<String, String>>>>
) {
    var localRadio : Boolean = false
    var remoteRadio : Boolean = false

    /**
     * Локальный datum, который нужно обновить*/
    var localTypesOfResponsibility: DataLayerTypesOfResponsibility
    /**
     * remote datum, на основе которого изменяется локальный*/
     var remoteTypesOfResponsibility: PresentationLayerTypesOfResponsibility

    /**
     * */
    var changedInfo: MutableSet<MutableMap<String, MutableMap<String, MutableMap<String, String>>>>

    init {
        this.localTypesOfResponsibility = localTypesOfResponsibility
        this.remoteTypesOfResponsibility = remoteTypesOfResponsibility
        this.changedInfo = changedInfo
    }

    /**
     * Меняет состояние на "нажата кнопка показать локальные изменения"
     * */
    fun clickedLocalRadio() {
        when {
            (!localRadio) -> this.localRadio = true
            (remoteRadio) -> this.remoteRadio = false
        }
    }

    /**
     * Меняет состояние на "нажата кнопка показать удаленные изменения"
     * */
    fun clickedRemoteRadio() {
        when {
            (!remoteRadio) -> this.remoteRadio = true
            (localRadio) -> this.localRadio = false
        }
    }
}