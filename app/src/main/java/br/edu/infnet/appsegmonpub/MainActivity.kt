package br.edu.infnet.appsegmonpub

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.core.content.ContextCompat
import br.edu.infnet.appsegmonpub.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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


    fun callWriteOnExternalStorage(view: View) {
        //Setar Permissões
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                callDialog(
                    "É preciso liberar o WRITE EXTERNAL STORAGE",
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                )

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_CODE
                )
            }
        } else {
            createDeleteFile()
        }
    }

    // Permissões -  INI
    private val REQUEST_PERMISSIONS_CODE = 12800

    private fun callDialog(message: String, permissions: Array<String>) {
        var mDialog = AlertDialog.Builder(this)
            .setTitle("Permissão")
            .setMessage((message))
            .setPositiveButton("OK")
            { dialog, id ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    permissions,
                    REQUEST_PERMISSIONS_CODE
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar")
            { dialog, id ->
                dialog.dismiss()
            }
        mDialog.show()

    }

    private fun createDeleteFile() {
        val file = File(getExternalFilesDir(null), "DemoFile.txt")
        if (file.exists()) {
            file.delete()
        } else {
            try {
                val os: OutputStream = FileOutputStream(file)
                os.write("Instituto Infnet\n Com Quebra de Linha.".toByteArray())
                os.close()
            } catch (e: IOException) {
                Log.d("Permissão", "Erro de escrita em arquivo")
            }
        }




    }

    fun callReadFromExternalStorage(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                )){
                callDialog( "É preciso a liberar READ_EXTERNAL_STORAGE",
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            } else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_CODE)
            }

            }
        else{
            readFile()
        }

    }

    private fun readFile() {
        val file = File(getExternalFilesDir(null), "DemoFile.txt")
        if(!file.exists()) {
            Toast.makeText(this@MainActivity,
                "Arquivo não encontrado",
                Toast.LENGTH_SHORT).show()
            return
        }
        val text = StringBuilder()
        try{
            val br = BufferedReader(FileReader(file))
            var line : String?
            while(br.
                readLine().also{ line = it} != null){
                text.append(line)
                text.append('\n')
            }
            br.close()
    }
        catch(e:IOException){
            e.printStackTrace()
        }
        Toast.makeText(this@MainActivity,
            text.toString(),
            Toast.LENGTH_SHORT).show()
    }

    fun callAccessLocation(view: View) {
        val permissionAFL = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionACL = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permissionAFL != PackageManager.PERMISSION_GRANTED &&
            permissionACL != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )){
                callDialog("É preciso liberar ACCESS_FINE_LOCATION",arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_CODE)
            }
        }
        else {
            readMyCurrentCoordinates()
        }
    }

    private fun readMyCurrentCoordinates() {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )
        val isNetworkEnabled = locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(this, "Habilite o GPS!", Toast.LENGTH_LONG).show()
            Log.d("Permissao", "Ative os serviços necessários")
        } else {
            if (isGPSEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
                }
            } else if (isNetworkEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
                }
            }
        }
    }

    // Tratamento da Localização - INI
    private val locationListener =
        object : LocationListener {
            override fun onLocationChanged(location: Location) {

                Toast.makeText(applicationContext,
                    "Lat: $location.latitude | Long: $location.longitude",
                    Toast.LENGTH_SHORT).show()
            }
            override fun onStatusChanged(
                provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
}
