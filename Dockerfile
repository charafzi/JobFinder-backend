# Utiliser l'image officielle d'OpenJDK
FROM openjdk:17-jdk-slim

# Définir un répertoire de travail dans le conteneur
WORKDIR /app

# Argument pour le fichier JAR généré
ARG JAR_FILE=target/jobfinder-backend-0.0.1-SNAPSHOT.jar

# Copier le fichier JAR dans le conteneur
COPY ${JAR_FILE} app.jar

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
