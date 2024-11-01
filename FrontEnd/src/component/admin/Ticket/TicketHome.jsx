import React from "react";
import { useState, useEffect, useCallback } from "react";
import { Link } from 'react-router-dom';
import ApiService from "../../../service/ApiService";
import { TiPencil } from "react-icons/ti";
import './TicketHome.css';




function TicketHome() {
    
    const [ticketsNoAnsw, setTicketsNoAnsw] = useState([]);
    const [tickets, setTickets] = useState([]);
    const [expandedCategory, setExpandedCategory] = useState(null);
    const [selectedTipologia, setSelectedTipologia] = useState(null); // Stato per tipologia selezionata
    const [categoryMap, setCategoryMap] = useState({});
    const [loadingTicketNoAnsw, setLoadingTicketNoAnsw] = useState(true);
    const [loadingTicket, setLoadingTicket] = useState(true);
    const [loadingCat, setLoadingCat] = useState(true);

    // Funzione per caricare i ticket senza risposta dal database
    const fetchTickets = useCallback(async () => {
        try {
            setLoadingTicketNoAnsw(true);
            
            // Chiamata API per ottenere i ticket 
            const ticketResponse = await ApiService.getAllNTicket();
            const alltickets = ticketResponse.ticketDTOList;
            
            // Raggruppa le domande per tipo di categoria
            const categorizedTicket = alltickets.reduce((acc, ticket) => {
                const categoryType = categoryMap[ticket.category];
                if (!acc[categoryType]) {
                    acc[categoryType] = [];
                }
                acc[categoryType].push(ticket);
                return acc;
            }, {});

            setTicketsNoAnsw(categorizedTicket);

        } catch (error) {
            console.error("Error fetching tickets: " + error.message);
        } finally {
            setLoadingTicketNoAnsw(false);
        }
    }, [categoryMap]); // Dipendenza su `categoryMap`


    // Funzione per caricare i ticket con risposta dal database
    const fetchTicketsNoAnsw = useCallback(async () => {
        try {
            setLoadingTicket(true);
            
            // Chiamata API per ottenere tutti i ticket 
            const ticketResponse = await ApiService.getAllYTicket();
            const alltickets = ticketResponse.ticketDTOList;
            
            // Raggruppa le domande per tipo di categoria
            const categorizedTicket = alltickets.reduce((acc, ticket) => {
                const categoryType = categoryMap[ticket.category];
                if (!acc[categoryType]) {
                    acc[categoryType] = [];
                }
                acc[categoryType].push(ticket);
                return acc;
            }, {});

            setTickets(categorizedTicket);

        } catch (error) {
            console.error("Error fetching tickets: " + error.message);
        } finally {
            setLoadingTicket(false);
        }
    }, [categoryMap]); // Dipendenza su `categoryMap`


    const fetchCategories = async () => {
        try {
            setLoadingCat(true);
            // Chiamata API per ottenere tutte le categorie
            const categoryResponse = await ApiService.getAllCategories();
            const allCategories = categoryResponse.categoryDTOList;

            // Crea una mappa per associare ID categoria al tipo
            const categoryMapGenerated = allCategories.reduce((acc, category) => {
                acc[category.id] = category.type;
                return acc;
            }, {});

            setCategoryMap(categoryMapGenerated);

        } catch (error) {
            console.error("Error fetching categories: " + error.message);
        } finally {
            setLoadingCat(false);
        }
    };

    useEffect(() => {
        fetchCategories(); // Carica le categorie
    }, []);

    useEffect(() => {
        if (!loadingCat) { 
            fetchTickets(); // Carica i ticket senza riposta solo dopo il caricamento delle categorie
            fetchTicketsNoAnsw(); // Carico i ticket con risposta solo dopo il caricamento delle categorie
        }
    }, [fetchTickets, fetchTicketsNoAnsw, loadingCat]);

     // Funzione per alternare l'espansione della categoria selezionata
    const toggleCategory = (category) => {
        setExpandedCategory(expandedCategory === category ? null : category);
    };

    // Funzione per selezionare una tipologia
    const toggleTipologia = (tipologia) => {
        setSelectedTipologia(selectedTipologia === tipologia ? null : tipologia);
    };

    if (loadingTicket || loadingCat || loadingTicketNoAnsw) {
        return <p className="categories-Loading">Caricamento informazioni...</p>; // Mostra il messaggio di caricamento
    }

    return (
        
        <div className="ticket-wrapper-admin">
            <h1 className="Title-ticket">Ticket Aperti</h1>
            
            <h1 className="categories-Title">Tipologia</h1>
            
            <div className="testata">  
                
                {/* Bottone per Ticket Aperti */}
                <div
                    onClick={() => toggleTipologia("Ticket Aperti")}
                    className={`categories-obj-testata-admin ${selectedTipologia === "Ticket Aperti" ? 'active' : ''}`}
                >
                    <span>Ticket Aperti</span>
                </div>

                {/* Bottone per Ticket Chiusi */}
                <div
                    onClick={() => toggleTipologia("Ticket Chiusi")}
                    className={`categories-obj-testata-admin ${selectedTipologia === "Ticket Chiusi" ? 'active' : ''}`}
                >
                    <span>Ticket Chiusi</span>
                </div>

            </div> {/* chiusura testata */}

            {/* Sezione per visualizzare i ticket aperti o chiusi */}
            {selectedTipologia === "Ticket Aperti" && (
                <div className="contenitore-admin">
                    <h2>Ticket Aperti per Categoria:</h2>
                    <div className="categories-wrapper-admin">
                        {Object.keys(ticketsNoAnsw).length > 0 ? (
                            Object.keys(ticketsNoAnsw).map((category) => (
                                <div
                                    key={category}
                                    onClick={() => toggleCategory(category)}
                                    className={`categories-obj-admin ${expandedCategory === category ? 'active' : ''}`}
                                >
                                    <span>{category}</span>
                                </div>
                            ))
                        ) : (
                            <p>Nessun ticket aperto presente</p>
                        )}
                    </div>
                    {expandedCategory && (
                        <div className="questions-wrapper-admin">
                            <h2 style={{ marginBottom: '10px' }}>Domande per la categoria: {expandedCategory}</h2>
                            {ticketsNoAnsw[expandedCategory].map((ticket) => (
                                <div className="question-div" key={ticket.id} style={{ marginBottom: '20px' }}>
                                    <div className="question-div-info1">
                                        <h2>• {ticket.title}</h2>
                                        <Link className="LinkT" to={`/ticketUpdate/${ticket.id}`}>
                                            <TiPencil className="icon-Update" />Aggiorna
                                        </Link>
                                    </div>
                                    <hr style={{ border: '1px solid #ccc', margin: '10px 0' }} />
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            )}

            {selectedTipologia === "Ticket Chiusi" && (
                <div className="contenitore-admin">
                    <h2>Ticket Chiusi per Categoria:</h2>
                    <div className="categories-wrapper-admin">
                        {Object.keys(tickets).length > 0 ? (
                            Object.keys(tickets).map((category) => (
                                <div
                                    key={category}
                                    onClick={() => toggleCategory(category)}
                                    className={`categories-obj-admin ${expandedCategory === category ? 'active' : ''}`}
                                >
                                    <span>{category}</span>
                                </div>
                            ))
                        ) : (
                            <p>Nessun ticket chiuso presente</p>
                        )}
                    </div>
                    {expandedCategory && (
                        <div className="questions-wrapper-admin">
                            <h2 style={{ marginBottom: '10px' }}>Domande per la categoria: {expandedCategory}</h2>
                            {tickets[expandedCategory].map((ticket) => (
                                <div className="question-div" key={ticket.id} style={{ marginBottom: '20px' }}>
                                    <div className="question-div-info1">
                                        <div className="ticketDetails">
                                            <h2>• {ticket.title}</h2>
                                            <p><b>Risposta: </b>{ticket.answer}</p>
                                        </div>
                                        <Link className="LinkT" to={`/ticketUpdate/${ticket.id}`}>
                                            <TiPencil className="icon-Update" />Aggiorna
                                        </Link>
                                    </div>
                                    <hr style={{ border: '1px solid #ccc', margin: '10px 0' }} />
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            )}
    </div>
    );
}

export default TicketHome;