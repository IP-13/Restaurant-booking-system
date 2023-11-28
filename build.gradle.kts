tasks.register("clean") {
    delete("booking-system/build")
    delete("eureka-server/build")
    delete("gateway/build")
    delete("config-server/build")
}