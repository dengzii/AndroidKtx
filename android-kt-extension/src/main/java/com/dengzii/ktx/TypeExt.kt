package com.dengzii.ktx

inline fun Boolean?.isTrue(block: () -> Unit) {
    if (this == true) {
        block()
    }
}

