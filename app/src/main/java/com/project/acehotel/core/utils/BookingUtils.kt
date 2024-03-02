package com.project.acehotel.core.utils

import java.text.DecimalFormat

fun formatNumber(number: Int): String {
    // Create a DecimalFormat instance with the desired pattern
    val formatter = DecimalFormat("#,###")

    // Replace the comma (default grouping separator) with a dot
    val symbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = '.'
    formatter.decimalFormatSymbols = symbols

    // Format the number and return it
    return formatter.format(number)
}