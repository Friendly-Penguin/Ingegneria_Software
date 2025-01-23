import React, { useState, useEffect } from "react";
import ApiService from "../../../service/ApiService";
import { FaRegUser } from "react-icons/fa";
import { RxCross2 } from "react-icons/rx";
import { RiAdminFill } from "react-icons/ri";
import './UserHome.css';

function UserHome() {
    document.title = "Sezione Utenti";
    const [loadingUser, setLoadingUser] = useState(true);
    const [user, setUser] = useState({});
    const [errorMessage, setErrorMessage] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const [selectedTipo, setSelectedTipo] = useState(null);
  // Funzione per caricare tutti gli utenti
  const fetchUser = async () => {
    try {
        setLoadingUser(true);
        const userResponse = await ApiService.getAllUser();
        const allUser = userResponse.userDTOList;

        // Raggruppa gli utenti per ruolo
        const categorizedUser = allUser.reduce((acc, user) => {
            const role = user.role; // Usa direttamente il ruolo dall'utente
            if (!acc[role]) {
                acc[role] = []; // Inizializza un array per il ruolo se non esiste
            }
            acc[role].push(user); // Aggiungi l'utente all'array del ruolo corrispondente
            return acc;
        }, {});

        setUser(categorizedUser); // Imposta lo stato con gli utenti raggruppati
    } catch (error) {
        console.error("Error fetching user: " + error.message);
        setErrorMessage("Si è verificato un errore durante il caricamento degli utenti.");
    } finally {
        setLoadingUser(false);
    }
};

    useEffect(() => {
        fetchUser(); // Carica gli utenti
    }, []);

    // Funzione per rimuovere un utente
    const handleRemoveUser = async (userID, nomeUtente) => {
        const response = window.confirm(`Sei sicuro di voler rimuovere l'utente: ${nomeUtente}?`);
        if (response) {
            try {
                await ApiService.deleteUser(userID);
                setSuccessMessage("Utente rimosso con successo!");
                setTimeout(() => setSuccessMessage(""), 3000);
            } catch (error) {
                console.error("Error removing user:", error.message);
                setErrorMessage("Si è verificato un errore nella rimozione dell' utente.");
            }
        }
    };

    // Funzione per mostrare gli utenti di una categoria
    const handleCategoryClick = (category) => {
        setSelectedTipo(selectedTipo === category ? null : category); // Toggle the active category
    };

    const formatLastLoginDate = (lastLoginDate) => {
        if(lastLoginDate !== undefined){
        // Converti il valore LocalDateTime in oggetto Date
        const lastLogin = new Date(lastLoginDate);
        const now = new Date();
        const timeDiff = now - lastLogin; // Differenza in millisecondi
    
        const daysDiff = Math.floor(timeDiff / (1000 * 60 * 60 * 24)); // Converte la differenza in giorni
    
        if (daysDiff < 1) {
            return "Oggi"; // Se l'ultimo accesso è oggi
        } else if (daysDiff === 1) {
            return "Ieri"; // Se l'ultimo accesso è ieri
        } else {
            return `${daysDiff} giorni fa`; // Altrimenti, mostra i giorni trascorsi
        }
    }
    else
        return "Nessun accesso";
    };

    if (loadingUser) {
        return <p className="categories-Loading">Caricamento informazioni...</p>; // Mostra il messaggio di caricamento
    }

    return (
        <div className="user-wrapper">
            <h1>UTENTI</h1>

            <div className="testata-User">  
                
                {/* Bottone per Ticket Aperti */}
                <div
                    onClick={() => handleCategoryClick("user")}
                    className={`categories-obj-USER ${selectedTipo === "user" ? 'active' : ''}`}
                >
                    <span>Utenti Base</span>
                </div>

                {/* Bottone per Ticket Chiusi */}
                <div
                    onClick={() => handleCategoryClick("admin")}
                    className={`categories-obj-USER ${selectedTipo === "admin" ? 'active' : ''}`}
                >
                    <span>Admin</span>
                </div>

            </div> {/* chiusura testata */}

            {/* Sezione per visualizzare gli utenti base */}
            {selectedTipo === "user" && (
                <div className="utentiBase-wrapper">
                    {Object.keys(user).length > 0 && (
                    // Filtra per includere solo gli utenti con ruolo 'USER'
                        user['USER'] && user['USER'].length > 0 ? (
                        user['USER'].map((u) => (
                        <div className="UserTotale" key={u.id} >
                            <div className="User-Info">
                                <FaRegUser className="UserIcon" />
                                <div className="User-Details">
                                    <span><b>Nome: </b>{u.name}</span>
                                    <span><b>Ultimo Accesso: </b>{formatLastLoginDate(u.lastLoginDate)}</span>
                                </div>
                                <div className="UserButton" onClick={() =>handleRemoveUser(u.id, u.name)}>
                                    <RxCross2 className="IconDeleteUS"/>
                                    <h1>Elimina</h1> 
                                </div>
                            </div>
                            <hr/>
                        </div>
                    ))
            ) : (
                <p>Nessun utente con ruolo 'USER' presente</p>
            ))}
    </div>
)}
             {/* Sezione per visualizzare gli utenti ADMIN */}
             {selectedTipo === "admin" && (
                <div className="utentiBase-wrapper">
                    {Object.keys(user).length > 0 && (
                    // Filtra per includere solo gli utenti con ruolo 'ADMIN'
                        user['ADMIN'] && user['ADMIN'].length > 0 ? (
                        user['ADMIN'].map((u) => (
                        <div className="UserTotale" key={u.id} >
                            <div className="User-Info">
                                <RiAdminFill className="UserIcon" />
                                <div className="User-Details">
                                    <span><b>Nome: </b>{u.name}</span>
                                    <span><b>Ultimo Accesso: </b>{formatLastLoginDate(u.lastLoginDate)}</span>
                                </div>
                                <div className="UserButton" onClick={() =>handleRemoveUser(u.id, u.name)}>
                                    <RxCross2 className="IconDeleteUS"/>
                                    <h1>Elimina</h1> 
                                </div>
                            </div>
                            <hr/>
                        </div>
                    ))
            ) : (
                <p>Nessun utente con ruolo 'USER' presente</p>
            ))}
    </div>
)}


           
            {successMessage && <p className="success-message">{successMessage}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
        </div>
    );
}

export default UserHome;
