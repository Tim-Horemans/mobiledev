import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import edu.ap.projecty.R
import edu.ap.projecty.model.Exam

class ExamListAdapter(
    private val dataSet: List<Exam>,
    private val onItemClick: (Exam) -> Unit
) : RecyclerView.Adapter<ExamListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonExam: Button = view.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_button, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val exam = dataSet[position]
        viewHolder.buttonExam.text = exam.name

        viewHolder.buttonExam.setOnClickListener {
            onItemClick(exam)
        }
    }

    override fun getItemCount() = dataSet.size
}