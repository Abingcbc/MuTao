package com.example.cbc.the_hack

data class Poem(
        val id: Long = counter++,
        val title: String,
        val dynasty: String,
        val author: String,
        val body: String
) {
    companion object {
        private var counter = 0L
    }
}
