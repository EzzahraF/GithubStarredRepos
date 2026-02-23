package com.example.githubstarredrepos

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.githubstarredrepos.databinding.ActivityRepoDetailBinding
import com.example.githubstarredrepos.presentation.ui.details.RepoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityRepoDetailBinding
    private val viewModel: RepoDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is RepoDetailViewModel.DetailState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is RepoDetailViewModel.DetailState.Success -> {
                        binding.progressBar.isVisible = false
                        val repo = state.repo

                        // --- Remplissage des vues avec les BONS IDs ---

                        // Nom du repo
                        binding.tvRepoName.text = repo.name

                        // Nom du owner
                        binding.tvOwnerName.text = repo.ownerUsername

                        // Description
                        binding.tvDescription.text = repo.description

                        // Statistiques
                        binding.tvStarsCount.text = formatCount(repo.stars)
                        binding.tvForksCount.text = formatCount(repo.forks)

                        // Langage (gestion de la visibilité)
                        if (!repo.language.isNullOrEmpty()) {
                            binding.tvLanguage.text = repo.language
                            binding.tvLanguage.visibility = View.VISIBLE
                        } else {
                            binding.tvLanguage.visibility = View.GONE
                        }

                        // Image Avatar
                        Glide.with(this@RepoDetailActivity)
                            .load(repo.ownerAvatarUrl)
                            .circleCrop()
                            .placeholder(R.drawable.ic_avatar_placeholder)
                            .into(binding.ivOwnerAvatar)
                    }
                    is RepoDetailViewModel.DetailState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.tvDescription.text = "Erreur: ${state.message}"
                    }
                }
            }
        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fk", count / 1_000.0)
            else -> count.toString()
        }
    }
}