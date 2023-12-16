plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "Restaurant-booking-system"
include("config-server")
include("eureka")
include("gateway")
include("grade-service")
include("restaurant-service")
include("user-service")
include("black-list-service")
include("file-service")
