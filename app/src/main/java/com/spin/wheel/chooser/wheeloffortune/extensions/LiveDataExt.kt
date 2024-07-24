package com.spin.wheel.chooser.wheeloffortune.extensions

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.chunked(chunkSize: Int): Flow<List<T>> {
    val buffer = mutableListOf<T>()
    return flow {
        this@chunked.collect {
            buffer.add(it)
            if (buffer.size == chunkSize) {
                emit(buffer.toList())
                buffer.clear()
            }
        }
        if (buffer.isNotEmpty()) {
            emit(buffer.toList())
        }
    }
}

fun <T> Flow<T>.div(list: List<T>, size: Int): Flow<List<T>> {
    Log.d("vmh", "div: $size")
    return flow {
        this@div.collect {
            when (size) {
                2 -> {
                    when (list.size) {
                        in 0..4 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, list.size))
                        }
                        in 5..6 -> {
                            emit(list.subList(0, 3))
                            emit(list.subList(3, list.size))
                        }
                        in 7..9 -> {
                            emit(list.subList(0, 4))
                            emit(list.subList(4, list.size))
                        }
                        in 10..100 -> {
                            emit(list.subList(0, 5))
                            emit(list.subList(5, list.size))
                        }
                    }

                }

                3 -> {
                    when (list.size) {
                        in 0..4 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 3))
                            emit(list.subList(3, list.size))
                        }

                        in 5..7 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 4))
                            emit(list.subList(4, list.size))
                        }

                        in 8..100 -> {
                            emit(list.subList(0, 3))
                            emit(list.subList(3, 6))
                            emit(list.subList(6, list.size))
                        }
                    }

                }

                4 -> {
                    when (list.size) {
                        in 0..5 -> {
                            emit(list.subList(0, 1))
                            emit(list.subList(1, 2))
                            emit(list.subList(2, 3))
                            emit(list.subList(3, list.size))
                        }
                        6 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 4))
                            emit(list.subList(4, 5))
                            emit(list.subList(5, list.size))
                        }
                        in 7..9 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 4))
                            emit(list.subList(4, 6))
                            emit(list.subList(6, list.size))
                        }
                        in 10..100 -> {
                            emit(list.subList(0, 3))
                            emit(list.subList(3, 6))
                            emit(list.subList(6, 8))
                            emit(list.subList(8, list.size))
                        }
                    }
                }

                5 -> {
                    when (list.size) {
                        in 0..6 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 3))
                            emit(list.subList(3, 4))
                            emit(list.subList(4, 5))
                            emit(list.subList(5, list.size))
                        }
                        7 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 3))
                            emit(list.subList(3, 4))
                            emit(list.subList(4, 6))
                            emit(list.subList(6, list.size))
                        }
                        8 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 4))
                            emit(list.subList(4, 5))
                            emit(list.subList(5, 6))
                            emit(list.subList(6, list.size))
                        }
                        in 9..100 -> {
                            emit(list.subList(0, 2))
                            emit(list.subList(2, 4))
                            emit(list.subList(4, 6))
                            emit(list.subList(6, 8))
                            emit(list.subList(8, list.size))
                        }
                    }
                }
            }
        }
    }
}