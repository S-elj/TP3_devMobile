package selj.umontpellier.fragmentapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import selj.umontpellier.fragmentapp.data.SavedPersonalInformation
import java.io.IOException

class DisplayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gonfle le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_affichage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val info = Json.decodeFromString<SavedPersonalInformation>(
            arguments?.getString("personalData") ?: return
        )
        view.findViewById<TextView>(R.id.tvNom).text = info.nom
        view.findViewById<TextView>(R.id.tvPrenom).text = info.prenom
        view.findViewById<TextView>(R.id.tvTel).text = info.tel
        view.findViewById<TextView>(R.id.tvMail).text = info.mail
        view.findViewById<TextView>(R.id.tvBirth).text = info.birth
        view.findViewById<TextView>(R.id.tvCentreInteret).text = info.activites.joinToString(", ")

        // Récupération des données du bundle
        view.findViewById<Button>(R.id.btnValider).setOnClickListener {
            try {
                val file = context?.openFileOutput("data.json", Context.MODE_PRIVATE)
                if (file == null) {
                    Toast.makeText(context, "Le fichier n'a pas pu être ouvert!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                file.write(Json.encodeToString(info.censure()).toByteArray())
                Toast.makeText(context, "Données sauvegardées", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show()
            }
        }


        view.findViewById<Button>(R.id.btnRetour).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InputFragment())
                .commit()
        }
    }
}