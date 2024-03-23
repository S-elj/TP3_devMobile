package selj.umontpellier.fragmentapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SaisieFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gonfle le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_saisie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val shouldPrefill = arguments?.getBoolean("shouldPrefill", false) ?: false
        if (shouldPrefill) {
            prefillData()
        }


        view.findViewById<Button>(R.id.btn_soumettre).setOnClickListener {
            val editTexts = mutableListOf(
                view.findViewById<EditText>(R.id.nom),
                view.findViewById<EditText>(R.id.prenom),
                view.findViewById<EditText>(R.id.birth),
                view.findViewById<EditText>(R.id.tel),
                view.findViewById<EditText>(R.id.mail),
            )

            val bundle = Bundle()
            var isAllFieldsFilled = true

            editTexts.forEach { editText ->
                if (editText.text.isNotEmpty()) {
                    editText.setBackgroundColor(Color.GREEN)
                    bundle.putString(editText.getTag().toString(), editText.text.toString())
                } else {
                    isAllFieldsFilled = false
                    editText.setBackgroundColor(Color.RED)
                }
            }

            val centresInteret = mutableListOf<String>()
            if (view.findViewById<CheckBox>(R.id.centre_interet_sport).isChecked) {
                centresInteret.add("Sport")
            }
            if (view.findViewById<CheckBox>(R.id.centre_interet_musique).isChecked) {
                centresInteret.add("Musique")
            }
            if (view.findViewById<CheckBox>(R.id.centre_interet_lecture).isChecked) {
                centresInteret.add("Lecture")
            }

            val centresInteretStr = centresInteret.joinToString(", ")

            bundle.putString("centresInteret", centresInteretStr)

            if (isAllFieldsFilled) {

                if (isAllFieldsFilled) {
                    // Création et affichage du Fragment 2 avec les données
                    val affichageFragment = AffichageFragment().apply {
                        arguments = bundle
                    }
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, affichageFragment)
                        ?.commit()
                }
            }
        }
    }
    private fun prefillData() {
        try {
            val filename = "userData.json"
            val fileInput = context?.openFileInput(filename)
            val inputStreamReader = InputStreamReader(fileInput)
            val stringBuilder = StringBuilder()
            val bufferedReader = BufferedReader(inputStreamReader)
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            val jsonData = JSONObject(stringBuilder.toString())
            val nom = jsonData.getString("nom")
            val prenom = jsonData.getString("prenom")
            val tel = jsonData.getString("tel")

            view?.findViewById<EditText>(R.id.nom)?.setText(nom)
            view?.findViewById<EditText>(R.id.prenom)?.setText(prenom)
            view?.findViewById<EditText>(R.id.tel)?.setText(tel)

        } catch (e: IOException) {
            // Gestion des erreurs
        } catch (e: JSONException) {
            // Gestion des erreurs JSON
        }
    }
}
