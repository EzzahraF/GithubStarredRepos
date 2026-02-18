package com.example.githubstarredrepos
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubstarredrepos.databinding.ActivityMainBinding
import com.example.githubstarredrepos.presentation.ui.components.LoadingStateAdapter
import com.example.githubstarredrepos.presentation.ui.main.ReposAdapter
import com.example.githubstarredrepos.presentation.ui.main.ReposViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // ─── VIEW BINDING ────────────────────────────────────────────
    private lateinit var binding: ActivityMainBinding

    // ─── VIEW MODEL ──────────────────────────────────────────────
    private val viewModel: ReposViewModel by viewModels()

    // ─── ADAPTER ─────────────────────────────────────────────────
    private lateinit var reposAdapter: ReposAdapter

    // ─── LIFECYCLE ───────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialiser ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Configurer la toolbar
        setupToolbar()

        // 3. Configurer le RecyclerView et l'Adapter
        setupRecyclerView()

        // 4. Observer les données du ViewModel
        observeRepositories()

        // 5. Observer les états de chargement
        observeLoadStates()
    }

    // ─── SETUP ───────────────────────────────────────────────────

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {

        // Créer l'adapter
        reposAdapter = ReposAdapter()

        // Configurer le RecyclerView
        binding.recyclerViewRepos.apply {

            // Ajouter l'adapter + le footer de chargement
            adapter = reposAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { reposAdapter.retry() }
            )

            // Séparateur entre les items
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
        }
    }

    // ─── OBSERVERS ───────────────────────────────────────────────

    private fun observeRepositories() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repositories.collectLatest { pagingData ->
                    reposAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeLoadStates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reposAdapter.loadStateFlow.collectLatest { loadStates ->
                    // Afficher le spinner central seulement au premier chargement
                    binding.progressBar.isVisible =
                        loadStates.refresh is LoadState.Loading
                }
            }
        }
    }
}
