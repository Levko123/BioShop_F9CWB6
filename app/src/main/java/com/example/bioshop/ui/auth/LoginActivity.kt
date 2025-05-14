package com.example.bioshop.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bioshop.R
import com.example.bioshop.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // FirebaseAuth példány
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Animáció: A BioShop felirat fade‑in
        val textViewAppTitle = findViewById<TextView>(R.id.textViewAppTitle)
        textViewAppTitle.animate().alpha(1f).setDuration(1500).start()

        // Firebase inicializálása
        auth = FirebaseAuth.getInstance()

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonGoToRegister = findViewById<Button>(R.id.buttonGoToRegister)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Töltsd ki az összes mezőt!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase bejelentkezés
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sikeres bejelentkezés
                        Toast.makeText(this, "Sikeres belépés!", Toast.LENGTH_SHORT).show()
                        // Itt indíthatsz tovább egy főképernyőre vezető activity-t, pl. ProductListActivity
                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // ne tudjon visszalépni a Login képernyőre
                    } else {
                        // Ellenőrizzük az exception üzenetét (például, ha a felhasználó nem létezik)
                        Toast.makeText(this, "Felhasználó nem létezik!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Regisztráció gomb: indítja a RegisterActivity-t
        buttonGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
