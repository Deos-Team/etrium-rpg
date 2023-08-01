package dev.deos.etrium.api

interface Tab {
    fun parentScreenClass(): Class<*>? {
        return null
    }
}