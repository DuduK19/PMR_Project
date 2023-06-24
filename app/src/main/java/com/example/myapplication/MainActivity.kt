package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.game.Game
import com.example.myapplication.game.Player
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    val game: Game=Game()
    var lastId: Int = 0
    var i:Int = 10
    private var speechRecognizer:SpeechRecognizer?=null
    private var editText: EditText?= null
    private var btn : Button? = null
    private var voiceCommand : TextView? = null
    private var keepListening : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!=
            PackageManager.PERMISSION_GRANTED){
            checkPermissions()
        }
        editText = findViewById<EditText>(R.id.text)
        btn = findViewById<Button>(R.id.btnRegister)
        voiceCommand = findViewById<TextView>(R.id.command)
        speechRecognizer =SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")

        speechRecognizer!!.setRecognitionListener(object :RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {
                //Toast.makeText(applicationContext, "ready for speech", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {

                editText!!.setText("")
                editText!!.setHint("listening")
            }


            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
                Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
                if (keepListening) speechRecognizer!!.startListening(speechRecognizerIntent)
            }

            override fun onResults(results: Bundle?) {

                if(results!=null) {
                    val data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (data != null && data.isNotEmpty()) {
                        val commande = data!![0]
                        editText!!.setText(commande)
                        voiceCommand!!.text = commande
                        handleCommand(commande)
                        Log.i("data", commande)
                    }
                }
                if (keepListening) speechRecognizer!!.startListening(speechRecognizerIntent)
                else {Log.i("message","fini")
                    speechRecognizer!!.stopListening()}
            }


            override fun onPartialResults(partialResults: Bundle?) {}


            override fun onEvent(eventType: Int, params: Bundle?) {}

        })

        btn!!.setOnClickListener { speechRecognizer!!.startListening(speechRecognizerIntent) }

    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()
    }


    private fun checkPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(
                this,arrayOf(Manifest.permission.RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecordAudioRequestCode && grantResults.isNotEmpty()){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show()
        }
    }

    companion object{
        const val RecordAudioRequestCode = 1
    }

    private fun setI(card:String){
        when{
            card=="10"->i+=1
            (card.toInt() in 2..6)->i-=1
        }
    }

    private fun handleCommand(command: String) {
        Log.i("commande:", command)
        when(command){
            "bonjour"-> {
                game.players.add(Player(id=lastId+1))
                lastId+=1
                Log.i("player:", game.players[0].toString())
            }
            "distribution"-> {
                val player:Player = game.players[0]?:Player(id=-1)
                if (player.id>-1){
                    var card = "10" //on recupere la carte en scannant
                    player.addCard(card)
                    setI(card)
                    card = "7" //on recupere la carte en scannant
                    player.addCard(card)
                    setI(card)
                }}
            "je prends"->{ val player:Player = game.players[0]?:Player(id=-1)
                if (player.id>-1) player.setCheatScore("prendre", i)
            }
            "je passe"-> { val player:Player = game.players[0]?:Player(id=-1)
                if (player.id>-1) player.setCheatScore("passer", i)}
            "fin de la partie"-> keepListening = false
            else-> {Log.i("player",game.players[0].toString())}
        }

    }

}