package edu.ap.projecty.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import edu.ap.projecty.R
import edu.ap.projecty.model.Exam
import edu.ap.projecty.model.Student

class StudentListAdapter (
    private var dataSet: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonExam: Button = view.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_button, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val student = dataSet[position]
        viewHolder.buttonExam.text = student.name

        viewHolder.buttonExam.setOnClickListener {
            onItemClick(student)
        }
    }
    fun updateStudents(newStudents: List<Student>) {
        dataSet = newStudents
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}