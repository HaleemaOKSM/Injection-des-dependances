
# TP : Injection des Dépendances — Couplage Faible

## Objectif

Comprendre le principe du couplage faible et l'injection des dépendances en Java :

- Instanciation statique
- Instanciation dynamique
- Utilisation du framework Spring (XML et annotations)


## Rôle général des packages

### `dao` — Couche d'accès aux données (Data Access Object)

Elle est responsable de la récupération des données depuis une source (base de données, fichier, service externe, ou valeur simulée dans ce TP). Cette couche isole la logique d'accès aux données du reste de l'application.

### `metier` — Couche métier (Business Layer)

Elle contient la logique métier de l'application, c'est-à-dire les traitements et calculs réalisés à partir des données récupérées par la couche DAO. Elle dépend d'une abstraction (interface) pour assurer un couplage faible.

### `presentation` — Couche présentation

Elle permet de tester et d'exécuter l'application. C'est dans cette couche que l'on effectue l'injection des dépendances (statique, dynamique ou via Spring).



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



## Étape 3 : Création de l'interface `IMetier`

L'interface `IMetier` définit le contrat de la **couche métier**. Elle expose une méthode `calcul()` qui représente le traitement métier à effectuer.

```java
public interface IMetier {
    double calcul();
}
```

### Pourquoi une interface ici aussi ?

De la même façon que pour la couche DAO, utiliser une interface pour la couche métier permet de :

- Découpler la couche présentation de la logique métier
- Faciliter les tests (on peut injecter un faux objet métier)
- Respecter le principe de **programmation vers l'abstraction**



## Étape 4 : Implémentation de `IMetier` avec couplage faible — `MetierImpl`

`MetierImpl` implémente la logique métier en s'appuyant sur la couche DAO via l'interface `IDao`, et non sur une implémentation concrète. C'est ce qui garantit le **couplage faible**.

```java
package net.ouaksim.metier;

import net.ouaksim.dao.IDao;

public class MetierImpl implements IMetier {
    private IDao dao;

    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double data = dao.getData();
        return data * 43 / 3;
    }
}
```

### Comment le couplage faible est-il appliqué ici ?

| Élément | Détail |
|---|---|
| **Type de `dao`** | `IDao` (interface) et non `DaoImpl` (classe concrète) |
| **Injection** | La dépendance est passée via le **constructeur**, pas instanciée en interne |
| **Conséquence** | On peut changer la source de données sans modifier `MetierImpl` |

### Injection par constructeur

```java
// MetierImpl ne sait pas QUI lui fournit les données
// Il sait juste que l'objet respecte le contrat IDao
public MetierImpl(IDao dao) {
    this.dao = dao;
}
```

> `MetierImpl` ne fait jamais `new DaoImpl()` en interne — c'est l'appelant qui décide quelle implémentation de `IDao` injecter. C'est le principe fondamental de l'**injection de dépendances**.


## Étape 5 : Injection des dépendances

L'injection des dépendances consiste à **fournir de l'extérieur** les objets dont une classe a besoin, plutôt que de les instancier elle-même. On présente ici quatre approches.


### 5a. Instanciation statique

L'injection est réalisée **manuellement dans le code** : le développeur crée les objets avec `new` et les passe explicitement au constructeur. Tout est connu et câblé à la **compilation**.

C'est l'approche la plus simple, mais elle introduit un couplage fort au niveau de la couche présentation, car les classes concrètes (`DaoImpl`, `MetierImpl`) y sont directement référencées. Tout changement d'implémentation impose une **recompilation**.

```java

public class StaticPresentation {
    public static void main(String[] args) {
//        DaoImpl dao = new DaoImpl();       // ← ancienne implémentation
        DaoImplV2 dao = new DaoImplV2();    // ← nouvelle implémentation (nécessite recompilation)
        IMetier metier = new MetierImpl(dao); // Injection statique via le constructeur
        System.out.println("Résultat calcul (statique) : " + metier.calcul());
    }
}
```

> Le commentaire sur `DaoImpl` illustre bien le problème : pour changer de source de données, il faut **modifier le code source** et recompiler. C'est ce que les approches suivantes cherchent à éviter.


### 5b. Instanciation dynamique

L'injection est réalisée **à l'exécution** grâce à la **réflexion Java** (`Class.forName`). Le nom complet des classes à instancier est lu depuis un fichier de configuration externe (ex. `config.txt`). Le code de présentation ne manipule que des **interfaces**, il ne connaît jamais les classes concrètes.

Changer d'implémentation se fait simplement en modifiant le fichier de configuration, **sans recompiler**. C'est une approche pédagogique qui illustre bien le principe du couplage faible, mais elle reste manuelle comparée à Spring.

**Fichier `config.txt` :**

```
net.ouaksim.dao.DaoImpl
```

```java

public class DynamicPresentation {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Scanner scan = new Scanner(new File("config.txt"));

        String daoClassName = scan.nextLine();       // Lecture du nom de classe depuis config.txt
        Class cDao = Class.forName(daoClassName);    // Chargement dynamique de la classe
        IDao dao = (IDao) cDao.newInstance();        // Instanciation via la réflexion Java
        System.out.println(dao.getData());
    }
}
```

> Pour changer d'implémentation (ex. passer de `DaoImpl` à `DaoImplV2`), il suffit de modifier **une seule ligne dans `config.txt`** — aucune modification du code Java, aucune recompilation.

### 5c. Injection avec Spring — Version XML

Spring est un **framework d'injection de dépendances** qui automatise la création et l'assemblage des objets (appelés **beans**). Dans la version XML, la configuration est externalisée dans un fichier `config.xml` où l'on déclare les beans et leurs dépendances.

Spring lit ce fichier au démarrage, instancie les objets dans le bon ordre et injecte automatiquement les dépendances. Le code Java ne contient aucune référence aux classes concrètes — Spring joue le rôle d'**assembleur**.


```java
public class PresSpringXml {
    public static void main(String[] args) {
        // Spring lit config.xml, crée et assemble les beans automatiquement
        ApplicationContext springContext = new ClassPathXmlApplicationContext("config.xml");

        IMetier metier = (IMetier) springContext.getBean("metier");
        System.out.println(metier.calcul());
    }
}
```

> `springContext.getBean("metier")` retourne le bean assemblé par Spring — `MetierImpl` reçoit déjà son `IDao` injecté, sans que la couche présentation s'en préoccupe.


### 5d. Injection avec Spring — Version annotations

Au lieu du fichier XML, on utilise des **annotations** directement dans le code Java pour déclarer les beans et injecter les dépendances. Les principales annotations utilisées sont `@Repository` (couche DAO), `@Service` (couche métier) et `@Autowired` (injection automatique). Spring scanne les packages grâce à `@ComponentScan` et construit le contexte applicatif sans configuration externe.

C'est l'approche la plus **moderne et recommandée** : elle est plus concise, plus lisible et élimine complètement le besoin de fichier XML.


## Comparaison des quatre méthodes

| Méthode | Recompilation nécessaire ? | Fichier de config externe ? | Complexité | Usage typique |
|---|---|---|---|---|
| **Statique** |  Oui |  Non | Faible | Prototypage, petits projets |
| **Dynamique** |  Non |  Oui (`.txt`) | Moyenne | Compréhension de la réflexion Java |
| **Spring XML** |  Non |  Oui (`.xml`) | Moyenne | Projets Spring legacy |
| **Spring Annotations** |  Non |  Non | Faible | Projets Spring modernes |

> **Recommandation** : Dans un projet Spring moderne, on privilégie toujours la version **annotations**, plus concise et plus maintenable que la version XML.