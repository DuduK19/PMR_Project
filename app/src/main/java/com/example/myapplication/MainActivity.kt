package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.game.Game
import com.example.myapplication.game.Player
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    val game: Game=Game()
    var lastId: Int = 0
    var i:Int = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnSpeak = findViewById<ImageButton>(R.id.btnSpeak)
        val tvText = findViewById<TextView>(R.id.tvText)
        btnSpeak.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
            try {
                startActivityForResult(intent, RESULT_SPEECH)
                tvText.setText("")
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    applicationContext,
                    "Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val tvText = findViewById<TextView>(R.id.tvText)
        val champId = findViewById<TextView>(R.id.idPlayer)
        val champMain = findViewById<TextView>(R.id.mainPlayer)
        val champCheatScore = findViewById<TextView>(R.id.cheatScorePlayer)
        val champNbChoix = findViewById<TextView>(R.id.nbChoixPlayer)
        when (requestCode) {
            RESULT_SPEECH -> if (resultCode == RESULT_OK && data != null) {
                val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                tvText!!.text = text!![0]
                if (game.players.size>0){
                    val player = game.players[0]
                    champId.text="id: ${player.id}"
                    champCheatScore.text="id: ${player.cheatScore}"
                    champNbChoix.text="id: ${player.nbChoix}"
                }
                when(text[0]){
                    "bonjour"-> {Log.i("bonjour:", text[0])
                        game.players.add(Player(id=lastId+1))
                        lastId+=1
                        startRegister()
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
                    else-> {val player:Player = game.players[0]?:Player(id=-1)
                        tvText!!.text = player.toString()}
                }
            }
        }

    }

    companion object {
        protected const val RESULT_SPEECH = 1
    }

    fun startRegister(){
        val tvText = findViewById<TextView>(R.id.tvText)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
        try {
            startActivityForResult(intent, RESULT_SPEECH)
            tvText.setText("")
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                "Your device doesn't support Speech to Text",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    private fun setI(card:String){
        when{
            card=="10"->i+=1
            (card.toInt() in 2..6)->i-=1
        }
    }
}