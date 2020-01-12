package name.antonsmirnov.notes.app.controller.rest

fun buildRestApiImpl(protocol: String, host: String, port: UInt) : RestApi {
    return RestApi("$protocol://$host:$port")
}