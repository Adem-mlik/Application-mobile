package com.example.conversation

import androidx.annotation.DrawableRes

data class Message(
    val author: String,
    val text: String,
    @DrawableRes val imageRes: Int
)

object SampleData1 {
    val conversationSample = listOf(
        Message("Ali", "I am strong", R.drawable.img),
        Message("Sami", "I believe in myself", R.drawable.img),
        Message("Amira", "Each day is a new opportunity to grow", R.drawable.img),
        Message("Youssef", "Every challenge in my life is an opportunity to learn", R.drawable.img),
        Message("Nour", "I have so much to be grateful for", R.drawable.img),
        Message("Karim", "Good things are always coming into my life", R.drawable.img),
        Message("Salma", "New opportunities await me at every turn", R.drawable.img),
        Message("Hedi", "I have the courage to follow my heart", R.drawable.img),
        Message("Ines", "Things will unfold at the right time", R.drawable.img),
        Message("Omar", "I will be present in all moments today", R.drawable.img)
    )
}