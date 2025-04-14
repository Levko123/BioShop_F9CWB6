package com.bioshop.bioshop

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Firebase inicializálása
        auth = FirebaseAuth.getInstance()

        val editTextEmail = findViewById<EditText>(R.id.editTextRegEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextRegPassword)
        val editTextPasswordConfirm = findViewById<EditText>(R.id.editTextRegPasswordConfirm)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val textViewRegSuccess = findViewById<TextView>(R.id.textViewRegSuccess)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        // Vissza gomb - vissza a bejelentkező felületre
        buttonBack.setOnClickListener {
            finish()
        }

        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val passwordConfirm = editTextPasswordConfirm.text.toString().trim()

            // Email formátum ellenőrzés: ellenőrizzük, hogy az email tartalmaz-e legalább egy '@'-ot
            if (!email.contains("@")) {
                Toast.makeText(this, "Érvénytelen email cím! Az emailnek tartalmaznia kell '@'-ot.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ellenőrizzük, hogy a két jelszó egyezik-e
            if (password != passwordConfirm) {
                Toast.makeText(this, "A jelszavak nem egyeznek!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Töltsd ki az összes mezőt!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase regisztráció
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show()

                        // Animáció: A sikeres regisztrációs üzenet csússzon be
                        // Először állítsuk láthatóra, majd animáljuk
                        textViewRegSuccess.visibility = View.VISIBLE
                        // Kezdetben helyezzük el balról eltolva (például -300 pixelrel)
                        textViewRegSuccess.translationX = -300f
                        textViewRegSuccess.alpha = 0f
                        // Animate: visszaugrik a 0f pozícióba, miközben az alpha 1-re emelkedik
                        textViewRegSuccess.animate()
                            .translationX(0f)
                            .alpha(1f)
                            .setDuration(1000)
                            .start()

                        // Ha szeretnéd, később visszanavigálhatsz (pl. finish() hívással)
                        // vagy várakozhatsz egy pár másodpercet, majd automatikusan visszatérhetsz:
                        // Handler(Looper.getMainLooper()).postDelayed({ finish() }, 2500)
                    } else {
                        Toast.makeText(this, "Hiba: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
