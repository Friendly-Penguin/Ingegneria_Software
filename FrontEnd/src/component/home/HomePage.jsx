import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from '../../service/ApiService';
import "./HomePage.css";

function HomePage() {
    const [questions, setQuestions] = useState([]); // Stato per le domande e categorie
    const [expandedCategory, setExpandedCategory] = useState(null); // Stato per gestire l'espansione dei menu a tendina
    const [searchTerm, setSearchTerm] = useState(""); // Stato per il termine di ricerca
    const isAuthenticated = ApiService.isAuthenticated(); // Controllo se l'utente è autenticato
    const navigate = useNavigate();

    // Funzione per caricare le categorie e le domande dal database
    const fetchQuestionsAndCategories = async () => {
        try {
            // Chiamata API per ottenere tutte le categorie
            const categoryResponse = await ApiService.getAllCategories();
            const allCategories = categoryResponse.categoryDTOList;

            // Crea una mappa per associare ID categoria al tipo
            const categoryMap = allCategories.reduce((acc, category) => {
                acc[category.id] = category.type;
                return acc;
            }, {});

            // Chiamata API per ottenere tutte le domande con le relative risposte
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
        }
    };

    useEffect(() => {
        fetchQuestionsAndCategories();
    }, []);

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

    const handleAddNewTicket = () => {
        if (!isAuthenticated) {
            const confirm = window.confirm("Per aprire un ticket, esegui il login");
            if (confirm) {
                navigate('/login');
            }
        } else {
            navigate('/ticket');
        }
    };

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

            {/* Visualizzazione delle categorie */}
            <div className="contenitore">
                <div className="categories-wrapper">
                    {Object.keys(filteredQuestions).map((category) => (
                        <div
                            key={category}
                            onClick={() => toggleCategory(category)}
                            className={`categories-obj ${expandedCategory === category ? 'active' : ''}`}
                        >
                            <span>{category}</span>
                        </div>
                    ))}
                </div>

                {/* Visualizzazione delle domande della categoria espansa */}
                {expandedCategory && (
                    <div className="questions-wrapper">
                        <h2 style={{ marginBottom: '10px' }}>Domande per la categoria: {expandedCategory}</h2>
                        <ul>
                            {filteredQuestions[expandedCategory].map((question) => (
                                <li key={question.id}>
                                    <strong>{question.title}</strong>
                                    <p>{question.content}</p>
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
