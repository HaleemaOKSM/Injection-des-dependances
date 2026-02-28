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

#  1 : Création de l'interface IDao avec une méthode getData
# TP : Injection des Dépendances — Couplage Faible

## Objectif

Comprendre le principe du couplage faible et l'injection des dépendances en Java :

- Instanciation statique
- Instanciation dynamique
- Utilisation du framework Spring (XML et annotations)

---

## Rôle général des packages

### `dao` — Couche d'accès aux données (Data Access Object)

Elle est responsable de la récupération des données depuis une source (base de données, fichier, service externe, ou valeur simulée dans ce TP). Cette couche isole la logique d'accès aux données du reste de l'application.

### `metier` — Couche métier (Business Layer)

Elle contient la logique métier de l'application, c'est-à-dire les traitements et calculs réalisés à partir des données récupérées par la couche DAO. Elle dépend d'une abstraction (interface) pour assurer un couplage faible.

### `presentation` — Couche présentation

Elle permet de tester et d'exécuter l'application. C'est dans cette couche que l'on effectue l'injection des dépendances (statique, dynamique ou via Spring).

---

## Étape 1 : Création de l'interface `IDao`

L'interface `IDao` définit un **contrat** que toute implémentation de la couche DAO doit respecter.

```java
public interface IDao {
    double getData();
}
```

### Pourquoi une interface ?

Une interface ne contient **aucune logique**, elle se contente de déclarer *ce que la classe doit faire*, sans dire *comment* elle le fait. Cela permet de :

- Garantir que toute implémentation fournit bien une méthode `getData()`
- Assurer un **couplage faible** entre les couches
- Faciliter le remplacement d'une source de données sans toucher au reste de l'application



## Étape 2 : Implémentation de l'interface — `DaoImpl`

`DaoImpl` est la classe concrète qui fournit la **logique réelle** d'accès aux données.

```java
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        System.out.println("Version Base de données");
        double temp = 23;
        return temp;
    }
}
```

### Pourquoi ce n'est pas la même chose que l'interface ?

| | `IDao` (interface) | `DaoImpl` (implémentation) |
|---|---|---|
| **Rôle** | Déclare *quoi faire* | Définit *comment le faire* |
| **Contenu** | Signature de méthode uniquement | Logique complète de récupération des données |
| **Instanciable ?** |  Non |  Oui |
| **Modifiable sans impact ?** |  Rarement modifiée |  Peut être remplacée librement |

> En d'autres termes : l'interface est le **plan**, l'implémentation est la **construction**. On peut reconstruire différemment (base de données, fichier, API…) sans jamais changer le plan.


## Principe clé : programmer vers l'abstraction

```java
// ✅ Bonne pratique — couplage faible
IDao dao = new DaoImpl();

// ❌ Mauvaise pratique — couplage fort
DaoImpl dao = new DaoImpl();
```

En déclarant la variable avec le type `IDao`, on peut **substituer** `DaoImpl` par n'importe quelle autre implémentation sans modifier le reste du code.
