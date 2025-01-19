# Ing_Soft  
Progetto per Ingegneria del Software 2023-24

Il progetto prevede la realizzazione di un sistema **FAQ**.  

## Struttura del Progetto  
Il progetto è suddiviso in due parti principali:  
1. **Backend**: sviluppato con Spring Boot.  
2. **Frontend**: sviluppato con React.  

---

## Setup  
### !--- Backend ---!  
1. **Preparazione del Database**:  
   - Caricare il dump del database in **Postgres**.  

2. **Configurazione del File di Proprietà**:  
   - Modificare il file `resources/application.properties` con i valori corretti per il collegamento al database Postgres (ad esempio, username, password e URL del database).  

3. **Installazione di Lombok**:  
   - Verificare che il plugin **Lombok** sia installato e abilitato nel proprio IDE (ad esempio, IntelliJ o Eclipse).  
   - Per IntelliJ:  
     - Vai su `File > Settings > Plugins`, cerca **Lombok** e installalo.  
     - Assicurati che `Annotation Processors` sia abilitato (`File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`).  

4. **Avvio del Backend**:  
   - Una volta configurato, avviare l'applicazione Spring Boot tramite l'IDE o tramite il comando:  
     ```bash
     ./mvnw spring-boot:run
     ```  

### !--- Frontend ---!  
1. **Prerequisiti**:  
   - Assicurarsi che il Backend sia attivo.  

2. **Avvio del Frontend**:  
   - Aprire **Visual Studio Code**.  
   - Aprire la cartella `Frontend`.  
   - Installare le dipendenze richieste:  
     ```bash
     npm install
     ```  
   - Avviare il Frontend con:  
     ```bash
     npm start
     ```  

---

## Dipendenze del Progetto  
### Backend  
Le seguenti dipendenze sono utilizzate nel progetto:  

1. **Spring Boot**:  
   - `spring-boot-starter-data-jpa`: Fornisce il supporto per l'integrazione con JPA/Hibernate.  
   - `spring-boot-starter-security`: Per gestire la sicurezza dell'applicazione.  
   - `spring-boot-starter-validation`: Supporta la validazione tramite annotazioni come `@NotNull`, `@Size`.  
   - `spring-boot-starter-web`: Per creare applicazioni web e API REST.  

2. **Database**:  
   - `postgresql`: Driver per la connessione al database PostgreSQL.  

3. **Lombok**:  
   - `lombok`: Riduce il codice boilerplate generando automaticamente getter, setter, costruttori, ecc.  

4. **JWT (JSON Web Token)**:  
   - `jjwt-impl`, `jjwt-api`, `jjwt-jackson`: Librerie per gestire l'autenticazione basata su token JWT.  

5. **Testing**:  
   - `spring-boot-starter-test`: Include strumenti di test come JUnit e Mockito.  
   - `spring-security-test`: Per testare le funzionalità di sicurezza.  

---

## Tecnologie Utilizzate  
- **Backend**:  
  - Spring Boot  
  - Hibernate (JPA)  
  - PostgreSQL  
  - Lombok  
  - JSON Web Token (JWT)  
- **Frontend**:  
  - React  
  - HTML/CSS  

---

### Nota per il Frontend:
L'indirizzo del **proxy** per le chiamate al server può essere trovato nel file `package.json` all'interno della sezione `"proxy"`.
