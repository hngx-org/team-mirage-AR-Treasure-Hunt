package com.shegs.artreasurehunt.data.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine {task->
        addOnCompleteListener {
            if (it.exception != null) {
                task.resumeWithException(it.exception!!)
            }else {
                task.resume(it.result,null)
            }
        }
    }
}