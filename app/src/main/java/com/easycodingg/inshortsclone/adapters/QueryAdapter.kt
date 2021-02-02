package com.easycodingg.inshortsclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easycodingg.inshortsclone.databinding.ListItemQueryBinding
import com.easycodingg.inshortsclone.models.Query

class QueryAdapter(
    private val queryList: List<Query>,
    private val onItemClickListener: (Query) -> Unit
): RecyclerView.Adapter<QueryAdapter.QueryViewHolder>() {

    inner class QueryViewHolder(private val binding: ListItemQueryBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClickListener(queryList[adapterPosition])
            }
        }

        fun bind(query: Query) {
            binding.tvName.text = query.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val binding = ListItemQueryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QueryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {

        val currentQuery = queryList[position]
        holder.bind(currentQuery)
    }

    override fun getItemCount() = queryList.size
}