import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useCallback } from 'react';
import ApiService from '../../service/ApiService';
import "./HomePage.css";

function HomePage() {
    document.title = "FAQ";
    const [questions, setQuestions] = useState([]); // Stato per le domande e categorie
    const [expandedCategory, setExpandedCategory] = useState(null); // Stato per gestire l'espansione dei menu a tendina
    const [searchTerm, setSearchTerm] = useState(""); // Stato per il termine di ricerca
    const [loading, setLoading] = useState(true); // Stato per il caricamento dei dati
    const isAuthenticated = ApiService.isAuthenticated(); // Controllo se l'utente è autenticato
    const [ticketCount , setTicketCount] = useState("");
    const navigate = useNavigate();

    // Funzione per caricare le categorie e le domande dal database
    
    const fetchQuestionsAndCategories = useCallback(async () => {
        try {
            if (isAuthenticated) {
                const response = await ApiService.getTicketCount(localStorage.getItem("userID"));
                setTicketCount(response.ticketCount);
            }
    
            const categoryResponse = await ApiService.getAllCategories();
            const allCategories = categoryResponse.categoryDTOList;
    
            // Crea una mappa per associare ID categoria al tipo
            const categoryMap = allCategories.reduce((acc, category) => {
                acc[category.id] = category.type;
                return acc;
            }, {});
    
            const questionResponse = await ApiService.getAllQuestion();
            const allQuestions = questionResponse.questionDTOList;
    
            // Raggruppa le domande per tipo di categoria
            const categorizedQuestions = allQuestions.reduce((acc, question) => {
                const categoryType = categoryMap[question.category]; // Ottieni il tipo usando la mappa
                if (!acc[categoryType]) {
                    acc[categoryType] = [];
                }
                acc[categoryType].push(question);
                return acc;
            }, {});
    
            setQuestions(categorizedQuestions); // Imposta le domande raggruppate per tipo di categoria
        } catch (error) {
            console.error("Error fetching questions or categories:", error.message);
        } finally {
            setLoading(false); // Termina il caricamento
        }
    }, [isAuthenticated]);  // Usa useCallback per memorizzare la funzione

   

    useEffect(() => {
        fetchQuestionsAndCategories();
    }, [fetchQuestionsAndCategories]);

   
    // Funzione per gestire l'espansione delle categorie
    const toggleCategory = (category) => {
        setExpandedCategory(expandedCategory === category ? null : category); // Espande/chiude il menu a tendina
    };

    // Funzione per filtrare le domande in base al termine di ricerca
    const filteredQuestions = Object.keys(questions).reduce((acc, category) => {
        const filtered = questions[category].filter((question) => {
            const searchLower = searchTerm.toLowerCase();
            return (
                question.title.toLowerCase().includes(searchLower) ||
                question.content.toLowerCase().includes(searchLower)
            );
        });

        if (filtered.length > 0) {
            acc[category] = filtered;
        }

        return acc;
    }, {});

    // Funzione per evidenziare il termine di ricerca
    const highlightText = (text) => {
        if (!searchTerm) return text; // Se non c'è un termine di ricerca, restituisci il testo originale
        const regex = new RegExp(`(${searchTerm})`, 'gi'); // Crea un'espressione regolare per il termine di ricerca
        const parts = text.split(regex); // Dividi il testo in base al termine di ricerca
        return parts.map((part, index) => 
            part.toLowerCase() === searchTerm.toLowerCase() ? (
                <span key={index} className="highlight">{part}</span> // Avvolgi il termine di ricerca in un <span>
            ) : part
        );
    };

    // Funzione per aprire un nuovo ticket
    const handleAddNewTicket = () => {
        if (!isAuthenticated) {
            const confirm = window.confirm("Per aprire un ticket, esegui il login");
            if (confirm) {
                navigate('/login');
            }
        } else if(ticketCount.toString() === "3") {
            window.alert("Hai aperto il numero massimo di Ticket ( 3 ). \nAspetta che un ADMIN ti risponda prima di aprirne altri!")
            return;
        
            }else{
                navigate('/ticket');
            }
    };

    if (loading) {
        return <p className="loading-message">Caricamento in corso...</p>; // Messaggio di caricamento
    }

    return (
        <div className="home">
            <div className="introduzione">
                <h1 className="faq-title">HAI DELLE DOMANDE?</h1>
                <h2 className="h2Title">Noi abbiamo le risposte (....la maggiorparte delle volte!)</h2>
                <p className="fistPar">Qui sotto troverai le risposte alle domande più comuni che potresti avere visitando il nostro sito. </p>
                <p>Se non riesci a trovare una risposta usa il form per inviare la tua richiesta!</p>
                <img src="/assets/images/faq_img.png" alt="LogoFAQ"></img>
            </div>

            {/* Casella di input per la ricerca */}
            <div className="search-wrapper">
                <input
                    type="text"
                    placeholder="Trova parola chiave..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)} // Aggiorna lo stato del termine di ricerca
                    className="search-input"
                />
            </div>

            {/* Sezione inserimento nuova domanda */}
            <div className="add-wrapper">
                <h1>Non hai trovato una risposta utile?</h1>
                <button onClick={handleAddNewTicket} className="buttonTicket">Apri un ticket</button>
            </div>

            <div className="titolo-Domande">
                <h1>Domande presenti:</h1>
            </div>
            {/* Visualizzazione delle categorie */}
            <div className="contenitoreHome">
                <div className="categories-wrapper">
                    {Object.keys(filteredQuestions).length > 0 ? (
                        Object.keys(filteredQuestions).map((category) => (
                            <div
                                key={category}
                                onClick={() => toggleCategory(category)}
                                className={`categories-obj ${expandedCategory === category ? 'active' : ''}`}
                            >
                                <span>{category}</span>
                            </div>
                        ))
                    ) : (
                        <span>Nessun Risultato</span>
                    )}
                </div>

                {/* Visualizzazione delle domande della categoria espansa */}
                {expandedCategory && filteredQuestions[expandedCategory] && (
                    <div className="questions-wrapper">
                        <h2 style={{ marginBottom: '10px' }}>Domande per la categoria: {expandedCategory}</h2>
                        <ul>
                            {filteredQuestions[expandedCategory].map((question) => (
                                <li key={question.id}>
                                    <strong>{highlightText(question.title)}</strong>
                                    <p>{highlightText(question.content)}</p>
                                    <hr style={{ border: '1px solid #ccc', margin: '10px 0' }} />
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
}

export default HomePage;
