package com.example.quizapp2.model

data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswers: List<Int>
)
