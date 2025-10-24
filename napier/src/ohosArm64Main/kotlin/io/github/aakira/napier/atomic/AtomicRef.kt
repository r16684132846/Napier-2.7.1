package io.github.aakira.napier.atomic

internal actual class AtomicRef<T> actual constructor(value: T) {
    private var _value: T = value

    actual var value: T
        get() = _value
        set(value) {
            _value = value
        }
}
