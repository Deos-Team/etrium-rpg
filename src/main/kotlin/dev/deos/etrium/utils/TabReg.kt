package dev.deos.etrium.utils

import dev.deos.etrium.EtriumClient
import dev.deos.etrium.api.InventoryTab
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
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