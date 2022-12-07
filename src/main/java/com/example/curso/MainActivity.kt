package com.example.curso

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var img: ImageView

    lateinit var currentPhotoPath:String

    val REQUEST_IMAGE_CAPTURE=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        img=findViewById(R.id.img)
    }

    //criar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    //trata os eventos de clique dos botoes
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sair){
            finish()
        }else if (id==R.id.action_photo){
            takePhoto()
        }else if(id==R.id.action_salvar){
            taskSave()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createImageFile():File{
        val timestamp:String=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File=getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("${timestamp}_",".jpg",storageDir).apply {
            currentPhotoPath=absolutePath
        }

    }

    private fun takePhoto(){
        var photoFile:File=createImageFile()

        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                it
            )
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
            var imgFile:File= File(currentPhotoPath)
            img.setImageURI(Uri.fromFile(imgFile))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun taskSave(){
        var imgFile:File = File(currentPhotoPath)
        if (imgFile != null && imgFile.exists()){
            PhotoService.postForm(imgFile)
            Toast.makeText(this,"image enviada com sucesso",Toast.LENGTH_LONG).show()
            img.setImageResource(0 )
        }
    }

}