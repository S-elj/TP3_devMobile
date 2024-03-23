package selj.umontpellier.fragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

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
            // Répétez pour les autres données si
        }

        view.findViewById<Button>(R.id.btnRetour).setOnClickListener {
                val formFragment = SaisieFragment()
                fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, formFragment)
                ?.commit()
        }

        }
}