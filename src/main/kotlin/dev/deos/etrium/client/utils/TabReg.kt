package dev.deos.etrium.client.utils

import dev.deos.etrium.EtriumClient
import dev.deos.etrium.api.InventoryTab

object TabReg {

    fun registerInventoryTab(tab: InventoryTab) {
        EtriumClient.invTabs.add(tab)
        val priorityList: MutableList<Int> = ArrayList()
        for (i in EtriumClient.invTabs.indices) {
            var preferedPos = EtriumClient.invTabs[i].preferedPos
            if (preferedPos == -1) {
                preferedPos = 99
            }
            priorityList.add(preferedPos)
        }
        SortList.concurrentSort(priorityList, EtriumClient.invTabs)
    }

}