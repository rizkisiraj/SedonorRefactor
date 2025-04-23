import com.example.sedonortdd.data.models.OpenAiRequest
import com.example.sedonortdd.data.models.OpenAiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/chat/completions")
    fun getGptResponse(
        @Header("Authorization") authorization: String,
        @Body requestBody: OpenAiRequest
    ): Call<OpenAiResponse>
}
