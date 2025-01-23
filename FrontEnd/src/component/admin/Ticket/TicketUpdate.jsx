import React, { useCallback, useState, useEffect } from "react";
import { useParams, useNavigate } from 'react-router-dom';
import ApiService from "../../../service/ApiService";
import './TicketUpdate.css';

function TicketUpdate() {
    document.title = "Aggiorna Ticket";
    const { id } = useParams();
    const navigate = useNavigate();
    
    const [ticket, setTicket] = useState(null);
    const [category, setCategory] = useState("");
   
    
    const [selectCategory, setSelectCategory] = useState("");
    const [categories, setCategories] = useState([]); // Stato per le categorie
    const [showNewCategoryForm, setShowNewCategoryForm] = useState(false); // Stato per visualizzare il form "Nuova Categoria"
    
    const [errorMessage, setErrorMessage] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null); // Stato per il messaggio di successo
    
    
    const [newCategory, setNewCategory] = useState(""); // Stato per la nuova categoria
    const [newAnswer, setNewAnswer] = useState("");


    const [isChecked2, setIsChecked2] = useState(false);


    // Recupera i dati del ticket e le categorie dal database
    const fetchTicketAndCategories = useCallback(async () => {
        try {
            const ticketDB = await ApiService.retriveTicket(id);
            const categoriesDB = await ApiService.getAllCategories();
            setTicket(ticketDB.ticket);
            setCategories(categoriesDB.categoryDTOList);
        } catch (error) {
            console.error("Error fetching data:", error.message);
        }
    }, [id]);
    
    useEffect(() => {
        fetchTicketAndCategories();
    }, [fetchTicketAndCategories]);

    // Funzione di sanificazione dell'input
    const sanitizeInput = (input) => {
        return input.replace(/[^a-zA-Z0-9\s.,!'/?àèìòùéÀÈÌÒÙÉ]/g, "");
    };

    // Funzione di sanificazione dell'input
    const sanitizeInput2 = (input) => {
        return input.replace(/[^a-zA-Z\s]/g, "");
    };

    // Aggiorna newAnswer con testo sanificato
    const handleAnswerChange = (e) => {
        const sanitizedAnswer = sanitizeInput(e.target.value);
        setNewAnswer(sanitizedAnswer);
    };

    // Aggiorna newCategory con testo sanificato
    const handleCategoryChange = (e) => {
        const sanitizedCategory = sanitizeInput2(e.target.value.toUpperCase());
        setNewCategory(sanitizedCategory);
    };


    // Gestisce l'invio del form del ticket
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const risposta = window.confirm("Sei sicuro di voler procedere?");

        if (risposta) {

            // Controllo se non è stata scritta alcuna risposta
            if (!newAnswer) {
                setErrorMessage("Errore: non è stata scritta alcuna risposta!");
                setTimeout(() => setErrorMessage(""), 3000); // Rimuove l'errore dopo 3 secondi
                return; // Impedisce l'invio del form
            }

            // Controllo lo stato del checkbox e la categoria selezionata
            if (!isChecked2) {
                console.log("Checkbox non selezionato");

                // Controllo se la categoria non è stata impostata
                if (!category) {
                    console.log("Categoria non selezionata");
                    setErrorMessage("Errore: il campo categoria non è stato settato!");
                    setTimeout(() => setErrorMessage(""), 3000);
                    return;
                }

                // Controllo se la categoria è rimasta invariata rispetto al ticket originale
                if (ticket.category === category) {
                    console.log("Categoria invariata");
                    setErrorMessage("Errore: il campo categoria è rimasto invariato!");
                    setTimeout(() => setErrorMessage(""), 3000);
                    return;
                }
                
            }

            
            const formData1 = new FormData();
            formData1.append('category', category);
            formData1.append('answer', newAnswer);
            

            try {
                const response = await ApiService.updateTicket(id,formData1);

                if (response.message === "Success") {
                    setSuccessMessage("Operazione completata correttamente!");
                    setTimeout(() => {
                        navigate('/');
                    }, 2000);
                } else {
                    setErrorMessage("Errore nell'aggiornamento del ticket. Riprova.");
                }
            } catch (error) {
                console.error("Error updating ticket:" + error.message);
                setErrorMessage("Si è verificato un errore. Riprova.");
            }
        }

    };


    // Gestisce l'invio della nuova categoria
    const handleNewCategorySubmit = async (e) => {
        e.preventDefault();

        if(newCategory === "" || newCategory === null){
            setErrorMessage("Errore: categoria compilata in modo errato. Riprova.");
            return;
        }
        
        try {

            const response = await ApiService.addCategory(newCategory);

            if (response.message === "Success") {
                setSuccessMessage("Categoria aggiunta con successo!");
                setNewCategory("");
                setShowNewCategoryForm(false);
                setTimeout(() => {
                    setSuccessMessage("");
                    setSelectCategory("");
                    fetchTicketAndCategories();
                }, 3000);

            } else {
                setErrorMessage("Errore nell'aggiunta della categoria. Riprova.");
            }
        } catch (error) {
            console.error("Error adding category:", error.message);
            setErrorMessage("Si è verificato un errore nell'aggiunta della categoria.");
        }


    };

    return (
        <div className="update-ticket-wrapper">
            {ticket ? (
                <div className="ticket-wrapper">
                    <h1 className="ticket-title">Modifica TICKET</h1>
                    
                    <div className="campiTicket">
                        
                        <div className="titolo-risposta">
                            
                            <div className="FormTicketTitle">
                                <h2 className="testiT">Richiesta</h2>
                                <textarea className="textAreaT" 
                                    type="text"
                                    placeholder={ticket.title}
                                    disabled={true}
                                />
                            </div>

                            <div className="FormTicketNewAnswer">
                                <h2 className="testiT">Nuova Risposta</h2>
                                <textarea className="textAreaT" 
                                    type="text" 
                                    value={newAnswer}
                                    onChange={handleAnswerChange} 
                                    placeholder="Nuova risposta"
                                    maxLength="500" 
                                />
                            </div>

                        </div>

                        
                        <div className="bottom-Ticket">
                            <div className="FormTicketCategory">
                                <h2 className="testiT">Categoria attuale: {categories.find(cat => cat.id === ticket.category).type}</h2>
                                <select 
                                    disabled={isChecked2}
                                    value={selectCategory}
                                    onChange={(e) => {
                                        const selectedValue = e.target.value;
                                        setSelectCategory(selectedValue);
                                        if (selectedValue === "new") {
                                            setShowNewCategoryForm(true); // Mostra il form per la nuova categoria
                                        }else if (selectedValue === ""){
                                            setShowNewCategoryForm(false);
                                            setCategory(""); // Nasconde il form se si seleziona una categoria esistente
                                        }else {
                                            setShowNewCategoryForm(false);
                                            setCategory(selectedValue);
                                        }
                                    }}
                                >
                                    <option value="">Seleziona una categoria</option>
                                    {categories
                                        .filter(category => category.id !== 1)  // Filtra la categoria con id = 1
                                        .map((category) => (
                                            <option key={category.id} value={category.id}>{category.type}</option>
                                        ))
                                    }
                                    <option value="new">Nuova Categoria</option>
                                </select>
                                <div className="titleCheckBoxTicket">
                                    <h2 className="testiT"> Mantenere la stessa categoria?</h2>
                                    <input 
                                        label="check"
                                        type="checkbox" 
                                        checked={isChecked2} // Associa il valore della checkbox allo stato
                                        onChange={(e) => {
                                            setIsChecked2(e.target.checked); // Aggiorna isChecked2
                                            setCategory(ticket.category);
                                            setSelectCategory(""); // Aggiorna category con il valore di ticket.category
                                            if (!isChecked2 && showNewCategoryForm) {
                                                setShowNewCategoryForm(false);
                                            };
                                        }}// Gestisci il cambiamento
                                    ></input>
                                </div>
                            </div>

                            {/* Form per aggiungere una nuova categoria, visibile solo se si seleziona "Nuova Categoria" */}
                            {showNewCategoryForm && (
                                <form onSubmit={handleNewCategorySubmit} className="new-category-form-ticket">
                                    <h2 className="testiT">Aggiungi Nuova Categoria</h2>
                                    <input 
                                        type="text" 
                                        value={newCategory} 
                                        onChange={handleCategoryChange} 
                                        placeholder="Nome nuova categoria"
                                        maxLength="25"
                                        required 
                                    />
                                    <button className="FormbuttonTicket" type="submit">Aggiungi Categoria</button>
                                </form>
                            )}

                        </div>
                            <form onSubmit={handleSubmit}>
                                <div className="ButtonSendTicket">
                                    <button className="FormbuttonTicket" type="submit">CONFERMA</button>
                                </div>
                            </form>
                    </div>
                    {successMessage && <p className="success-message">{successMessage}</p>}
                    {errorMessage && <p className="error-message">{errorMessage}</p>}
                </div>

            ) : (
                <p>Caricamento...</p>
            )}
        </div>
    );

}

export default TicketUpdate;
