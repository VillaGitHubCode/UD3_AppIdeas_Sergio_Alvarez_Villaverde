package utad.ud3_appideas.main_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utad.ud3_appideas.R
import utad.ud3_appideas.data_store.DataStoreManager
import utad.ud3_appideas.data_store.dataStore
import utad.ud3_appideas.databinding.FragmentAccountInfoBinding
import utad.ud3_appideas.initial_activity.InitialActivity

class AccountInfoFragment : Fragment() {

    private lateinit var _binding: FragmentAccountInfoBinding
    private val binding: FragmentAccountInfoBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getUser(requireContext()).collect { user ->
                withContext(Dispatchers.Main){
                    binding.tvUsername.text = user.toString().trim()
                }

            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getPassword(requireContext()).collect { password ->
                withContext(Dispatchers.Main){
                    binding.tvPassword.text = "Contraseña: $password"
                }

            }
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataStoreManager.setUserNotLogged(requireContext())
                withContext(Dispatchers.Main){
                    goToInitialActivity()
                }

            }

        }

        binding.btnDeleteAccount.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                DataStoreManager.deleteUser(requireContext())
                withContext(Dispatchers.Main){

                    goToInitialActivity()

                }
            }

        }

    }


    private fun goToInitialActivity() {
        requireActivity().finish()
        val intent = Intent(requireContext(), InitialActivity::class.java)
        startActivity(intent)
    }

}