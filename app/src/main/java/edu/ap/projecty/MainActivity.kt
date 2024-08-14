package edu.ap.projecty

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import edu.ap.projecty.admin.AddExam
import edu.ap.projecty.databinding.MainLayoutBinding
import edu.ap.projecty.user.OverviewExams


class MainActivity : ComponentActivity()  {

    private lateinit var mainBinding: MainLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = MainLayoutBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.button.setOnClickListener {
            val intent = Intent(this@MainActivity, OverviewExams::class.java)
            startActivity(intent)
            finish()
        }

        mainBinding.button2.setOnClickListener {
            val intent = Intent(this@MainActivity, AddExam::class.java)
            startActivity(intent)
            finish()
        }
    }
}

