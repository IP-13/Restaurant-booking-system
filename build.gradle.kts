tasks.register("clean") {
    delete("eureka/build")
    delete("gateway/build")
    delete("config-server/build")
    delete("user-service/build")
    delete("reservation-service/build")
    delete("restaurant-service/build")
}