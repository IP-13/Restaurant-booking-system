package com.ip13.main.sql

import io.r2dbc.spi.ConnectionFactory
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ScriptUtils
import reactor.core.publisher.Mono

class RunSqlExtension : BeforeTestExecutionCallback {
    override fun beforeTestExecution(extensionContext: ExtensionContext?) {
        val annotation = extensionContext?.testMethod?.map { it.getAnnotation(RunSql::class.java) }?.orElse(null)
        annotation?.let {
            val testInstance = extensionContext.testInstance.orElseThrow {
                RuntimeException("${javaClass.simpleName} is supposed to be used in junit 5 only!")
            }
            val connectionFactory = getConnectionFactory(testInstance)
            if (connectionFactory != null)
                it.scripts.forEach { script ->
                    Mono.from(connectionFactory.create())
                        .flatMap<Any> { connection ->
                            ScriptUtils.executeSqlScript(
                                connection,
                                ClassPathResource(script)
                            )
                        }.block()
                }
        }
    }

    private fun getConnectionFactory(testInstance: Any?): ConnectionFactory? {
        testInstance?.let {
            val superclass = it.javaClass.superclass
            val fields = superclass.declaredFields

            return fields
                .find { it.name.equals("connectionFactory") }
                .also { it?.isAccessible = true }?.get(it) as ConnectionFactory
        }
        return null
    }
}