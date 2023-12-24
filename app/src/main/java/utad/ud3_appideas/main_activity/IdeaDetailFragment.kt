package utad.ud3_appideas.main_activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utad.ud3_appideas.databinding.FragmentIdeaDetailBinding
import utad.ud3_appideas.room.IdeaApplication
import utad.ud3_appideas.room.models.Detail
import utad.ud3_appideas.room.models.Idea

class IdeaDetailFragment : Fragment() {

    private lateinit var _binding : FragmentIdeaDetailBinding
    private val binding: FragmentIdeaDetailBinding get() = _binding

    private val args: IdeaDetailFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCardView()
        comprobacionCheckbox("Prioridad",binding.chbAlta, binding.chbMedia, binding.chbBaja)
        comprobacionCheckbox("Progreso", binding.chbProceso, binding.chbPendiente, binding.chbTerminada)
        val selectedImage : Bitmap

        binding.btnUpdIdeaDetail.setOnClickListener {

            val detalle = binding.tietUpdIdeaDetail.text.toString()
            if(!detalle.isNullOrEmpty()){
                updateDetail(args.ideaId, detalle)
            }
            else{
                Toast.makeText(requireContext(), "Rellena el campo de arriba para añadir un detalle", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnUpdIdea.setOnClickListener {

            val priority= getCheckboxSelected(binding.chbAlta, binding.chbMedia, binding.chbBaja)
            val progress= getCheckboxSelected(binding.chbProceso, binding.chbPendiente, binding.chbTerminada)

            if(!priority.isNullOrEmpty() && !progress.isNullOrEmpty()){
                updateIdea(priority, progress)
                goToIdeaListFragment()
            }
            else{
                Toast.makeText(requireContext(), "Es necesario que los checkbox estén marcados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDetail(idIdea: Int, detalle: String) {
        lifecycleScope.launch(Dispatchers.IO){
            val application = (requireActivity().application as IdeaApplication)
            val newDetail = Detail(
                id = 0,
                ideaId = idIdea,
                detail = detalle
            )
            application.dataBase.ideaDao().addDetail(newDetail)
            }
    }



    private fun setCardView() {
        lifecycleScope.launch(Dispatchers.IO){
            val application = (requireActivity().application as IdeaApplication)
            application.dataBase.ideaDao().getIdea(args.ideaId).collect{relation ->
                    withContext(Dispatchers.Main){
                        binding.ivIdeaImageDetail.setImageBitmap(relation.idea.image)
                        binding.tvIdeaNameDetail.text = relation.idea.name
                        binding.tvIdeaDetailDescription.text = relation.idea.description
                        setInitialCheckbox(relation.idea.priority, binding.chbAlta, binding.chbMedia, binding.chbBaja)
                        setInitialCheckbox(relation.idea.progress, binding.chbProceso, binding.chbPendiente, binding.chbTerminada)
                        binding.tvDetailList.text = setDetailList(relation.detailList)
                    }


            }

        }
    }

    private fun setDetailList(detailList: List<Detail>?): CharSequence? {
        var texto :String = "Lista de detalles:"
        if(detailList != null){
            for (detail: Detail in detailList){
                val detalle = detail.detail
                texto = "$texto \n- $detalle"
            }
        }

        return texto
    }

    private fun setInitialCheckbox(categoria :String, check1 : CheckBox, check2 : CheckBox, check3 : CheckBox){
        if (check1.text.equals(categoria)){
            check1.isChecked = true
        }
        if (check2.text.equals(categoria)){
            check2.isChecked = true
        }
        if (check3.text.equals(categoria)){
            check3.isChecked = true
        }
    }

    private fun comprobacionCheckbox( categoria: String, checkbox1: CheckBox, checkbox2: CheckBox, checkbox3: CheckBox) {

        checkbox1.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                checkbox2.isChecked = false
                checkbox3.isChecked = false

            }
        }
        checkbox2.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                checkbox1.isChecked = false
                checkbox3.isChecked = false
            }
        }
        checkbox3.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                checkbox2.isChecked = false
                checkbox1.isChecked = false
            }
        }
    }

    private fun getCheckboxSelected(checkbox1: CheckBox, checkbox2: CheckBox, checkbox3: CheckBox): String?{
        var seleccion :String? = null

        if(checkbox1.isChecked){
            seleccion = checkbox1.text.toString()
        }
        if(checkbox2.isChecked){
            seleccion = checkbox2.text.toString()
        }
        if(checkbox3.isChecked){
            seleccion = checkbox3.text.toString()
        }

        return seleccion
    }

    private fun updateIdea(newPriority: String, newProgress: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            val application = (requireActivity().application as IdeaApplication)
            application.dataBase.ideaDao().getIdea(args.ideaId).collect(){oldIdea->
                val newIdea = Idea(
                    id = oldIdea.idea.id,
                    name = oldIdea.idea.name,
                    priority = newPriority,
                    progress = newProgress,
                    description = oldIdea.idea.description,
                    image = oldIdea.idea.image
                )

                application.dataBase.ideaDao().updateIdea(newIdea)
            }

        }
    }



    private fun goToIdeaListFragment() {
        findNavController().popBackStack()
    }
}