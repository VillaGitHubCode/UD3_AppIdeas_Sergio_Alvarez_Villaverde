package utad.ud3_appideas.initial_activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import utad.ud3_appideas.databinding.ActivityInitialBinding



class InitialActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityInitialBinding
    private val binding: ActivityInitialBinding get()= _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}