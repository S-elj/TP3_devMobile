package selj.umontpellier.fragmentapp.data

import kotlinx.serialization.Serializable

@Serializable
data class SavedPersonalInformation(
    val nom: String,
    val prenom: String,
    val tel: String,
    val mail: String,
    var birth: String = "",
    var activites: List<String> = emptyList()
) {
    fun censure() = apply {
        birth = ""
        activites = emptyList()
    }
}