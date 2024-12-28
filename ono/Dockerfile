#Tag mobile qui peut accèder aux mise à jour de java 21
FROM eclipse-temurin:21-jre-alpine

#Création du repertoire dédiée
WORKDIR /app

COPY target/backend-*.jar .
#COPY --from=maven /app/target/backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java","-jar","backend-*.jar"]