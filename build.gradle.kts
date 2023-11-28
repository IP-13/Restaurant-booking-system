tasks.register("clean") {
    delete("booking-system/build")
    delete("eureka/build")
    delete("gateway/build")
    delete("config-server/build")
}