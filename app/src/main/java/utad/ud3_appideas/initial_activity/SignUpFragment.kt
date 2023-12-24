package utad.ud3_appideas.initial_activity

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
import utad.ud3_appideas.R
import utad.ud3_appideas.data_store.DataStoreManager
import utad.ud3_appideas.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private lateinit var _binding: FragmentSignUpBinding
    private val binding :FragmentSignUpBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignupSignup.setOnClickListener{
            createUser()
        }
        binding.btnToLogin.setOnClickListener{
            navigateToLoginFragment()
        }

    }

    private fun createUser() {
        val userName = binding.tietUsernameSignup.text.toString().trim()
        val password = binding.tietPasswordSignup.text.toString().trim()
        val passwordCheck = binding.tietRepeatPasswordSignup.text.toString().trim()


        if (!userName.isNullOrEmpty() && !password.isNullOrEmpty() && !passwordCheck.isNullOrEmpty()) {

            if(password.equals(passwordCheck)){
                lifecycleScope.launch(Dispatchers.IO) {
                    DataStoreManager.saveUser(requireContext(), userName, password)
                }
                Toast.makeText(requireContext(), "El usuario se ha creado correctamente", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }else{
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "Todos los campos deben de ser rellenados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLoginFragment(){

        val loginFragment = LoginFragment()

        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fcv_initial_activity, loginFragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}