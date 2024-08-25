package edu.ap.projecty.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ap.projecty.R
import edu.ap.projecty.model.Student

class SelectUserAdapter(
    private var studentList: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<SelectUserAdapter.StudentViewHolder>() {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studentName: TextView = view.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_button, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.studentName.text = student.name
        holder.itemView.setOnClickListener {
            onItemClick(student)
        }
    }

    override fun getItemCount() = studentList.size

    fun updateStudents(newStudents: List<Student>) {
        studentList = newStudents
        notifyDataSetChanged()
    }
}