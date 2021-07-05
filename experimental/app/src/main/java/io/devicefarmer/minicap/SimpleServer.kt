/*
 * Copyright (C) 2020 Orange
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.devicefarmer.minicap

import android.net.LocalServerSocket
import android.net.LocalSocket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

/**
 * Minimalist "server" to bootstrap development
 */
class SimpleServer(private val socket: String, private val listener: Listener) : Runnable {

    var thread = Thread(this)

    companion object {
        val log: Logger = LoggerFactory.getLogger(SimpleServer::class.java.simpleName)
    }

    interface Listener {
        fun onConnection(socket: Socket)
    }

    fun start() {
        thread.start()
    }

    override fun run() {
        try {
            val serverSocket = ServerSocket(2333)
            log.info("Listening on socket : ${serverSocket}")
            val clientSocket: Socket = serverSocket.accept()
            listener.onConnection(clientSocket)
        } catch (e: IOException) {
            log.error("error waiting connection", e)
        }
    }
}
