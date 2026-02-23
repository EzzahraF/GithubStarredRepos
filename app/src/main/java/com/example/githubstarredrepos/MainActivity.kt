package com.example.githubstarredrepos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.githubstarredrepos.databinding.ActivityMainBinding
import com.example.githubstarredrepos.presentation.ui.components.LoadingStateAdapter
import com.example.githubstarredrepos.presentation.ui.main.ReposAdapter
import com.example.githubstarredrepos.presentation.ui.main.ReposViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.githubstarredrepos.data.remote.dto.OwnerDto
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ReposViewModel by viewModels()
    private lateinit var reposAdapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeRepositories()
        observeLoadStates()
        observeUiState()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "Setting up RecyclerView")
        reposAdapter = ReposAdapter { repo ->
            val intent = Intent(this, RepoDetailActivity::class.java).apply {
                putExtra("owner", repo.ownerUsername)
                putExtra("name", repo.name)
            }
            startActivity(intent)
        }
        binding.recyclerViewRepos.apply {
            adapter = reposAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { reposAdapter.retry() }
            )

            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun observeRepositories() {
        Log.d(TAG, "Starting to observe repositories")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repositories.collectLatest { pagingData ->
                    Log.d(TAG, "Received new PagingData")
                    reposAdapter.submitData(pagingData)
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Gonfle le menu; cela ajoute les items à la Toolbar
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Gère le clic sur un item du menu
        return when (item.itemId) {
            R.id.action_settings -> {
                // Ouvre la SettingsActivity
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun observeLoadStates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reposAdapter.loadStateFlow.collectLatest { loadStates ->
                    val isLoading = loadStates.refresh is LoadState.Loading
                    Log.d(TAG, "Load state - Loading: $isLoading")

                    binding.progressBar.isVisible = isLoading

                    when {
                        loadStates.refresh is LoadState.Error -> {
                            val error = (loadStates.refresh as LoadState.Error).error
                            Log.e(TAG, "Error loading repos: ${error.message}", error)
                            viewModel.onError(error)
                        }
                        loadStates.refresh is LoadState.NotLoading -> {
                            viewModel.onDataLoaded(reposAdapter.itemCount)
                        }
                    }
                }
            }
        }
    }

    // ─── NOUVEAU : OBSERVER LE STATE FLOW ─────────────────────
    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        is ReposViewModel.UiState.Idle -> {
                            Log.d(TAG, "UiState: Idle")
                        }
                        is ReposViewModel.UiState.Loading -> {
                            Log.d(TAG, "UiState: Loading")
                        }
                        is ReposViewModel.UiState.Success -> {
                            Log.d(TAG, "UiState: Success (${state.itemCount} items)")
                        }
                        is ReposViewModel.UiState.Error -> {
                            Log.e(TAG, "UiState: Error - ${state.message}")
                            Toast.makeText(
                                this@MainActivity,
                                "Error: ${state.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}