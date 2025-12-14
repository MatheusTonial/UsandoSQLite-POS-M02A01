package com.tonial.usandosqlite.entity

import com.google.firebase.firestore.DocumentId

data class Cadastros(
    @DocumentId
    val _id: Int,
    val nome: String,
    val telefone: String
)



