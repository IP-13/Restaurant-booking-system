package com.ip13.main.webSocket

import com.ip13.main.event.BlackListNotificationEvent
import com.ip13.main.util.getLogger
import org.springframework.kafka.annotation.KafkaListener
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
    val undeliveredMessages = mutableMapOf<String, MutableList<String>>()

    @KafkaListener(topics = [BAD_PEOPLE_TOPIC])
    fun handleNotification(event: BlackListNotificationEvent) {
        log.info("[${LocalDateTime.now()}] Event received: $event")

        val message = "${event.senderName}: ${event.message}"

        sessions[event.receiverName]?.sendMessage(TextMessage(message))
            ?: undeliveredMessages[event.receiverName]?.add(message)
            ?: undeliveredMessages.put(event.receiverName, mutableListOf(message))
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info("[{}] connection established with session id {}", LocalDateTime.now(), session.id)
        val username = session.principal?.name ?: throw RuntimeException("No principal")
        sessions[username] = session
        undeliveredMessages[username]?.forEach { session.sendMessage(TextMessage(it)) }
    }

    /**
     * message received in form: 'userTo: payload'
     * userTo - name of receiver
     * payload - message
     */
    override fun handleMessage(session: WebSocketSession, webSocketMessage: WebSocketMessage<*>) {
        val (receiver, payload) = try {
            parsePayload(webSocketMessage.payload as String)
        } catch (ex: RuntimeException) {
            log.info(ex.message)
            session.sendMessage(TextMessage(ex.message ?: "Parse error"))
            return
        }

        val author = session.principal?.name ?: throw RuntimeException("No principal")
        val message = "$author: $payload"

        sessions[receiver]?.sendMessage(TextMessage(message)) ?: undeliveredMessages[receiver]?.add(message)
        ?: undeliveredMessages.put(receiver, mutableListOf(message))
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) =
        log.info("[{}] Error occurred on session {}", LocalDateTime.now(), session.id)


    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        log.info("[{}] Connection closed on session {}, status {}", LocalDateTime.now(), session.id, closeStatus.code)
        sessions.remove(session.principal!!.name)
    }

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

    companion object {
        private const val BAD_PEOPLE_TOPIC = "bad-people"
    }
}