package com.example.projetui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode


class MainActivity : AppCompatActivity() {

    lateinit var sceneView : ArSceneView
    lateinit var buttonPlace : ExtendedFloatingActionButton
    lateinit var playerText : TextView
    lateinit var cheatScoreText : EditText
   // lateinit var modelNode : ArModelNode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.sceneView)
        sceneView.planeRenderer.isVisible = false

        playerText = findViewById(R.id.Player)
        cheatScoreText = findViewById(R.id.cheatScoreText)
        buttonPlace = findViewById(R.id.place)
        playerText.text = 0.toString()

        //sceneView.onCallCheatScore(sceneView) //or sceneView.onTap()
        /*modelNode = ArModelNode().apply {
            loadModelGlbAsync(
                glbFileLocation = "models/sofa_single.glb"
            )

            { sceneView.planeRenderer.isVisible = true }

            onAnchorChanged = {
                buttonPlace.isGone
            }
        }*/

        buttonPlace.setOnClickListener{
            var dozenNode = ArModelNode().apply {

                loadModelGlbAsync(
                    glbFileLocation = "models/3d_number_0.glb",
                    centerOrigin = Float3(-1.0f, 1.0f, 0.0f)
                )
            }

            var unitNode = ArModelNode().apply {
                loadModelGlbAsync(
                    glbFileLocation = "models/3d_number_0.glb",
                    centerOrigin = Float3(1.0f, 1.0f, 0.0f)
                )
            }

            sceneView.addChild(dozenNode)
            sceneView.addChild(unitNode)

        }
    }
/*
    private fun placeModel() {
        modelNode?.anchor()
        sceneView.planeRenderer.isVisible = false
    }
}

    private fun ArSceneView.onCallCheatScore(sceneView: ArSceneView) {

        sceneView.clearAnimation()

        val scan = getScanPlayerQRCode()  //différent de getScanCardQRCode()
        val localisation = scan.localisation //type float3
        val cheatScore = player(scan.name).cheatScore

        cheatScoreText.text = cheatScore.toString()

        var dozenNode = ArModelNode().apply {
            loadModelGlbAsync(
                glbFileLocation = "models/3d_number_${cheatScore/10}.glb",
                centerOrigin = localisation + Float3(-1.0f,1.0f,0.0f)
            )
            }
        var unitNode = ArModelNode().apply {
            loadModelGlbAsync(
                glbFileLocation = "models/3d_number_${cheatScore%10}.glb",
                centerOrigin = localisation + Float3(1.0f,1.0f,0.0f)
            )
        }

        sceneView.addChild(dozenNode)
        sceneView.addChild(unitNode)


    }*/
}
