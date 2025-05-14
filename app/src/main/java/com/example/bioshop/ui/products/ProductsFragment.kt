package com.example.bioshop.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bioshop.data.ProductRepository
import com.example.bioshop.databinding.FragmentProductsBinding
import com.example.bioshop.model.Product
import com.example.bioshop.ui.detail.ProductDetailActivity
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {

    /* -------- adat + adapter -------- */
    private val repo by lazy { ProductRepository() }
    private val adapter = ProductAdapter { product: Product ->
        startActivity(
            Intent(requireContext(), ProductDetailActivity::class.java)
                .putExtra("id", product.id)
        )
    }

    /* -------- SEED csak első indításkor -------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { repo.seedDemoProductsIfEmpty() }
    }

    /* -------- UI -------- */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentProductsBinding.inflate(inflater, container, false)

        /* ----- RecyclerView beállítása ----- */
        binding.recyclerProducts.apply {

            // 1 oszlop telefonon, 2 oszlop tableten
            layoutManager = if (resources.configuration.smallestScreenWidthDp >= 600)
                GridLayoutManager(requireContext(), 2)
            else
                LinearLayoutManager(requireContext())

            adapter = this@ProductsFragment.adapter
        }

        /* ----- Élő Firestore monitor ----- */
        repo.liveAll().addSnapshotListener { snap, e ->
            if (e != null) {
                // hiba (pl. jogosultság) – ne dobjunk összeomlást
                Toast.makeText(context, e.message ?: "Firestore error", Toast.LENGTH_LONG).show()
                binding.swipeRefresh.isRefreshing = false
                return@addSnapshotListener
            }

            val list = snap?.toObjects(Product::class.java) ?: emptyList()
            adapter.submitList(list)
            binding.textEmpty.isVisible = list.isEmpty()      // üres-állapot szöveg
            binding.swipeRefresh.isRefreshing = false
        }

        /* ----- Pull-to-refresh ----- */
        binding.swipeRefresh.setOnRefreshListener {
            repo.liveAll().get()                              // friss lekérés
        }

        return binding.root
    }
}
