package utad.ud3_appideas.main_activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utad.ud3_appideas.databinding.FragmentIdeaListBinding
import utad.ud3_appideas.idea_activity.IdeaActivity
import utad.ud3_appideas.room.IdeaApplication
import utad.ud3_appideas.room.models.Idea

class IdeaListFragment : Fragment() {

    private lateinit var _binding : FragmentIdeaListBinding
    private val binding : FragmentIdeaListBinding get() = _binding

    private val adapter: IdeaListAdapter = IdeaListAdapter(
        goToDetail = { ideaId: Int -> goToIdeaDetail(ideaId)},
        deleteIdea = { idea: Idea -> deleteIdea(idea) }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setReciclerView()
        binding.fabToAddIdea.setOnClickListener{
            goToIdeaActivity()
        }
    }


    override fun onResume() {
        super.onResume()
        getIdeasFromDatabase()
    }

    private fun getIdeasFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            val application = (requireActivity().application as IdeaApplication)
            val newList = application.dataBase.ideaDao().getAllIdeas()
            withContext(Dispatchers.Main) {
                adapter.submitList(newList)
            }
        }
    }

    private fun setReciclerView(){
        binding.rvIdeaList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIdeaList.adapter = adapter
    }

    private fun deleteIdea(idea: Idea) {
        val application = requireActivity().application as IdeaApplication
        lifecycleScope.launch(Dispatchers.IO) {
            application.dataBase.ideaDao().deleteIdea(idea)
            application.dataBase.ideaDao().deleteDetailByIdeaID(idea.id)
            getIdeasFromDatabase()
        }

    }



    private fun goToIdeaActivity(){
        requireActivity().finish()
        val intent = Intent(requireContext(), IdeaActivity::class.java)
        startActivity(intent)
    }

    private fun goToIdeaDetail(ideaId: Int) {
        val action = IdeaListFragmentDirections.actionIdeaListFragmentToIdeaDetailFragment(ideaId)
        findNavController().navigate(action)
    }

}