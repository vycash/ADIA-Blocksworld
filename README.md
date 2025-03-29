# Aide à décision intélligence artificielle BLOCKSWORLD                

## Description 
Ce projet est une implémentation du monde des blocs (BlocksWorld) qui s'agit d'empiler des blocs les uns sur les autres en piles, un environnement classique utilisé en intelligence artificielle pour expérimenter des algorithmes de planification, de satisfaction de contraintes et d’extraction de connaissances. Réalisé dans le cadre du cours "Aide à la Décision et Intelligence Artificielle" à l'Université de Caen Normandie.

Le projet explore et met en œuvre différents aspects fondamentaux de l'intelligence artificielle :
1. **Résolution de contraintes (CSP)** :
   - Implémentation de contraintes régulières (écarts constants dans les piles) et croissantes (un bloc ne peut être placé que sur un bloc de numéro inférieur).
   - Utilisation de solveurs comme MAC (maintien de cohérence d'arc), le backtracking, et HeuristicMAC.

2. **Planification** :
   - Résolution de problèmes d'empilement en utilisant les algorithmes **BFS**, **DFS**, **Dijkstra** et **AStar** sur des graphes où les sommets dont représentés par des **états** (empilement) et les arcs sont représentés par des **actions** (déplacement d'un bloc).
   - Deux heuristiques personnalisées sont utilisées pour optimiser les résultats.

3. **Extraction de connaissances** :
   - Extraction de motifs fréquents avec l'algorithme **Apriori**.
   - Génération de règles d'association pour analyser les données où la Fréquence minimale ≥ 2/3 et la Confiance minimale : ≥ 95%.

4. **Visualisation** :
   - Les configurations et plans générés peuvent être visualisés graphiquement grâce à des bibliothèques tierces.

## l'arborescence du rendu est comme suit:
.
├── information.txt  
├── lib/  
├── MainScript.sh  
├── README.md  
├── run_blocksworld.sh  
├── src  
│   ├── blocksworld  
│   │   ├── csp  
│   │   │   ├── DemoConstraint.java  
│   │   │   ├── DemoCSP.java  
│   │   │   ├── DemoIncreasingConstraint.java  
│   │   │   ├── DemoRegularityAndIncreasingConstraint.java  
│   │   │   └── DemoRegularityConstraint.java  
│   │   ├── datamining  
│   │   │   ├── BooleanVariableGenerator.java  
│   │   │   └── DemoDataMining.java  
│   │   ├── DemoBlocksWorld.java  
│   │   ├── planners  
│   │   │   ├── ActionGenerator.java  
│   │   │   ├── BadBlockHeuristic.java  
│   │   │   ├── BadStackHeuristic.java  
│   │   │   └── PlanVisualizer.java  
│   │   └── world  
│   │       ├── BlockWorld.java  
│   │       ├── BlockWorldWithConstraints.java  
│   │       ├── BlockWorldWithIncreasingConstraint.java  
│   │       ├── BlockWorldWithRegularityAndIncreasingConstraint.java  
│   │       ├── BlockWorldWithRegularityConstraint.java  
│   │       └── StateVisualizer.java  
│   ├── cp/    
│   ├── datamining/  
│   ├── modelling/  
│   └── planning/  
└── test/  

    
## Structure :
- src : contient l'ensemble du code source réppartit en packages, et chaque package contient le
  code source de la partie correpondante au nom du package
- src/blocksworld : contient l'ensemble du code source de l'environement blocksworld ainsi que le code dont il dépend répartis en packages,
- src/blocksworld/world/ : contient toutes les classes de modélisation du monde ainsi qu'une classe de visualisation des états
- src/blocksworld/planners/ : contient la classe qui permet de générer toute les actions possibles dans un monde conné, ainsi que deux heuristiques et une classe qui permet la visualisation de l'execution d'un plan trouvé grace à un planner.
- src/blocksworld/csp / : contient les classes permettant de démontrer la recherche des états satisfaisant des contraintes pour un monde
- src/blocksworld/datamining/ : contient les classes de démonstration d'extraction de règles d'association et la classe de generation et creation de bases de donnéés
- test : contient l'ensembles des classes éxécutant les tests unitaires des librairies de test
- MainScript.sh : est un script shell permettant de compiler et executer les classes de démonstration et les classes de test pour faciliter la compilation et execution
- run_blocksworld.sh : est un script shell pour compiler et executer les classes de Démo du package blocksworld et ses sous packages correspondants
- README.md : est un fichier contenant toutes les informations utiles à propos du rendu
- lib/ : répértoire contenant toutes les librairies utiles et utilisées par l'ensenmble des classes


### pour executer les classes de tests et démo des packages standard executez le script disponible à la racine 
``` bash
./MainScript.sh
```
 
### pour executer les Démo du package blocksworld le script disponible à la racine 
``` bash
./run_blocksworld.sh
```
 
### pour executer GUIDemo, placez vous dans lib/ et executez : ** java -cp blocksworld.jar bwdemo.GUIDemo **
``` bash
./java -cp blocksworld.jar bwdemo.GUIDemo
```


