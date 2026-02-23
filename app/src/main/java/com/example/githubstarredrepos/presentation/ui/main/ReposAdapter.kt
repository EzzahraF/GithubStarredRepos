package com.example.githubstarredrepos.presentation.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubstarredrepos.R
import com.example.githubstarredrepos.databinding.ItemRepositoryBinding
import com.example.githubstarredrepos.domain.model.Repository

class ReposAdapter(
    private val onItemClick:(Repository) -> Unit
) : PagingDataAdapter<Repository, ReposAdapter.RepositoryViewHolder>(REPO_COMPARATOR) {

    // CRÉATION DE LA VUE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = ItemRepositoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepositoryViewHolder(binding)
    }

    // ─── REMPLISSAGE DE LA VUE ───────────────────────────────────
    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repo = getItem(position)

        repo?.let { repoItem ->
            holder.bind(repoItem)

            // Listener
            holder.itemView.setOnClickListener {
                onItemClick(repoItem)
            }
        }
    }

    // ─── VIEW HOLDER ─────────────────────────────────────────────
    class RepositoryViewHolder(
        private val binding: ItemRepositoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repository: Repository) {
            binding.apply {

                // Textes
                textViewRepoName.text    = repository.name
                textViewOwnerName.text   = repository.ownerUsername
                textViewDescription.text = repository.description
                textViewStars.text       = formatStarCount(repository.stars)

                // Avatar avec Glide
                Glide.with(imageViewAvatar.context)
                    .load(repository.ownerAvatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .error(R.drawable.ic_avatar_placeholder)
                    .into(imageViewAvatar)
            }
        }

        private fun formatStarCount(stars: Int): String {
            return when {
                stars >= 1_000_000 -> String.format("%.1fM", stars / 1_000_000.0)
                stars >= 1_000     -> String.format("%.1fk", stars / 1_000.0)
                else               -> stars.toString()
            }
        }
    }

    //DIF CALLBACK
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repository>() {

            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem == newItem
            }
        }
    }
}