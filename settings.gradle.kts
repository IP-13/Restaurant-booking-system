plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "Restaurant-booking-system"
include("eureka")
include("gateway")
include("config-server")
include("user-service")
include("reservation-service")
include("restaurant-service")