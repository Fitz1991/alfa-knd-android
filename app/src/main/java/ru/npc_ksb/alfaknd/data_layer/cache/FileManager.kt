/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.cache

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper class to do operations on regular files/directories.
 */
@Singleton
class FileManager
@Inject
constructor() {

    /**
     * Writes a file to Disk.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform this operation using another thread.

     * @param file The file to write to Disk.
     */
    fun writeToFile(file: File, fileContent: String) {
        if (!file.exists()) {
            try {
                val writer = FileWriter(file)
                writer.write(fileContent)
                writer.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {

            }
        }
    }

    /**
     * Reads a content from a filePath.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.

     * @param filePath The filePath to read from.
     * *
     * @return A string with the content of the filePath.
     */
    fun readFileContent(filePath: String): String {
        val fileContentBuilder = StringBuilder()
        if (filePath.isNotEmpty()) {
            var stringLine: String
            try {
                val fileReader = FileReader(filePath)
                val bufferedReader = BufferedReader(fileReader)
                stringLine = bufferedReader.readLine()
                while (stringLine != null) {
                    fileContentBuilder.append(stringLine + "\n")
                    stringLine = bufferedReader.readLine()
                }
                bufferedReader.close()
                fileReader.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return fileContentBuilder.toString()
    }

    /**
     * Returns a boolean indicating whether this file can be found on the underlying file system.

     * @param file The file to check existence.
     * *
     * @return true if this file exists, false otherwise.
     */
    fun exists(file: File): Boolean {
        return file.exists()
    }

    /**
     * Warning: Deletes the content of a directory.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.

     * @param directory The directory which its content will be deleted.
     */
    fun clearDirectory(directory: File) {
        if (directory.exists()) {
            for (file in directory.listFiles()) {
                file.delete()
            }
        }
    }

    /**
     * Write a value to a user preferences file.

     * @param context [android.content.Context] to retrieve android user preferences.
     * *
     * @param preferenceFileName A file name reprensenting where typesOfResponsibilities will be written to.
     * *
     * @param key A string for the key that will be used to retrieve the value in the future.
     * *
     * @param value A long representing the value to be inserted.
     */
    fun writeToPreferences(context: Context, preferenceFileName: String, key: String,
                           value: Long) {

        val sharedPreferences = context.getSharedPreferences(preferenceFileName,
                Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * Get a value from a user preferences file.

     * @param context [android.content.Context] to retrieve android user preferences.
     * *
     * @param preferenceFileName A file name representing where typesOfResponsibilities will be get from.
     * *
     * @param key A key that will be used to retrieve the value from the preference file.
     * *
     * @return A long representing the value retrieved from the preferences file.
     */
    fun getFromPreferences(context: Context, preferenceFileName: String, key: String): Long {
        val sharedPreferences = context.getSharedPreferences(preferenceFileName,
                Context.MODE_PRIVATE)
        return sharedPreferences.getLong(key, 0)
    }
}
