#!/bin/bash

# Variables
CLASSPATH="lib/blocksworld.jar:lib/bwgenerator.jar"
SOURCE_PATH="src"
BUILD_PATH="build"
MAIN_PACKAGE="blocksworld"
DEMO_CLASSES=(
    "DemoBlocksWorld"
    "csp.DemoConstraint"
    "csp.DemoIncreasingConstraint"
    "csp.DemoRegularityConstraint"
    "csp.DemoRegularityAndIncreasingConstraint"
    "datamining.DemoDataMining"
)

# Fonction pour afficher les options
show_menu() {
    echo "========================"
    echo " Choisissez une démonstration à exécuter:"
    echo "========================"
    for i in "${!DEMO_CLASSES[@]}"; do
        echo "$((i + 1)). ${DEMO_CLASSES[$i]}"
    done
    echo "q. Quitter"
    echo "c. recompiler"
}

# Compilation
compile() {
echo "Compilation des fichiers..."
javac -cp "$CLASSPATH" -d "$BUILD_PATH" \
    "$SOURCE_PATH/blocksworld/world/"*.java \
    "$SOURCE_PATH/blocksworld/csp/"*.java \
    "$SOURCE_PATH/blocksworld/planners/"*.java \
    "$SOURCE_PATH/blocksworld/datamining/"*.java \
    "$SOURCE_PATH/blocksworld/"*.java \
    "$SOURCE_PATH/modelling/"*.java \
    "$SOURCE_PATH/cp/"*.java \
    "$SOURCE_PATH/modelling/"*.java \
    "$SOURCE_PATH/planning/"*.java \
    "$SOURCE_PATH/datamining/"*.java 
}

compile
if [ $? -ne 0 ]; then
    echo "Erreur lors de la compilation. Vérifiez le code source."
    exit 1
fi
echo "Compilation réussie!"

# Menu interactif
while true; do
    show_menu
    read -p "Entrez le numéro de votre choix (ou 'q' pour quitter) :" CHOICE

    if [[ "$CHOICE" == "q" || "$CHOICE" == "Q" ]]; then
        echo "Sortie du programme."
        exit 0
    elif [[ "$CHOICE" == "c" || "$CHOICE" == "C" ]]; then
        compile
    elif [ "$CHOICE" -ge 1 ] && [ "$CHOICE" -le "${#DEMO_CLASSES[@]}" ]; then
        SELECTED_CLASS=${DEMO_CLASSES[$((CHOICE - 1))]}
        echo "Exécution de $SELECTED_CLASS..."
        java -cp "$CLASSPATH:$BUILD_PATH/" "$MAIN_PACKAGE.$SELECTED_CLASS"
    else
        echo "Choix invalide. Veuillez réessayer."
    fi
done
