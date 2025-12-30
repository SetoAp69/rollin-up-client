import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.location.Geofence
import com.rollinup.rollinup.component.location.LocationHandler
import com.rollinup.rollinup.component.theme.LocalGlobalSetting
import dev.jordond.compass.Coordinates
import dev.jordond.compass.Location
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A composable that manages geofencing logic by tracking user location and validating it against a defined zone.
 *
 * It retrieves global settings (radius, latitude, longitude) to define the geofence and uses [LocationHandler]
 * to receive location updates. It calculates whether the user is inside the geofence and invokes [onUpdateLocation].
 *
 * @param startTracking Controls whether location tracking is active.
 * @param onUpdateLocation Callback invoked when a location update is received.
 * Returns the [Location] object and a Boolean indicating if the user is inside the geofence.
 */
@Composable
fun GeofenceHandler(
    startTracking: Boolean,
    onUpdateLocation: (Location?, Boolean) -> Unit,
) {
    val globalSetting = LocalGlobalSetting.current
    val geoFence = Geofence(
        rad = globalSetting.radius,
        longitude = globalSetting.longitude,
        latitude = globalSetting.latitude
    )
    LocationHandler(
        onLocationChanges = { location ->
            onUpdateLocation(
                location,
                validateLocation(geoFence, location)
            )
        },
        startTracking = startTracking
    )
}

/**
 * Checks if the provided location is within the radius of the geofence.
 *
 * @param geofence The geofence definition.
 * @param location The current user location.
 * @return True if the user is within the geofence radius, false otherwise.
 */
private fun validateLocation(geofence: Geofence, location: Location?): Boolean {
    val currentLocation = location?.coordinates ?: return false
    val geofenceCenter = Coordinates(geofence.latitude, geofence.longitude)

    return calculateDistance(currentLocation, geofenceCenter) <= geofence.rad
}

/**
 * Calculates the distance between two coordinates using the Haversine formula.
 *
 * @param current The current coordinates.
 * @param target The target (geofence center) coordinates.
 * @return The distance in meters.
 */
private fun calculateDistance(
    current: Coordinates,
    target: Coordinates,
): Double {
    val earthRad = 6371
    val deltaLat = (current.latitude - target.latitude).toRadians()
    val deltaLon = (current.longitude - target.longitude).toRadians()
    val a = sin(deltaLat / 2).pow(2.0) +
            sin(deltaLon / 2).pow(2.0) *
            cos(current.latitude.toRadians()) * cos(target.latitude.toRadians())

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val finalDistance = earthRad * c * 1000

    return finalDistance
}

/**
 * Extension function to convert degrees to radians.
 */
private fun Double.toRadians(): Double {
    return this * PI / 180.0
}