package selj.umontpellier.fragmentapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import selj.umontpellier.fragmentapp.data.SavedPersonalInformation
import selj.umontpellier.fragmentapp.service.FileService

class InputFragment : Fragment() {

    private val dataFetchReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                FileService.ACTION_DATA_FETCH_SUCCESS -> {
                    val info =
                        Json.decodeFromString<SavedPersonalInformation>(intent.getStringExtra("personalData") ?: return)
                    view?.findViewById<EditText>(R.id.nom)?.setText(info.nom)
                    view?.findViewById<EditText>(R.id.prenom)?.setText(info.prenom)
                    view?.findViewById<EditText>(R.id.tel)?.setText(info.tel)
                    view?.findViewById<EditText>(R.id.mail)?.setText(info.mail)
                }

                FileService.ACTION_DATA_FETCH_ERROR -> {
                    Toast.makeText(context, "Vous êtes sûr de votre lien? >:(", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gonfle le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_saisie, container, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {


        // Sauvegarde
        view.findViewById<Button>(R.id.btn_import).setOnClickListener {
            val intent = Intent(context, FileService::class.java)
            context?.startService(intent)
            // Enregistrement du BroadcastReceiver pour écouter les diffusions du service
            val intentFilter = IntentFilter().apply {
                addAction(FileService.ACTION_DATA_FETCH_SUCCESS)
                addAction(FileService.ACTION_DATA_FETCH_ERROR)
            }
            requireActivity().registerReceiver(dataFetchReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        }
        view.findViewById<Button>(R.id.btn_submit).setOnClickListener {
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
            val info = SavedPersonalInformation(
                view.findViewById<EditText>(R.id.nom).text.toString(),
                view.findViewById<EditText>(R.id.prenom).text.toString(),
                view.findViewById<EditText>(R.id.tel).text.toString(),
                view.findViewById<EditText>(R.id.mail).text.toString(),
                view.findViewById<EditText>(R.id.birth).text.toString(),
                centresInteret
            )

            // Création et affichage du Fragment 2 avec les données
            val affichageFragment = DisplayFragment().apply {
                arguments = Bundle().apply {
                    putString("personalData", Json.encodeToString(info))
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, affichageFragment)
                .commit()
        }

        var info: SavedPersonalInformation? = null
        try {
            val file = context?.openFileInput("data.json")?.bufferedReader()?.readText()
            info = Json.decodeFromString<SavedPersonalInformation>(
                file ?: arguments?.getString("personalData") ?: return
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (info == null) return

        view.findViewById<EditText>(R.id.nom).setText(info.nom)
        view.findViewById<EditText>(R.id.prenom).setText(info.prenom)
        view.findViewById<EditText>(R.id.tel).setText(info.tel)
        view.findViewById<EditText>(R.id.mail).setText(info.mail)
    }

}