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

 Configuration
1. Clonez le projet.
2. Remplacez `SUPABASE_URL` et `SUPABASE_KEY` dans le fichier `SupabaseInstance.kt` par vos propres identifiants.
3. Configurez une table `vendors` et un bucket `images` sur votre console Supabase.
4. Compilez et lancez sur un appareil Android (API 24+).

Présentation Vidéo
[]
