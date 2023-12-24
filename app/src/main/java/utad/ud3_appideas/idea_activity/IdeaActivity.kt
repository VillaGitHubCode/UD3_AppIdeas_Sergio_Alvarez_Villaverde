package utad.ud3_appideas.idea_activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utad.ud3_appideas.databinding.ActivityIdeaBinding
import utad.ud3_appideas.room.models.Idea
import utad.ud3_appideas.main_activity.MainActivity
import utad.ud3_appideas.room.IdeaApplication

class IdeaActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityIdeaBinding
    private val binding: ActivityIdeaBinding get() = _binding


    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

            if (isGranted) {
                openGallery()
            } else {
                showPermissionDialog()
            }
        }

    private var settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            checkPermissions()
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selectedImage: Uri? = data.data
                    selectedPhoto = convertUriToBitmap(selectedImage)
                    binding.ivAddIdea.setImageBitmap(selectedPhoto)
                } else {
                    Toast.makeText(this, "No se ha escogido una imagen", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se ha escogido una imagen", Toast.LENGTH_SHORT).show()
            }
        }

    private var selectedPhoto: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        comprobacionCheckbox(binding.chbPendiente, binding.chbProceso, binding.chbTerminada)
        comprobacionCheckbox(binding.chbAlta, binding.chbMedia, binding.chbBaja)

        binding.btnSelectImage.setOnClickListener {
            checkPermissions()
        }

        binding.btnAddIdea.setOnClickListener {

            val name = binding.tietAddIdeaName.text.toString()
            val description = binding.tietAddIdeaDescription.text.toString()
            val progress = getCheckboxSelected(binding.chbPendiente, binding.chbProceso, binding.chbTerminada)
            val priority = getCheckboxSelected(binding.chbAlta, binding.chbMedia, binding.chbBaja)
            val image = selectedPhoto


            if(!progress.isNullOrEmpty() && !priority.isNullOrEmpty() && !name.isNullOrEmpty() && !description.isNullOrEmpty()){
                if(image!=null){
                    crearIdea(name, description, progress, priority, image)
                    goToMainActivity()
                }
                else{
                    Toast.makeText(this, "Es obligatorio escoger una foto", Toast.LENGTH_SHORT).show()
                }


            }else{
                Toast.makeText(this, "Es obligatorio rellenar todos los campos", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getImagePermission(): String {
        if (Build.VERSION.SDK_INT >= 33) {
            return Manifest.permission.READ_MEDIA_IMAGES
        } else {
            return Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun checkPermissions() {

        val permission = getImagePermission()

        val isPermissionGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

        val shouldRequestRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

        if (isPermissionGranted) {
            openGallery()
        } else if (shouldRequestRationale) {
            showPermissionDialog()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permiso denegado")
            .setMessage("Es necesario acceder a tus archivos para cargar una imagen")
            .setPositiveButton("Ir a ajustes") { _, _ ->
                goToSettings()
            }.setNegativeButton("Cancelar") { _, _ ->
                finish()
            }.show()
    }

    private fun goToSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + application.packageName)
        settingsLauncher.launch(intent)
    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        intent.type = "image/*"

        imageLauncher.launch(intent)
    }

    private fun convertUriToBitmap(uri: Uri?): Bitmap? {

        try {
            val inputStream = contentResolver.openInputStream(uri!!)
            val image = BitmapFactory.decodeStream(inputStream)
            if (image.byteCount <= 2500000) {
                return image
            } else {
                var compressedImage = image
                do {
                    var scaleWidth = compressedImage.width / 2
                    var scaleHeight = compressedImage.height / 2

                    compressedImage =
                        Bitmap.createScaledBitmap(image, scaleWidth, scaleHeight, true)
                } while (compressedImage.byteCount >= 2500000)
                return compressedImage
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun comprobacionCheckbox(checkbox1: CheckBox, checkbox2: CheckBox, checkbox3: CheckBox) {
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

    private fun crearIdea(name :String, description :String, progress :String, priority :String, image: Bitmap ) {
        val application = this.application as IdeaApplication
        lifecycleScope.launch(Dispatchers.IO){
            val newIdea = Idea(
                id = 0,
                name = name,
                priority = priority,
                progress = progress,
                description = description,
                image = image
            )

            application.dataBase.ideaDao().addIdea(newIdea)
        }


    }

    private fun goToMainActivity() {
        this.finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}