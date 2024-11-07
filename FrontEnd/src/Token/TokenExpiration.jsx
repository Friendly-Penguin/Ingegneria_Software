import { useEffect, useState, useCallback } from "react";
import {jwtDecode} from 'jwt-decode';
import { useNavigate } from "react-router-dom";

const TokenExpirationWarning = () => {
  const token = localStorage.getItem('token');
  const navigate = useNavigate();
  const [warningTriggered, setWarningTriggered] = useState(false);
  const preWarningTime = 60 * 1000; // 1 minuto in millisecondi

  const checkTokenExpiration = (token) => {
    if (!token) return false;

    const decodedToken = jwtDecode(token);
    const expirationTime = decodedToken.exp * 1000;  // Converti in millisecondi
    const currentTime = Date.now(); // Tempo corrente in millisecondi
    const timeLeft = expirationTime - currentTime;
    console.log("Exp token: " + expirationTime)
    return timeLeft;
  };

 
  // Usa useCallback per evitare che la funzione venga ricreata ad ogni render
  const handleWarning = useCallback(() => {
    alert("Sessione scaduta!\nEsegui nuovamente l'accesso!");
    setWarningTriggered(true);
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('userID')
    navigate("/login");  // Reindirizza alla pagina di login
  }, [navigate]);

  useEffect(() => {
    if (!token) return;

    const checkExpiration = () => {
      const timeLeft = checkTokenExpiration(token);
      console.log("Tempo rimanente: " + timeLeft);

      if (timeLeft <= preWarningTime && !warningTriggered) {
        handleWarning();
      }
    };

    // Controlla la scadenza ogni 40 secondi
    const interval = setInterval(checkExpiration, 40000);

    // Cleanup dell'intervallo
    return () => clearInterval(interval);
  }, [token, warningTriggered, preWarningTime, handleWarning]);

  return null; // Non viene renderizzato nulla
};

export default TokenExpirationWarning;
