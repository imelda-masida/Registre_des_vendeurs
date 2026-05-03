Registre Numérique des Vendeurs (MarketReg)

Une application Android native et moderne conçue pour numériser le recensement des commerçants dans les espaces municipaux. Ce projet démontre la maîtrise du développement déclaratif avec Jetpack Compose et l'intégration d'un backend "Backend-as-a-Service" (Supabase).

Fonctionnalités Clés
-Gestion CRUD complète : Enregistrement, lecture, modification et suppression des vendeurs en temps réel.
- Recherche Instantanée : Filtrage dynamique de la liste via une barre de recherche réactive (Material 3).
- Stockage Cloud: Synchronisation des données avec PostgreSQL et des images avec Supabase Storage.
- Architecture Professionnelle : Séparation stricte des responsabilités via le pattern MVVM.

Stack Technique
- Langage : Kotlin (100%)
- Interface : Jetpack Compose & Material Design 3
- Architecture : MVVM (Model-View-ViewModel) + StateFlow
- Backend : Supabase (Postgrest & Storage)
- Gestion d'images : Coil (Chargement asynchrone)
- Navigation  : Jetpack Navigation Compose

Organisation du Code
- `data/` : Modèles de données (`Vendor.kt`) et Repository pour les appels API.
- `ui/` : Écrans Compose, ViewModels et thèmes (Couleurs/Typographie). Utilisation de :  https://material-foundation.github.io/material-theme-builder/.
- `navigation/` : Configuration des routes entre la liste et le formulaire.

 
Structure des dossiers (Arborescence)
app
└── src
    └── main
        ├── java/com/example/registredesvendeurs/
        │   ├── model/
        │   │   └── Vendor.kt                     // [Fichier 1] Le Modèle (Data Class)
        │   │
        │   ├── SupaBaseClient.kt                 // [Fichier 2] Configuration du Backend
        │   │
        │   ├── MainActivity.kt                   // [Fichier 3] Gestionnaire de Navigation
        │   │
        │   └── ui/theme/vendor/
        │       ├── VendorViewModel.kt            // [Fichier 4] La Logique métier (Cerveau)
        │       ├── VendorListScreen.kt           // [Fichier 5] Écran d'accueil (Liste + Recherche)
        │       ├── AddVendorScreen.kt            // [Fichier 6] Écran d'ajout (Photo + Saisie)
        │       ├── EditVendorScreen.kt           // [Fichier 7] Écran de modification
        │       └── VendorDetailScreen.kt         // [Fichier 7bis] Écran de détails
        │
        └── res/                                  // Ressources (Icônes, Images, thèmes)
