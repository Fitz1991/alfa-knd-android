package ru.npc_ksb.alfaknd.utils

import kotlin.math.min

object Chunk {
    fun chopped(dataSize: Int, limit: Int): MutableMap<Int, MutableMap<Int, Int>> {
        val parts = mutableMapOf<Int, MutableMap<Int, Int>>()

        var i = 0
        var j = 1
        var chunkNumber = 1
        var pageNumber = 1

        var list = linkedSetOf<Int>()
        while (j <= dataSize) {
            list.add(j)
            j++
        }

        val countAll = list.size
        while (i < countAll) {
            var tempList = mutableMapOf<Int, Int>()
            ArrayList(list.toList().subList(i, min(countAll, i + limit)))
                .forEach {
                    it as Int
                    tempList[pageNumber] = it
                    pageNumber++
                }
            parts[chunkNumber] = tempList
            chunkNumber++
            i += limit
        }
        return parts
    }
}