package com.example.myapplication

import androidx.annotation.DrawableRes

data class Message(
    val text: String,
    @DrawableRes val imageRes: Int
)

object SampleData1 {
    val conversationSample = listOf(
        Message(
            text = "I am strong",
            imageRes = R.drawable.image1
        ),
        Message(
            text = "I believe in myself",
            imageRes = R.drawable.image2
        ),
        Message(
            text = "Each day is a a new opportunity to grow and be a better version of myself",
            imageRes = R.drawable.image3
        ),
        Message(
            text = "Every challenge in my life is an opportunity to learn from",
            imageRes = R.drawable.image4
        ),
        Message(
            text = "I have so much to be grateful for",
            imageRes = R.drawable.image5
        ),
        Message(
            text = "Good things are always coming into my life",
            imageRes = R.drawable.image6
        ),
        Message(
            text = "New opportunities await me at every turn",
            imageRes = R.drawable.image7
        ),
        Message(
            text = "I have the courage to follow my heart",
            imageRes = R.drawable.image8
        ),
        Message(
            text = "Things will unfold at precisely the right time",
            imageRes = R.drawable.image9
        ),
        Message(
            text = "I will be present in all the moments that this day brings",
            imageRes = R.drawable.image10
        )
    )
}