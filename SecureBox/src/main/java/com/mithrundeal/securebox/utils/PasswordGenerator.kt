package com.mithrundeal.securebox.utils

import kotlin.random.Random

interface Builder {
    fun enableUppercase(enabled: Boolean = true): Builder
    fun enableLowerCase(enabled: Boolean = true): Builder
    fun enableNumbers(enabled: Boolean = true): Builder
    fun enableSymbols(enabled: Boolean = true): Builder
    fun setMaxLength(maxLength: Int): Builder
    fun build(): PasswordGenerator
}

/**
 * If no value is set, it is set to true by default.
 * @property includeUppercase default enabled
 * @property includeLowerCase default enabled
 * @property includeNumbers default enabled
 * @property includeSymbols default enabled
 * @property maxLength default length is 24.
 */
class PasswordGeneratorBuilder : Builder {

    private var includeUpperCase = true
    private var includeLowerCase = true
    private var includeNumbers = true
    private var includeSymbols = true
    private var maxLength = 24 // Default maximum length

    override fun enableUppercase(enabled: Boolean): Builder {
        includeUppercase = enabled
        return this
    }

    override fun enableLowerCase(enabled: Boolean): Builder {
        includeLowerCase = enabled
        return this
    }

    override fun enableNumbers(enabled: Boolean): Builder {
        includeNumbers = enabled
        return this
    }

    override fun enableSymbols(enabled: Boolean): Builder {
        includeSymbols = enabled
        return this
    }

    override fun setMaxLength(maxLength: Int): Builder {
        require(maxLength > 0) { "Maximum length must be positive." }
        this.maxLength = maxLength
        return this
    }

    override fun build(): PasswordGenerator {
        return PasswordGenerator(
            includeUppercase = includeUppercase,
            includeLowerCase = includeLowerCase,
            includeNumbers = includeNumbers,
            includeSymbols = includeSymbols,
            maxLength = maxLength
        )
    }
}

class PasswordGenerator(
    private val includeUppercase: Boolean,
    private val includeLowerCase: Boolean,
    private val includeNumbers: Boolean,
    private val includeSymbols: Boolean,
    private val maxLength: Int
) {

    private val random = Random

    /**
     * Random password generator based on preset criteria
     */
    fun generatePassword(): String {
        val characterSets = mutableListOf<String>()
        if (includeUppercase) characterSets.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        if (includeLowerCase) characterSets.add("abcdefghijklmnopqrstuvwxyz")
        if (includeNumbers) characterSets.add("0123456789")
        if (includeSymbols) characterSets.add("!@#$%^&*()_-+=|\\:;'\"?><,.`~{}[]/")

        if (characterSets.isEmpty()) {
            throw IllegalArgumentException("At least one character set must be enabled.")
        }

        val builder = StringBuilder()
        for (i in 0 until maxLength) {
            val selectedSet = characterSets.random()
            val randomIndex = random.nextInt(selectedSet.length)
            builder.append(selectedSet[randomIndex])
        }

        return builder.toString()
    }
}
