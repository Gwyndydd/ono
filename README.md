# ONO

## Présentation du projet
Le projet de Ono est de réalisé un site web pour étudier une langue. 
Il y a une partie vocabulaire. Celle-ci consiste à enregistrer un mot dans la langue étudié avec sa definition dans la langue de l'utilisateur.
Le vocabulaire est ensuite affiché sous forme de carte où le vocabulaire peut-être étudier.

La partie grammaire permet quand à elle d'étudier la grammaire de la langue à travers des listes.
Elle est configuré ainsi :
- Une notion : il s'agit de la règle de grammaire étudié
- Concept : une gramaire peut avoir plusieurs concept associés à une notion. Ainsi on peut détailler plus précisèment comment la règle est à utiliser.

Ces listes peuvent être associés ensemble à un programme d'étude.

## Code

### Backend
Pour lancer le backend nommé "ono", lancer en premier la base de données avec la ligne de commande :
```
> Docker compose build
> Docker compose up
```
Attention à bien être situé dans le dossier du backend end.
Pour ensuite lancer le serveur :
Lancer maven

### Frontend
Pour lancer le frontend "front_ono", vérifier que la base de données et le backend soit lancé.
Au sein du dossier du frontend, lancer la commande :
```
>npm run dev
```

## Fonctionnalité 

### Backend
Actuellement au sein du backend, toutes les classes nécessaires à la base de données ont été créé avec leur repository et leur service.
Chacunes de ces classes sont associés à une classe de dto et de converter faisant le lien entre l'entity et le dto.
Pour le controller, uniquement la partie User/Authentification et la partie vocabulaire ont été créé et fonctionne.
La partie grammaire est en cours de création...

Les tests conservent les services liés à la partie vocabulaire.

### Frontend



