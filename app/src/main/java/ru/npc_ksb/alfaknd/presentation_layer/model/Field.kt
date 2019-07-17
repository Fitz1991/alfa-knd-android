package ru.npc_ksb.alfaknd.presentation_layer.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель для таблицы в sqLite, так же модель для парсмнга из Json в Объект*/
@Entity
class Field {
    @PrimaryKey
    @SerializedName("uuid")
    @Expose
    var uuid: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("label")
    @Expose
    var label: String? = null

    /*
        var stringRepresentation: String
    get() = this.toString()
    set(value) {
        setDataFromString(value) // парсит строку и устанавливает значения для других свойств
    }
    */
    @Ignore
    private var fieldValueMatches: MutableMap<String, String> = mutableMapOf()

    /**
     * устанавливает соответствие ключа поля объекта datum названию на кириллице
     * field.name - поле на латинице (__str__)
     * field.label - его эквивалент на кириллице (Наименование)
    * */
    @Ignore
    fun setFieldValueMatches(fields : List<Field>){
        for (field in fields) {
            fieldValueMatches.put(field.name!!, field.label!!)
        }
    }

    @Ignore
    fun getFieldValueMatches() : MutableMap<String, String>{
        return fieldValueMatches
    }
}

/*Создать коллекцию  fieldValueMatches<String, String>, где ключ это название поля, например "__str__", а значение это его label, напрмер "Наименование".  Формирование делаем из  массива объектов fields:
1) перебираем массив fields в цикле:
for(field in fields)
2) в коллекцию fieldValueMatches добаввляем field.name как ключ и field.label как значение*/
