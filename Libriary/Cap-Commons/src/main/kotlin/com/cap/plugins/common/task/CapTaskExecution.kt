package com.cap.plugins.common.task

import com.intellij.util.concurrency.AppExecutorUtil
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class CapTaskExecution<T>(
    private val execution: () -> T,
    private var successHandlers: MutableList<(T) -> Unit> = mutableListOf(),
    private var failureHandlers: MutableList<(Exception) -> Unit> = mutableListOf(),
    private var finallyHandlers: MutableList<(T?, Exception?) -> Unit> = mutableListOf()
) {

    fun execute(): T {
        return try {
            val result = execution.invoke()
            successHandlers.forEach { it.invoke(result) }
            finallyHandlers.forEach { it.invoke(result, null) }
            result
        } catch (e: CapTaskCancelledException) {
            finallyHandlers.forEach { it.invoke(null, e) }
            throw e
        } catch (e: Exception) {
            failureHandlers.forEach { it.invoke(e) }
            finallyHandlers.forEach { it.invoke(null, e) }
            throw e
        }
    }

    fun executeQuietly(): T? {
        return try {
            execute()
        } catch (ignore: Exception) {
            null
        }
    }

    fun executeOnPooledThread(): CompletableFuture<T> {
        return CompletableFuture.supplyAsync(Supplier<T> { execute() }, AppExecutorUtil.getAppExecutorService())
    }

    fun <R> map(mapper: (T) -> R): CapTaskExecution<R> {
        return CapTaskExecution({ mapper.invoke(execution.invoke()) })
    }

    fun success(handler: (T) -> Unit): CapTaskExecution<T> {
        successHandlers.add(handler)
        return this
    }

    fun failure(handler: (Exception) -> Unit): CapTaskExecution<T> {
        failureHandlers.add(handler)
        return this
    }

    fun finally(handler: (T?, Exception?) -> Unit): CapTaskExecution<T> {
        finallyHandlers.add(handler)
        return this
    }

    companion object {
        fun empty() = CapTaskExecution({ null })
    }
}