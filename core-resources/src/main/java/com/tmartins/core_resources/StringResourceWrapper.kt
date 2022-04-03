package com.tmartins.core_resources

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

public interface StringResourceWrapper {
    public fun getString(@StringRes stringRes: Int): String
    public fun getString(stringRes: Int, vararg formatArgs: Any): String
    public fun getQuantityString(@PluralsRes pluralsRes: Int, quantity: Int, vararg formatArgs: Any): String
    public fun getStringArray(stringArrayRes: Int): Array<String>
}

public class StringResourceWrapperAndroid(
    private val context: Context
) : StringResourceWrapper {

    override fun getString(stringRes: Int): String {
        return context.getString(stringRes)
    }

    override fun getString(stringRes: Int, vararg formatArgs: Any): String {
        return context.getString(stringRes, *formatArgs)
    }

    override fun getQuantityString(pluralsRes: Int, quantity: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(pluralsRes, quantity, *formatArgs)
    }

    override fun getStringArray(stringArrayRes: Int): Array<String> {
        return context.resources.getStringArray(stringArrayRes)
    }
}