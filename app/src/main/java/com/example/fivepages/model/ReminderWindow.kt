package com.example.fivepages.model

//One Reminder Window (e.g. Morning coffee, 8:00 AM)
data class ReminderWindow(
    val id: Int,
    val label: String,
    val hour: Int,
    val minute: Int,
    val enabled: Boolean = true,
)

//Turns 13:00 into 1:00 PM
fun ReminderWindow.timeText(): String {
    //AM or PM
    val amPm = if (hour < 12) "AM" else "PM"
    //12-hour Clock
    val hour12 = if (hour % 12 == 0) 12 else hour % 12
    //Add Zero To Minutes (0 becomes 00)
    return "$hour12:${minute.toString().padStart(2, '0')} $amPm"
}