# TP : Injection des dépendances - Couplage faible

##  Objectif
Comprendre le principe du couplage faible et l'injection des dépendances en Java :
- Instanciation statique
- Instanciation dynamique
- Utilisation du framework Spring (XML et annotations)

## Rôle général des packages

- **dao**  
  Couche d’accès aux données (Data Access Object).  
  Elle est responsable de la récupération des données depuis une source (base de données, fichier, service externe, ou valeur simulée dans ce TP).  
  Cette couche isole la logique d’accès aux données du reste de l’application.

- **metier**  
  Couche métier (Business Layer).  
  Elle contient la logique métier de l’application, c’est-à-dire les traitements et calculs réalisés à partir des données récupérées par la couche DAO.  
  Elle dépend d’une abstraction (interface) pour assurer un couplage faible.

- **presentation**  
  Couche présentation.  
  Elle permet de tester et d’exécuter l’application.  
  C’est dans cette couche que l’on effectue l’injection des dépendances (statique, dynamique ou via Spring).

#  Partie 1 : Création des interfaces

