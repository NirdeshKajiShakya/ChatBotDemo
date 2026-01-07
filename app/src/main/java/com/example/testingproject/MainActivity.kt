package com.example.testingproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION = 101
    private val projectId = "YOUR_PROJECT_ID"
    private lateinit var sessionClient: SessionsClient
    private lateinit var session: SessionName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDialogflow()

        val input = findViewById<EditText>(R.id.userInput)
        val sendBtn = findViewById<Button>(R.id.sendBtn)
        val chatBox = findViewById<TextView>(R.id.chatBox)

        sendBtn.setOnClickListener {
            val message = input.text.toString()

            if (message.contains("location", true)) {
                requestLocation(message, chatBox)
            } else {
                sendToDialogflow(message, chatBox)
            }
        }
    }

    private fun setupDialogflow() {
        val stream = resources.openRawResource(R.raw.credential) // dialogflow service key
        val credentials = GoogleCredentials.fromStream(stream)
        val settings = SessionsSettings.newBuilder()
            .setCredentialsProvider { credentials }
            .build()

        sessionClient = SessionsClient.create(settings)
        session = SessionName.of(projectId, "12345")
    }

    private fun requestLocation(userMessage: String, chatBox: TextView) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                val msg = "$userMessage | lat:$lat | lng:$lng"
                sendToDialogflow(msg, chatBox)
            } else {
                chatBox.text = "Unable to access location."
            }
        }
    }

    private fun sendToDialogflow(text: String, chatBox: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            val query = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(text).setLanguageCode("en"))
                .build()

            val response = sessionClient.detectIntent(session, DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(query)
                .build()
            )

            val botReply = response.queryResult.fulfillmentText

            withContext(Dispatchers.Main) {
                chatBox.text = "Bot: $botReply"
            }
        }
    }
}