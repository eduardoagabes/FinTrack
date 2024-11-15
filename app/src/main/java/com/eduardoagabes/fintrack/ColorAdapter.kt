import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eduardoagabes.fintrack.R

class ColorAdapter(
    private val colors: List<Int>,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewColor: View = itemView.findViewById(R.id.view_color)

        fun bind(color: Int) {
            val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.color_circle_bg)

            drawable?.mutate()?.let {
                (it as GradientDrawable).setColor(color)
                viewColor.background = it
            }

            viewColor.alpha = if (adapterPosition == selectedPosition) 1.0f else 0.5f

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    onColorSelected(colors[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position])
    }

    override fun getItemCount() = colors.size
}
