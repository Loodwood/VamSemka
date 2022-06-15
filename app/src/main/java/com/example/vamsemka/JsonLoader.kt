import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okio.IOException
import kotlin.collections.List

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

fun getCountryCodes(context: Context): kotlin.collections.List<CitySK> {
    lateinit var jsonString: String
    try {
        jsonString = context.assets.open("countries/citiesSlovakia.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {

    }

    val listCountryType = object : TypeToken<kotlin.collections.List<CitySK>>() {}.type
    return Gson().fromJson(jsonString, listCountryType)
}

/*
data class Country(
    @SerializedName("dialCode") @Expose var dialCode: String? = null,
    @SerializedName("flag") @Expose var flag: String? = null,
    @SerializedName("isoCode") @Expose var isoCode: String? = null,
    @SerializedName("name") @Expose var name: String? = null
)

fun getCountryCode(context: Context): kotlin.collections.List<Country> {

    lateinit var jsonString: String
    try {
        jsonString = context.assets.open("countries/citiesSlovakia.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        var xd = ioException
    }

    val listCountryType = object : TypeToken<List<Country>>() {}.type
    return Gson().fromJson(jsonString, listCountryType)
}*/