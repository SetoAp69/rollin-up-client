import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun GeofenceHandler(
    startTracking:Boolean,
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

private fun validateLocation(geofence: Geofence, location: Location?): Boolean {
    val currentLocation = location?.coordinates ?: return false
    val geofenceCenter = Coordinates(geofence.latitude, geofence.longitude)

    return calculateDistance(currentLocation, geofenceCenter) <= geofence.rad
}

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

private fun Double.toRadians(): Double {
    return this * PI / 180.0
}
