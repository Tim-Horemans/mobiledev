package edu.ap.projecty.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import edu.ap.projecty.R
import edu.ap.projecty.admin.fragments.AddExamFragment
import edu.ap.projecty.admin.fragments.AddStudentFragment
import edu.ap.projecty.admin.fragments.ManageStudentFragment
import edu.ap.projecty.databinding.ActivityHomeAdminBinding
import edu.ap.projecty.databinding.HomeUserActivityBinding

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addExam.setOnClickListener {
            replaceFragment(AddExamFragment())
        }

        binding.studentNav.setOnClickListener {
            replaceFragment(ManageStudentFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout4, fragment)
            .commit()
    }


}