package edu.ap.projecty.admin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.ap.projecty.R
import edu.ap.projecty.databinding.FragmentOpenQuestionBinding


class OpenQuestion : Fragment() {
    private var _binding: FragmentOpenQuestionBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenQuestionBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    fun getResult(): String {
        val questionResult = _binding?.inputQuestion?.text
        return questionResult.toString()
    }
}