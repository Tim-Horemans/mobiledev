package edu.ap.projecty.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import edu.ap.projecty.R
import edu.ap.projecty.admin.fragments.AddExamFragment
import edu.ap.projecty.admin.fragments.AddStudentFragment
import edu.ap.projecty.admin.fragments.ManageStudentFragment
import edu.ap.projecty.databinding.ActivityHomeAdminBinding
import edu.ap.projecty.databinding.HomeUserActivityBinding

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        signInTestUser()

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

    private fun signInTestUser() {
        val email = "123@lol.com"
        val password = "123456"

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Auth", "signInWithEmail:success, user: ${user?.email}")
                } else {
                    Log.w("Auth", "signInWithEmail:failure", task.exception)
                }
            }
    }
}