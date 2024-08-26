package edu.ap.projecty

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.ap.projecty.admin.HomeAdminActivity
import edu.ap.projecty.admin.LoginActivity
import edu.ap.projecty.databinding.ActivityStartBinding
import edu.ap.projecty.user.HomeUserActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button8.setOnClickListener {
            val intent = Intent(this@StartActivity, HomeUserActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.button2.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}