package com.rollinup.common.model

enum class SecurityAlert(
    val title: String,
    val message: String,
) {
    ROOT(
        title = "Root Access",
        message = "For security reasons, this application cannot continue running on a rooted device.\n" +
                "Please use a non-rooted device to access this app."
    ),
    LOCATION_SPOOF(
        title = "Location Spoofing",
        message = "For security reasons, this application cannot continue running on a device with location spoofing.\n" +
                "Please disable location spoofing or try another device to access this app."
    ),
    TIME_SPOOF(
        title = "Location Spoofing",
        message = "For security reasons, this application cannot continue running on a device with time spoofing.\n" +
                "Please disable Time spoofing or try another device to access this app."
    )
}