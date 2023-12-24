package utad.ud3_appideas.initial_activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utad.ud3_appideas.R
import utad.ud3_appideas.data_store.DataStoreManager
import utad.ud3_appideas.databinding.FragmentLoginBinding
import utad.ud3_appideas.main_activity.MainActivity

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launch(Dispatchers.IO) {
            checkIsUserLogged()
        }

        binding.btnToSignup.setOnClickListener{
            goToSignUpFragment()
        }

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

    }

    override fun onResume() {
        super.onResume()
        binding.tietUsername.text = null
        binding.tietPassword.text = null

        lifecycleScope.launch(Dispatchers.IO){
            checkIsUserLogged()
        }

    }

    private suspend fun checkIsUserLogged() {
        DataStoreManager.getIsUserLogged(requireContext()).collect { isUserLogged ->
            withContext(Dispatchers.Main){
                if (isUserLogged) {
                    goToMainActivity()
                }
            }
        }
    }


    private fun goToSignUpFragment(){

        val signUpFragment = SignUpFragment()

        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fcv_initial_activity, signUpFragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun doLogin() {
        val name = binding.tietUsername.text.toString().trim()
        val password = binding.tietPassword.text.toString().trim()

        if (!name.isNullOrEmpty() && !password.isNullOrEmpty()) {
            obtainUserAndPassword(name, password)
        } else {
            Toast.makeText(requireContext(), "Rellena todos los campos para poder iniciar sesión", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainUserAndPassword(name: String, passwordUser: String) {

        var isNameValid: Boolean? = null
        var isPasswordValid: Boolean? = null

        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getUser(requireContext()).collect { user ->
                isNameValid = user == name
            }
        }

       lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getPassword(requireContext()).collect { password ->
                isPasswordValid = passwordUser == password
            }
        }

        lifecycleScope.launch(Dispatchers.Main){
            checkCredentials(isNameValid, isPasswordValid)
        }
    }


    private fun checkCredentials(isNameValid: Boolean?, isPasswordValid: Boolean?) {
            if (isNameValid == true && isPasswordValid == true) {
                setUserLogged()
                goToMainActivity()
            } else if (isNameValid == false) {
                    Toast.makeText(requireContext(), "Nombre no válido", Toast.LENGTH_SHORT).show()
            } else if (isPasswordValid ==false) {
                    Toast.makeText(requireContext(), "Contraseña no válida", Toast.LENGTH_SHORT).show()
            }

    }

    private fun setUserLogged() {

        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.setUserLogged(requireContext())
        }
    }

    private fun goToMainActivity() {
        requireActivity().finish()
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }



}