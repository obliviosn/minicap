/*
 * Copyright (C) 2020 Orange
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.devicefarmer.minicap.output

import android.util.Log
import io.devicefarmer.minicap.Size
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Provides method to send data to the client that connected
 *
 * Basically implements the "minicap" protocol
 */
@ExperimentalUnsignedTypes
class MinicapClientOutput(private val socket: Socket) :
    DisplayOutput() {
    companion object {
        const val BANNER_VERSION = 1
        const val BANNER_SIZE = 24
        const val QUIRK_ALWAYS_UPRIGHT = 2
    }

    /**
     * Sends the banner required at connection time
     */
    fun sendBanner(screenSize: Size, targetSize: Size) {
        val byteArray = ByteArray(BANNER_SIZE)
        ByteBuffer.wrap(byteArray).apply {
            order(ByteOrder.LITTLE_ENDIAN)
            put(BANNER_VERSION.toByte())
            put(BANNER_SIZE.toByte())
            putInt(android.os.Process.myPid()) //PID
            putInt(screenSize.width)//Width
            putInt(screenSize.height)//Height
            putInt(targetSize.width)//resized Width
            putInt(targetSize.height)//resized height
            put(0.toByte()) //orientation
            put(QUIRK_ALWAYS_UPRIGHT.toByte()) //quirk
        }
        with(socket.outputStream) {
            write(byteArray)
            flush()
        }
    }

    /**
     * Sends a buffer containing a jpg image
     */
    override fun send() {
        val data = imageBuffer.toByteArray()
        val payload = ByteArray(data.size + 4) //size: 32bit integer
        ByteBuffer.wrap(payload).apply {
            order(ByteOrder.LITTLE_ENDIAN)
            putInt(data.size)
            put(data)
        }
        var size=payload.size
        Log.w("minicap","send:  $size")
        with(socket.outputStream) {
            write(payload)
            flush()
        }
        imageBuffer.reset()
    }
}

