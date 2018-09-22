package com.kwunai.github.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private inline fun <T> SharedPreferences.delegate(
        key: String? = null,
        defaultValue: T,
        crossinline getter: SharedPreferences.(String, T) -> T,
        crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> =
        object : ReadWriteProperty<Any, T> {
            override fun getValue(thisRef: Any, property: KProperty<*>): T =
                    getter(key ?: property.name, defaultValue)!!

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
                    edit().setter(key ?: property.name, value).apply()
        }

inline fun <reified T> SharedPreferences.gson(defaultValue: T, key: String? = null) =
        object : ReadWriteProperty<Any, T> {
            private val gson = Gson()

            override fun getValue(thisRef: Any, property: KProperty<*>): T {

                val s = getString(key ?: property.name, "")

                return if (s.isBlank()) defaultValue else gson.fromJson(s, T::class.java)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
                    edit().putString(key ?: property.name, gson.toJson(value)).apply()
        }

fun SharedPreferences.string(key: String? = null, defValue: String = "", isEncrypt: Boolean = false): ReadWriteProperty<Any, String> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptString, SharedPreferences.Editor::putEncryptString)
    } else {
        delegate(key, defValue, SharedPreferences::getString, SharedPreferences.Editor::putString)
    }
}

fun SharedPreferences.int(key: String? = null, defValue: Int = 0, isEncrypt: Boolean = false): ReadWriteProperty<Any, Int> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptInt, SharedPreferences.Editor::putEncryptInt)
    } else {
        delegate(key, defValue, SharedPreferences::getInt, SharedPreferences.Editor::putInt)
    }
}

fun SharedPreferences.boolean(key: String? = null, defValue: Boolean = false, isEncrypt: Boolean = false): ReadWriteProperty<Any, Boolean> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptBoolean, SharedPreferences.Editor::putEncryptBoolean)
    } else {
        delegate(key, defValue, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)
    }
}

fun SharedPreferences.initKey(key: String) = Encrypt.key(key)

fun SharedPreferences.getEncryptString(key: String, defValue: String?): String {
    val encryptValue = this.getString(encryptPreference(key), null)
    return if (encryptValue == null) defValue ?: "" else decryptPreference(encryptValue)
}

fun SharedPreferences.getEncryptInt(key: String, defValue: Int): Int {
    val encryptValue = this.getString(encryptPreference(key), null)
            ?: return defValue
    return Integer.parseInt(decryptPreference(encryptValue))
}

fun SharedPreferences.Editor.putEncryptInt(key: String, value: Int): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(Integer.toString(value)))
    return this
}

fun SharedPreferences.Editor.putEncryptString(key: String, value: String): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(value))
    return this
}

fun SharedPreferences.getEncryptBoolean(key: String, defValue: Boolean): Boolean {
    val encryptValue = this.getString(encryptPreference(key), null) ?: return defValue
    return java.lang.Boolean.parseBoolean(decryptPreference(encryptValue))
}

fun SharedPreferences.Editor.putEncryptBoolean(key: String, value: Boolean): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(java.lang.Boolean.toString(value)))
    return this
}

private fun encryptPreference(plainText: String): String {
    return Encrypt.encrypt(plainText)
}

private fun decryptPreference(cipherText: String): String {
    return Encrypt.decrypt(cipherText)
}

