package com.ip13.main.webSocket

import com.ip13.main.util.getLogger
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.time.LocalDateTime

class Handler : WebSocketHandler {
    private val log = getLogger(javaClass)

    /**
     * key - username
     * value - user session
     */
    private val sessions = mutableMapOf<String, WebSocketSession>()
    private val undeliveredMessages = mutableMapOf<String, MutableList<String>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info("[{}] connection established with session id {}", LocalDateTime.now(), session.id)
        undeliveredMessages[session.principal?.name]?.forEach { session.sendMessage(TextMessage(it)) }
            ?: throw RuntimeException("No principal")
    }

    /**
     * message received in form:
     * userTo: payload
     * userTo - name of receiver
     * payload - message
     */
    override fun handleMessage(session: WebSocketSession, webSocketMessage: WebSocketMessage<*>) {
        val (receiver, payload) = parsePayload(webSocketMessage.payload as String)

        val author = session.principal?.name ?: throw RuntimeException("No principal")
        val message = "$author: $payload"

        sessions[receiver]?.sendMessage(TextMessage(message)) ?: undeliveredMessages[receiver]?.add(message)
        ?: undeliveredMessages.put(receiver, mutableListOf(message))
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) =
        log.info("[{}] Error occurred on session {}", LocalDateTime.now(), session.id)


    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) =
        log.info("[{}] Connection closed on session {}, status {}", LocalDateTime.now(), session.id, closeStatus.code)

    override fun supportsPartialMessages(): Boolean = false

    /**
     * first - userTo
     * second - message
     */
    private fun parsePayload(payload: String): Pair<String, String> {
        val splitList = payload.split(":")
        return if (splitList.size != 2) {
            throw RuntimeException("Something wrong with your message. Message should have form - username: payload")
        } else {
            splitList[0] to splitList[1]
        }
    }
}