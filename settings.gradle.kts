plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "Restaurant-booking-system"
include("eureka-server")
include("booking-system")
include("gateway")
