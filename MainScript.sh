#!/bin/bash

run_modelling_demo() {
    echo "Compilation du Demo du package modelling..."
    javac -d build/ src/modelling/*.java 
    if [ $? -eq 0 ]; then
        echo "Compilation réussie !"
        echo "Exécution de planningTest..."
        java -cp build/ modelling.DemoModelling
    else
        echo "Échec de la compilation de DemoModelling.java"
    fi
    echo ""
}

run_planning_demo() {
    echo "Compilation du Demo du package planning..."
    javac -d build/ src/planning/*.java src/modelling/*.java 
    if [ $? -eq 0 ]; then
        echo "Compilation réussie !"
        echo "Exécution de planningTest..."
        java -cp build/ planning.DemoPlanning
    else
        echo "Échec de la compilation de DemoPlanning.java"
    fi
    echo ""
}

run_cp_demo() {
    echo "Compilation du Demo du package cp..."
    javac -d build/ src/planning/*.java src/modelling/*.java src/cp/*.java
    if [ $? -eq 0 ]; then
        echo "Compilation réussie !"
        echo "Exécution de DemoCp..."
        java -cp build/ cp.DemoCp
    else
        echo "Échec de la compilation de DemoCp.java"
    fi
    echo ""
}


run_datamining_demo() {
    echo "Compilation du Demo du package datamining.."
    javac -d build/ src/planning/*.java src/modelling/*.java src/cp/*.java src/datamining/*.java
    if [ $? -eq 0 ]; then
        echo "Compilation réussie !"
        echo "Exécution de DemoDatamining..."
        java -cp build/ datamining.DemoDatamining
    else
        echo "Échec de la compilation de DemoDatamining.java"
    fi
    echo ""
}

run_dataminingRandom_demo() {
    echo "Compilation du Demo du package datamining.."
    javac -d build/ src/planning/*.java src/modelling/*.java src/cp/*.java src/datamining/*.java
    if [ $? -eq 0 ]; then
        echo "Compilation réussie !"
        echo "Exécution de DemoDataminingRandom..."
        java -cp build/ datamining.DemoDataminingRandom
    else
        echo "Échec de la compilation de DemoDataminingRandom.java"
    fi
    echo ""
}

# Boucle jusqu'à ce que l'utilisateur fasse un choix valide
while true; do
    # Affichage du menu
    echo "Sélectionnez une option :"
    echo "1) Compiler et exécuter DemoModelling"
    echo "2) Compiler et exécuter DemoPlanning"
    echo "3) Compiler et exécuter DemoCp"
    echo "4) Compiler et exécuter DataminingDemo"
    echo "5) Compiler et exécuter DataminingDemoRandom"
    echo "q) Quitter"
    read -p "Votre choix : " choix

    # Exécution selon le choix de l'utilisateur
    case $choix in
        1)
            run_modelling_demo
            ;;
        2)
            run_planning_demo
            ;;
        3)
            run_cp_demo
            ;;
        4)
            run_datamining_demo
            ;;
        5)
            run_dataminingRandom_demo
            ;;
        
        q|Q)
            echo "Sortie du script."
            exit 0
            ;;
        *)
            echo "Choix invalide. Veuillez sélectionner 1, 2, 3, 4, 5 ou 'q' pour quitter."
            ;;
    esac
done
