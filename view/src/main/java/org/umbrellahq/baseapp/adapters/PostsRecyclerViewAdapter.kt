package org.umbrellahq.baseapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_task.view.*
import org.umbrellahq.baseapp.R
import org.umbrellahq.baseapp.models.PostViewEntity

class PostsRecyclerViewAdapter(var posts: Array<PostViewEntity>) : RecyclerView.Adapter<PostsRecyclerViewAdapter.TaskViewHolder>() {
    var onClick: ((post: PostViewEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
            TaskViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.row_task,
                            parent,
                            false
                    )
            )

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = posts[position]

        holder.tvTitle.text = item.title
        holder.tvAuthor.text = "Author: ${item.author}"

        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(16))
        Glide.with(holder.itemView).load(item.thumbnailUrl).apply(requestOptions).into(holder.ivThumb)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.tvTitle
        var tvAuthor: TextView = view.tvAuthor
        val ivThumb: ImageView = view.imageView
    }
}