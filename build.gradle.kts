tasks.register("clean") {
    delete("booking-system/build")
    delete("eureka/build")
    delete("gateway/build")
    delete("config-server/build")
    delete("user-service/build")
    delete("restaurant-service/build")
    delete("reservation-service/build")
}