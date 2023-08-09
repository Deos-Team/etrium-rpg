package dev.deos.etrium.client.utils

import java.util.*

object SortList {

    fun <T : Comparable<T>?> concurrentSort(key: List<T>, vararg lists: List<*>) {
        val indices: MutableList<Int> = ArrayList()
        for (i in key.indices) indices.add(i)

        indices.sortWith { i, j -> key[i!!]!!.compareTo(key[j!!]) }

        val swapMap: MutableMap<Int, Int> = HashMap(indices.size)
        val swapFrom: MutableList<Int> = ArrayList(indices.size)
        val swapTo: MutableList<Int> = ArrayList(indices.size)
        for (i in key.indices) {
            var k = indices[i]
            while (i != k && swapMap.containsKey(k)) k = swapMap[k]!!
            swapFrom.add(i)
            swapTo.add(k)
            swapMap[i] = k
        }

        for (list in lists) for (i in list.indices) Collections.swap(
            list,
            swapFrom[i], swapTo[i]
        )
    }

}