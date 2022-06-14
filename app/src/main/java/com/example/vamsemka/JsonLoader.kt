import android.content.Context
import androidx.compose.runtime.MutableState
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
//https://sazib.medium.com/read-json-file-from-assets-346f624faf92
data class Cord (

    @SerializedName("lon" ) var lon : Double? = null,
    @SerializedName("lat" ) var lat : Double? = null

)
data class CitySK (

    @SerializedName("id"      ) var id      : Int?    = null,
    @SerializedName("name"    ) var name    : String? = null,
    @SerializedName("state"   ) var state   : String? = null,
    @SerializedName("country" ) var country : String? = null,
    @SerializedName("coord"   ) var coord   : Cord?  = Cord()

)

fun getCountryCode(context: Context): kotlin.collections.List<CitySK> {

    lateinit var jsonString: String
    try {
        jsonString = context.assets.open("citiesSlovakia.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
       // AppLogger.d(ioException)
    }

    val listCountryType = object : TypeToken<kotlin.collections.List<CitySK>>() {}.type
    return Gson().fromJson(jsonString, listCountryType)
}
