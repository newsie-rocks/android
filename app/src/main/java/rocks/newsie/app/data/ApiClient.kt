package rocks.newsie.app.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

data class ReqBody(
    val value: Int,
)

data class ResBody(
    val status: Int,
)

interface ApiInterface {
    @GET("/demo")
    suspend fun getQuotes(): Response<ResBody>
}

class ApiException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}

class ApiClient(
    private val baseUrl: String
) {
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiInterface::class.java)

    suspend fun getQuotes(): ResBody? {
        return service.getQuotes().body()
    }
}



