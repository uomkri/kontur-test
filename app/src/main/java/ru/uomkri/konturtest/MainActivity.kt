package ru.uomkri.konturtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(findViewById(R.id.toolbar))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}