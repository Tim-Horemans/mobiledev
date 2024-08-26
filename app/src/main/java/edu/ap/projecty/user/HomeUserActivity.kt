package edu.ap.projecty.user

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import edu.ap.projecty.R
import edu.ap.projecty.databinding.HomeUserActivityBinding
import edu.ap.projecty.repository.StudentViewModel
import edu.ap.projecty.user.fragments.OverviewExamsFragment

class HomeUserActivity : AppCompatActivity() {

    private lateinit var binding : HomeUserActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomeUserActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //temp nav
        binding.navExams.setOnClickListener {
            replaceFragment(OverviewExamsFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout3, fragment)
            .commit()
    }

}