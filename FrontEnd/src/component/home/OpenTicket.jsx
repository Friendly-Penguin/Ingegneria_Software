import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import ApiService from '../../service/ApiService'; 
import "./OpenTicket.css";

const OpenTicket = () => {
    const location = useLocation();
    const navigate = useNavigate();
    
    document.title = "Apri Ticket";
    
    // Stati per memorizzare le categorie e i dati del form
    const [categories, setCategories] = useState([]);
    const [title, setTitle] = useState("");
    const [selectedCategory, setSelectedCategory] = useState("");
    const from = location.state?.from?.pathname || '/FAQ';
    
    // Funzione per ottenere le categorie
    const fetchCategories = async () => {
        try {
            const categoryResponse = await ApiService.getAllCategories();
            setCategories(categoryResponse.categoryDTOList);
        } catch (error) {
            console.error("Error fetching categories:", error.message);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    // Funzione di filtro per accettare solo caratteri sicuri
    const sanitizeInput = (input) => {
        // Permette solo caratteri alfanumerici, spazi, punti, virgole, e punti esclamativi e interrogativi
        return input.replace(/[^a-zA-Z0-9\s.,!'/?àèìòùéÀÈÌÒÙÉ]/g, "");
    };

    // Aggiorna il titolo filtrato
    const handleTitleChange = (e) => {
        const sanitizedTitle = sanitizeInput(e.target.value);
        setTitle(sanitizedTitle);
    };

    // Funzione per gestire il submit del form
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // Validazione di base
        if (!title || !selectedCategory) {
            alert("Per favore, compila tutti i campi");
            return;
        }

        const formData = new FormData();
        formData.append("title", title);
        formData.append("category", selectedCategory);
        formData.append("userID", localStorage.getItem("userID"));

        try {
            const response = await ApiService.openTicket(formData);
            console.log("Ticket aperto con successo: " + response.message);
            alert("Ticket aperto con successo!");
            navigate(from, { replace: true });
        } catch (error) {
            console.error("Errore durante l'invio del ticket: " + error);
            alert("Si è verificato un errore durante l'apertura del domanda.");
        }
    };

    return (
        <div className="add-question-wrapper">
            
            <h2>Apri un TICKET</h2>
            <form onSubmit={handleSubmit} className="form">
                <label htmlFor="title">Descrivi brevemente il tuo problema:</label>
                <div className="textarea-wrapper">
                    <textarea 
                        className="title-textarea"
                        value={title}
                        onChange={handleTitleChange} // Usa la funzione di sanificazione
                        placeholder="Scrivi qui..."
                        maxLength="300"
                    />
                    <span style={{  fontSize: '12px', color: '#888' }}>
                        {title.length} / 300 caratteri
                    </span>
                </div>
                <label htmlFor="category">Seleziona la categoria:</label>
                <select 
                    className="title"
                    value={selectedCategory} 
                    onChange={(e) => setSelectedCategory(e.target.value)} >
                    <option value="">-- Seleziona una categoria --</option>
                    {categories.map((category) => (
                        <option key={category.id} value={category.id}>{category.type}</option>
                    ))}
                </select>

                <button type="submit" style={{ padding: "10px", backgroundColor: "#007bff", color: "#fff", border: "none", borderRadius: "5px", cursor: "pointer" }}>
                    Invia ticket
                </button>
            </form>
        </div>
    );
};

export default OpenTicket;
