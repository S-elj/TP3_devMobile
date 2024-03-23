package selj.umontpellier.fragmentapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.io.IOException

class AffichageFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gonfle le layout pour ce fragment
        return inflater.inflate(R.layout.fragment_affichage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupération des données du bundle
        arguments?.let { bundle ->
            view.findViewById<TextView>(R.id.tvNom).text = bundle.getString("nom", "")
            view.findViewById<TextView>(R.id.tvPrenom).text = bundle.getString("prenom", "")
            view.findViewById<TextView>(R.id.tvBirth).text = bundle.getString("birth", "")
            view.findViewById<TextView>(R.id.tvTel).text = bundle.getString("tel", "")
            view.findViewById<TextView>(R.id.tvMail).text = bundle.getString("mail", "")
            view.findViewById<TextView>(R.id.tvCentreInteret).text = bundle.getString("centresInteret", "")

            val jsonSaver = JSONObject().apply {
                put("nom", bundle.getString("nom", ""))
                put("prenom", bundle.getString("prenom", ""))
                put("birth", bundle.getString("birth", ""))
                put("tel", bundle.getString("tel", ""))
                put("mail", bundle.getString("mail", ""))
                put("centresInteret", bundle.getString("centresInteret", ""))
                // Ajoutez les autres champs ici

            }


                view.findViewById<Button>(R.id.btnValider).setOnClickListener {
                    try {
                        val filename = "userData.json"
                        val fileOutput = context?.openFileOutput(filename, Context.MODE_PRIVATE)
                        fileOutput?.write(jsonSaver.toString().toByteArray())
                        fileOutput?.close()

                        Toast.makeText(context, "Données sauvegardées", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(context, "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show()
                    }
                }


            }

        view.findViewById<Button>(R.id.btnRetour).setOnClickListener {
            val formFragment = SaisieFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("shouldPrefill", true)
                }
            }
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, formFragment)
                ?.commit()
        }


        }

    }
