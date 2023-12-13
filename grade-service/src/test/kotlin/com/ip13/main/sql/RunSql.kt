package com.ip13.main.sql

/**
 * NOTE! order of scripts matter
 */
@Target(AnnotationTarget.FUNCTION)
annotation class RunSql(val scripts: Array<String>)
