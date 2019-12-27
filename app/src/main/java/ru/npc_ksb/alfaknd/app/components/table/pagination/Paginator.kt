package ru.npc_ksb.alfaknd.app.components.table.pagination

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import kotlin.math.floor

class Paginator {
    companion object {
        val LABELS = "labels"
        val VALUES = "values"
    }

    fun getPagination(length: Int, value: Int, totalVisible: Int = 7): MutableMap<String, ArrayList<Any>> {
        val items = getItems(length, value, totalVisible)
        return getLabelsAndValues(items)
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    fun getItems(length: Int, value: Int, totalVisible: Int = 7): Multimap<String, String>  {
        val items = ArrayListMultimap.create<String, String>()

        if (length <= totalVisible) {
            for (i in  generateSequence(1) { it + 1 }.takeWhile{ it <= length }) {
                items.put(getItem(i), getItem(i))
            }
            return items
        }

        val half = floor((totalVisible / 2).toDouble()).toInt()

        val left: Int
        if (value > half + 1) {
            left = if (value > length - half) {
                if (value === length) {
                    half
                } else {
                    half - (length - value - half + 2)
                }
            } else {
                1
            }
        } else {
            left = if (value < half) {
                half
            } else {
                value + 1
            }
        }

        val right: Int
        right = if (value <= half + 1) {
            length - half - (half - left - 1)
        } else {
            if (value >= length - half) {
                if (value > length - half + 1) {
                    length - half + 1
                } else {
                    value - 1
                }
            } else {
                length
            }
        }


        for (i in generateSequence(1) { it + 1 }.takeWhile{ it <= left }) {
            items.put(getItem(i), getItem(i))
        }

        if (value > half + 1 && value < length - half) {
            val step = half -2
            items.put(getItem(getMiddle(value, left), true), getMiddle(value, left).toString())

            for (i in generateSequence(value - step) { it + 1 }.takeWhile{ it <= value + step }) {
                items.put(getItem(i), getItem(i))
            }

            items.put(getItem(getMiddle(value, right), true), getMiddle(value, right).toString())
        } else {
            items.put(getItem(getMiddle(left, right), true), getMiddle(left, right).toString())
        }

        for (i in generateSequence(right) { it + 1 }.takeWhile{ it <= length}) {
            items.put(getItem(i), getItem(i))
        }
        return items

    }

    private fun getItem(value: Int, more: Boolean? = null): String {
        return if (more != null) {
            "..."
        } else {
            value.toString()
        }
    }

    private fun getMiddle(a: Int, b: Int): Int {
        val sum: Double = ((a + b) / 2).toDouble()
        return floor(sum).toInt()
    }


    private fun getLabelsAndValues(paginationItems: Multimap<String, String>): MutableMap<String, ArrayList<Any>> {
        val labelsAndValues = mutableMapOf<String, ArrayList<Any>>()
        //сортируем
        val sortedPaginationMapItems = paginationItems.asMap().toSortedMap()
        //вырезаем ключ с коллекцией ...
        // записываем коллекцию в more
        val arrayLabels = arrayListOf<Any>()
        var arrayValues = arrayListOf<Any>()
        val moreCollections = sortedPaginationMapItems.remove("...")
        //формируем коллекцию ключей
        val sortedPaginationItems = sortedPaginationMapItems.map {
            it.key.toInt()
        }.sorted()

        for (el in sortedPaginationItems){
            arrayLabels.add(el)
            arrayValues.add(el)
        }

        if (moreCollections!=null){
            //преобразуем значение из коллекции ... в Int
            moreCollections.forEach {
                arrayValues.add(it.toInt())
            }
            val temp = arrayValues.map {
                it as Int
            }
            arrayValues = temp.sorted().map {
                it as Any
            } as ArrayList<Any>
            for (more in moreCollections) {
                arrayLabels.add(arrayValues.indexOf(more.toInt()), "...")
            }
        }
        labelsAndValues.put(LABELS, arrayLabels)
        labelsAndValues.put(VALUES, arrayValues)
        return labelsAndValues
    }
}