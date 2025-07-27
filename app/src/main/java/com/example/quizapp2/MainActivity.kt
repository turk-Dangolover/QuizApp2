package com.example.quizapp2

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp2.model.Question
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // --- JSON einlesen und in Question‑Objekte umwandeln ---
        val inputStream = assets.open("questions.json")
        val jsonStr = inputStream.bufferedReader().use { it.readText() }
        val jsonArr = JSONArray(jsonStr)

        val allQuestions = mutableListOf<Question>()
        for (i in 0 until jsonArr.length()) {
            val obj = jsonArr.getJSONObject(i)
            val text = obj.getString("text")

            // Antworten‑Array → List<String>
            val answersJson = obj.getJSONArray("answers")
            val answersList = List(answersJson.length()) { j ->
                answersJson.getString(j)
            }

            // correct‑Array → List<Int>
            val correctJson = obj.getJSONArray("correct")
            val correctList = List(correctJson.length()) { j ->
                correctJson.getInt(j)
            }

            allQuestions += Question(text, answersList, correctList)
        }

        // zufällig mischen und die ersten 10 rausnehmen
        val quizQuestions: List<Question> = allQuestions.shuffled().take(10)

        // zusätzlich Index und Score anlegen, damit du später navigieren kannst
        var currentIndex = 0
        var score = 0

        // --- ab hier baust du dein UI auf und zeigst quizQuestions[currentIndex] an ---
        setContentView(R.layout.activity_main)
        val textQuestion = findViewById<TextView>(R.id.textQuestion)
        val checkOption1 = findViewById<CheckBox>(R.id.checkOption1)
        val checkOption2 = findViewById<CheckBox>(R.id.checkOption2)
        val checkOption3 = findViewById<CheckBox>(R.id.checkOption3)
        val checkOption4 = findViewById<CheckBox>(R.id.checkOption4)
        val buttonNext = findViewById<Button>(R.id.buttonNext)

        textQuestion.text = quizQuestions[0].text
        checkOption1.text = quizQuestions[0].answers[0]
        checkOption2.text = quizQuestions[0].answers[1]
        checkOption3.text = quizQuestions[0].answers[2]
        checkOption4.text = quizQuestions[0].answers[3]
        // sicherstellen, dass keine Checkbox angehakt ist:
        checkOption1.isChecked = false
        checkOption2.isChecked = false
        checkOption3.isChecked = false
        checkOption4.isChecked = false

        buttonNext.setOnClickListener {
        // 1. Ausgewählte Antworten ermitteln
            val selectedAnswers = mutableListOf<Int>()
            if(checkOption1.isChecked) selectedAnswers.add(0)
            if(checkOption2.isChecked) selectedAnswers.add(1)
            if(checkOption3.isChecked) selectedAnswers.add(2)
            if(checkOption4.isChecked) selectedAnswers.add(3)
            // 2. Vergleich mit korrekter Antwortenliste
            val correctList = quizQuestions[currentIndex].correctAnswers.sorted()
            if(selectedAnswers.sorted() == correctList) {
                score++
            }
            // 3. Nächste Frage oder Ende
            currentIndex++
            if(currentIndex < quizQuestions.size) {
            // nächste Frage anzeigen:
                textQuestion.text = quizQuestions[currentIndex].text
                val ans = quizQuestions[currentIndex].answers
                checkOption1.text = ans[0]; checkOption1.isChecked = false
                checkOption2.text = ans[1]; checkOption2.isChecked = false
                checkOption3.text = ans[2]; checkOption3.isChecked = false
                checkOption4.text = ans[3]; checkOption4.isChecked = false
            } else {
            // Quiz zu Ende: Ergebnis anzeigen
                Toast.makeText(this, "Ergebnis: $score von ${quizQuestions.size} richtig", Toast.LENGTH_LONG).show()
            // (Optional: reset oder Activity beenden)
            }
        }

    }
}