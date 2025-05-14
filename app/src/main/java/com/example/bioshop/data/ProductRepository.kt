package com.example.bioshop.data

import android.graphics.Bitmap
import android.net.Uri
import com.example.bioshop.model.Product
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class ProductRepository {

    /* ---------------- Firestore --------------- */
    private val db   = Firebase.firestore
    private val col  = db.collection("products")

    /** Live összes termék – a ProductsFragment hallgatja. */
    fun liveAll(): Query                   = col
    fun create(p: Product)                 = col.add(p)
    fun update(p: Product)                 = col.document(p.id).set(p)
    fun delete(id: String)                 = col.document(id).delete()

    /* ---------------- 3 komplex lekérdezés --------------- */
    fun byCategory(cat: String):  Query =
        col.whereEqualTo("category", cat)

    fun byPriceRange(min: Double, max: Double): Query =
        col.whereGreaterThanOrEqualTo("price", min)
            .whereLessThanOrEqualTo("price",  max)

    fun firstNOrderedByName(limit: Int):  Query =
        col.orderBy("name").limit(limit.toLong())

    /* ---------------- Kép-feltöltés (galéria / kamera) --------------- */
    private val storage = Firebase.storage.reference.child("productImages")

    suspend fun uploadBitmap(id: String, bmp: Bitmap): Uri {
        val path   = storage.child("$id.jpg")
        val bytes  = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            java.io.ByteArrayOutputStream().apply { bmp.compress(Bitmap.CompressFormat.JPEG, 90, this) }.toByteArray()
        }
        path.putBytes(bytes).await()
        return path.downloadUrl.await()
    }

    suspend fun updateImage(id: String, uri: Uri) {
        col.document(id).update("imageUrl", uri.toString()).await()
    }

    /* ---------------- DEMÓ minta-adat seed --------------- */
    suspend fun seedDemoProductsIfEmpty() {
        val snap = col.limit(1).get().await()
        if (snap.isEmpty) {
            val demo = listOf(
                Product(
                    name     = "Bio Zöld Tea",
                    price    = 1290.0,
                    category = "tea",
                    imageUrl = "https://picsum.photos/400?tea",
                    inStock  = true
                ),
                Product(
                    name     = "Mandulatej 1 l",
                    price    = 699.0,
                    category = "drink",
                    imageUrl = "https://picsum.photos/400?milk",
                    inStock  = true
                ),
                Product(
                    name     = "Quinoa 500 g",
                    price    = 1590.0,
                    category = "grain",
                    imageUrl = "https://picsum.photos/400?grain",
                    inStock  = false
                )
            )
            demo.forEach { create(it) }
        }
    }

    /* ---------------- Háttér-szinkron Workerhez --------------- */
    suspend fun syncWithServer() {
        // Ide jöhetne pl. REST → Firestore összefésülés.
        // Most csak naplózunk, hogy fusson a WorkManager.
        android.util.Log.d("SyncWorker", "Napi szinkron lefutott")
    }
}
