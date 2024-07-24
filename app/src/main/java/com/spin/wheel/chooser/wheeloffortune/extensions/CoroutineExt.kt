package com.spin.wheel.chooser.wheeloffortune.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> CoroutineScope.executeAsync(
    onPreExecute: suspend () -> Unit,
    doInBackground: suspend () -> T,
    onPostExecute: (T) -> Unit
) = launch {
    onPreExecute()
    val result = withContext(Dispatchers.IO) {
        doInBackground()
    }
    onPostExecute(result)
}