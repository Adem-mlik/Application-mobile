package com.example.tp4

data class Product(
    val title: String,
    val description: String,
    val imageUrl: String
)

val products = listOf(
    Product(
        title = "Phone",
        description = "High quality smartphone",
        imageUrl = "https://www.google.com/imgres?q=phone&imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F8%2F8d%2FMobile_Phone_Evolution_1992_-_2014.jpg&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FMobile_phone&docid=19H_OSbKGXwWXM&tbnid=zKvq0cNFBuzi0M&vet=12ahUKEwiRzdWMuNGTAxU2TkEAHSg5GDwQnPAOegQIExAB..i&w=3977&h=4323&hcb=2&ved=2ahUKEwiRzdWMuNGTAxU2TkEAHSg5GDwQnPAOegQIExAB"
    ),
    Product(
        title = "Laptop",
        description = "Powerful laptop",
        imageUrl = "https://www.cnet.com/tech/computing/best-laptop/"
    ),
    Product(
        title = "Headphones",
        description = "Noise cancelling",
        imageUrl = "https://www.google.com/imgres?q=Headphones&imgurl=https%3A%2F%2Fwww.leafstudios.in%2Fcdn%2Fshop%2Ffiles%2F1_a43c5e0b-3a47-497d-acec-b4764259b10e_800x.png%3Fv%3D1750486829&imgrefurl=https%3A%2F%2Fwww.leafstudios.in%2Fproducts%2Fleaf-bass-pro-bluetooth-headphones%3Fsrsltid%3DAfmBOoqSC9KJgRnjzU1tGhF9baaoEWyVUR5dNpFYdGWstBplt-Ja8DFa&docid=k-bNXYabEFPJcM&tbnid=ko7xUbOETUycFM&vet=12ahUKEwiDhOTpuNGTAxVaV6QEHZulOegQnPAOegQIGhAB..i&w=800&h=800&hcb=2&ved=2ahUKEwiDhOTpuNGTAxVaV6QEHZulOegQnPAOegQIGhAB"
    )
)