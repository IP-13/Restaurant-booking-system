tasks.register("clean") {
    delete("config-server/build")
    delete("eureka/build")
    delete("gateway/build")
    delete("grade-service/build")
    delete("restaurant-service/build")
    delete("user-service/build")
}