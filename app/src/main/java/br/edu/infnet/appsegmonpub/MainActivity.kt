package br.edu.infnet.appsegmonpub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun callLoadImage(view: View) {
        val iv: ImageView = findViewById(R.id.iv_logo)

        Picasso.get()
            .load("https://www.infnet.edu.br/infnet/wp-content/uploads/sites/6/2018/01/logotipo.png")
            .into(iv)
    }
}