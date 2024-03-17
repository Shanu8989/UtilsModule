package com.shanu.utilsmodule

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

/*
*
* Extension method display message in the form of Toast, for a longer duration period
* */
fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/*
*
* Extension method display message in the form of Toast, for a short duration
* */
fun Context.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/*
*
* Extension method hide a view
* */
fun View.hide() {
    this.visibility = View.GONE
}


/*
*
* Extension method display a view
* */
fun View.show() {
    this.visibility = View.VISIBLE
}


/*
*
* Extension method to display Error, in the form of a Snackbar
* */
fun View.errorSnack(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    val snack = Snackbar.make(this, message, length)
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(Color.parseColor("#E0403F"))
    snack.show()
}

/*
*
* Extension method transform a string value to exclude alphanumeric values
* */
fun String.removeAllNonAlphanumericValuesFromString(): String {
    val re = Regex("[^A-Za-z0-9& ]")
    return re.replace(this, "")
}

/*
*
* Extension method to check if the String is Name
* */
fun String.isName(): Boolean {
    val nameRegex =
        """^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$""".toRegex()  // ToDo: make changes to accommodate localisation
    return this.matches(nameRegex)
}

/*
*
* Extension method to extract First Name, Last Name & Middle Name from Full Name
* In case Middle Name is not found, add default placeholder '--' to the position
* */
fun String.extractNames(): Triple<String?, String?, String?> {
    val names = this.split(" ")

    return when (names.size) {
        1 -> Triple(names[0], null, null)  // Only First Name
        2 -> Triple(names[0], null, names[1])  // First Name and Last Name
        else -> Triple(
            names[0],
            names[1],
            names.subList(2, names.size).joinToString(" -- ")
        )  // First Name, Last Name, and Middle Name
    }
}

/*
*
* String extension to convert UTF date to ddmmyyyy format
* */
fun String.convertToDDMMMYYYY(): String {
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'UTC' yyyy", Locale.US)
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    try {
        val date = inputFormat.parse(this)
        return outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return ""
}


/*
*
* Extension method transform a UTF date string to the proposed Date Format.
* */
fun String.convertToCustomFormat(): String {
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
    val date = inputFormat.parse(this)

    val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
    val time = outputFormat.format(date)

    val calendar = Calendar.getInstance()
    calendar.time = date
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)

    return when {
        isSameDay(calendar, today) -> "Today | $time"
        isSameDay(calendar, yesterday) -> "Yesterday | $time"
        else -> outputFormat.format(date)
    }
}


/*
*
* Method to compare a date and check if it is in fact the same day
* */
private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}


/*
*
* Calculate the remaining Time from a UTF Date String.
* */
fun String.calculateRemainingTime(): String {
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
    val date = inputFormat.parse(this)

    val currentTime = Calendar.getInstance().time
    val remainingMillis = abs(currentTime.time - date.time)

    val days = remainingMillis / (24 * 60 * 60 * 1000)
    val hours = (remainingMillis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)

    return String.format("%dd %dh", days, hours)
}