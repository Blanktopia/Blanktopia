package me.weiwen.blanktopia

import java.util.logging.Level

typealias Node = Map<String, *>

inline fun <reified T> Node.tryGet(path: String): T? {
    val value = this[path] as? T
    if (value == null) {
        BlanktopiaCore.INSTANCE.logger.log(Level.SEVERE, "Expected ${T::class.simpleName} at key '$path'")
    }
    return value
}

inline fun <reified T> Node.tryGet(path: String, def: T): T {
    val value = this[path]
    if (value != null && value !is T) {
        BlanktopiaCore.INSTANCE.logger.log(Level.SEVERE, "Expected ${T::class.simpleName} at key '$path'")
    }
    return value as? T ?: def
}

