package com.example.registredesvendeurs.model // Définit l'endroit où se trouve le fichier

import kotlinx.serialization.Serializable // Nécessaire pour convertir l'objet en JSON pour Supabase

@Serializable // Permet à Supabase de lire et d'écrire cet objet automatiquement
data class Vendor( // Définit une classe de données pour le vendeur
    val id: Int? = null, // Identifiant unique (null par défaut car généré par la BDD)
    val name: String, // Nom du vendeur ou du commerce
    val tableNumber: String, // Numéro de l'emplacement ou de la table
    val category: String, // Catégorie de produits vendus
    val imageUrl: String? = null // Lien vers la photo stockée sur Supabase Storage
)