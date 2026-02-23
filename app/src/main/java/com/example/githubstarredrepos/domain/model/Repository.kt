package com.example.githubstarredrepos.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val description: String,
    val stars: Int,
    val ownerUsername: String,
    val ownerAvatarUrl: String,
    val language: String?, 
    val forks: Int   
)
