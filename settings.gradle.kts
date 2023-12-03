plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "Restaurant-booking-system"
include("eureka")
include("booking-system")
include("gateway")
include("config-server")
include("user-service")
//include("restaurant-service")
//include("reservation-service")
