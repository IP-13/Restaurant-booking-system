tasks.register("clean") {
    delete("config-server/build")
    delete("eureka/build")
    delete("gateway/build")
    delete("grade-service/build")
    delete("restaurant-service/build")
    delete("user-service/build")
    delete("black-list-service/build")
    delete("file-service/build")
    delete("chat-service/build")
}