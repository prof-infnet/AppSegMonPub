package br.edu.infnet.appsegmonpub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.ImageView
import br.edu.infnet.appsegmonpub.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.webViewClient = WebViewClient()

        binding.webView.loadUrl("https://www.infnet.edu.br/")

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.settings.setSupportZoom(true)
    }

    // if you press Back button this code will work
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }

    fun callLoadImage(view: View) {
        val iv: ImageView = findViewById(R.id.iv_logo)

        Picasso.get()
            .load("https://www.infnet.edu.br/infnet/wp-content/uploads/sites/6/2018/01/logotipo.png")
            .into(iv)
    }
}