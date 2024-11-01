import React, { useCallback, useState, useEffect } from "react";
import { useParams, useNavigate } from 'react-router-dom';
import ApiService from "../../../service/ApiService";
import './QuestionUpdate.css';


function QuestionUpdate() {
    const { id } = useParams();
    const navigate = useNavigate();
    
    const [question, setQuestion] = useState(null);
    const [category, setCategory] = useState("");
    
    const [selectCategory, setSelectCategory] = useState("");
    const [categories, setCategories] = useState([]); // Stato per le categorie
    const [showNewCategoryForm, setShowNewCategoryForm] = useState(false); // Stato per visualizzare il form "Nuova Categoria"
    
    const [errorMessage, setErrorMessage] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null); // Stato per il messaggio di successo
    
    const [newTitle, setNewTitle] = useState("");
    const [newCategory, setNewCategory] = useState(""); // Stato per la nuova categoria
    const [newAnswer, setNewAnswer] = useState("");


    const [isChecked, setIsChecked] = useState(false);
    const [isChecked2, setIsChecked2] = useState(false);
    const [isCheckedAnswer, setIsCheckedAnswer] = useState(false)


    // Recupera i dati del ticket e le categorie dal database
    const fetchQuestionAndCategories = useCallback(async () => {
        try {
            const questionDB = await ApiService.getQuestionByID(id);
            const categoriesDB = await ApiService.getAllCategories();
            setQuestion(questionDB.question);
            setCategories(categoriesDB.categoryDTOList);
        } catch (error) {
            console.error("Error fetching data:", error.message);
        }
    }, [id]);
    
    useEffect(() => {
        fetchQuestionAndCategories();
    }, [fetchQuestionAndCategories]);


    // Gestisce l'invio del form del ticket
    const handleSubmit = async (e) => {
        e.preventDefault();

    const risposta = window.confirm("Sei sicuro di voler procedere?");
    if (risposta) {
        


        const formData = new FormData();

        //controllo titolo
        if(isChecked){
            
            formData.append('title', question.title)
        
        }else if(newTitle !== ""){

            formData.append('title', newTitle)
        
        }else{
            console.log("Errore titolo");
            setErrorMessage("Errore campo titolo: fornisci un titolo!");
            setTimeout(() => setErrorMessage(""), 3000);
            return;
        }

        //controllo risposta
        if(isCheckedAnswer){
            
            formData.append('answer', question.content)
        
        }else if(newAnswer !== ""){


            formData.append('answer', newAnswer)
        
        }else{
            console.log("Errore risposta");
            setErrorMessage("Errore campo risposta: fornisci una risposta!");
            setTimeout(() => setErrorMessage(""), 3000);
            return;
        }

        //controllo categoria
        if(isChecked2){
            
            formData.append('category', question.category)
        
        }else if(category !== ""){
                console.log("CAT-IF-ELSE-IF")
            if(category === String(question.category)){
                console.log("Errore categoria");
                setErrorMessage("Errore campo categoria: stessa categoria di partenza! Usa la checkbox");
                setTimeout(() => setErrorMessage(""), 3000);
                return;
            }else{
                formData.append('category', category)
            }

        }else{
            console.log("Errore categoria");
            setErrorMessage("Errore campo categoria: fornisci una categoria!");
            setTimeout(() => setErrorMessage(""), 3000);
            return;
        }
        
        console.log("Titolo: " + formData.get('title') + " Risposta: " + formData.get('answer') + " Categoria: " + formData.get('category'));

        try {
            const response = await ApiService.updateQuestion(id, formData);

            if (response.message === "Success") {
                setSuccessMessage("Operazione completata correttamente!");
                setTimeout(() => {
                    navigate('/');
                }, 2000);
            } else {
                setErrorMessage("Errore nell'aggiornamento del ticket. Riprova.");
            }
        } catch (error) {
            console.error("Error updating question:", error.message);
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
                    fetchQuestionAndCategories();
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
        <div className="update-wrapper">
            {question ? (
                <div className="question-wrapper">
                    <h1 className="question-title">Modifica DOMANDA</h1>
                    
                    <div className="campiQuestion">
                        
                        <div className="titoli">
                            
                            <div className="FormQuestionTitle">
                                <h2 className="testi">Titolo attuale</h2>
                                <textarea className="textArea" 
                                    type="text"
                                    placeholder={question.title}
                                    disabled={true}
                                />
                            </div>

                            <div className="FormQuestionNewTitle">
                                <h2 className="testi">Nuovo titolo</h2>
                                <textarea className="textArea" 
                                    type="text" 
                                    value={newTitle}
                                    onChange={(e) => setNewTitle(e.target.value)} 
                                    placeholder="Nuovo titolo"
                                    maxLength="300" 
                                    disabled={isChecked}
                                />
                                <div className="titleCheckBox">
                                    <h2 className="testi"> Mantenere lo stesso titolo?</h2>
                                    <input 
                                        label="check"
                                        type="checkbox" 
                                        checked={isChecked} // Associa il valore della checkbox allo stato
                                        onChange={(e) => setIsChecked(e.target.checked)}// Gestisci il cambiamento
                                    ></input>
                                </div>
                            </div>

                        </div>

                        <div className="risposte">
                            
                            <div className="FormQuestionAnswer">
                                <h2 className="testi">Risposta attuale</h2>
                                <textarea className="textArea" 
                                    type="text"
                                    placeholder={question.content}
                                    disabled={true}
                                />
                            </div>

                            <div className="FormQuestionNewAnswer">
                                <h2 className="testi">Nuova Risposta</h2>
                                <textarea className="textArea" 
                                    type="text" 
                                    value={newAnswer}
                                    onChange={(e) => setNewAnswer(e.target.value)} 
                                    placeholder="Nuova risposta"
                                    maxLength="300" 
                                    disabled={isCheckedAnswer}
                                />
                                <div className="titleCheckBox">
                                    <h2 className="testi"> Mantenere la stessa risposta?</h2>
                                    <input 
                                        label="check"
                                        type="checkbox" 
                                        checked={isCheckedAnswer} // Associa il valore della checkbox allo stato
                                        onChange={(e) => setIsCheckedAnswer(e.target.checked)}// Gestisci il cambiamento
                                    ></input>
                                </div>
                            </div>

                        </div>
                        
                        <div className="bottom">
                            <div className="FormticketCategory">
                                <h2 className="testi">Categoria attuale: {categories.find(cat => cat.id === question.category).type}</h2>
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
                                <div className="titleCheckBox">
                                    <h2 className="testi"> Mantenere la stessa categoria?</h2>
                                    <input 
                                        label="check"
                                        type="checkbox" 
                                        checked={isChecked2} // Associa il valore della checkbox allo stato
                                        onChange={(e) => {
                                            setIsChecked2(e.target.checked); // Aggiorna isChecked2
                                            setCategory(question.category); // Aggiorna category con il valore di question.category
                                            setSelectCategory(""); 
                                            if (!isChecked2 && showNewCategoryForm) {
                                                setShowNewCategoryForm(false);
                                            }; // 
                                        }}// Gestisci il cambiamento
                                    ></input>
                                </div>
                            </div>

                            {/* Form per aggiungere una nuova categoria, visibile solo se si seleziona "Nuova Categoria" */}
                            {showNewCategoryForm && (
                                <form onSubmit={handleNewCategorySubmit} className="new-category-form">
                                    <h2 className="testi">Aggiungi Nuova Categoria</h2>
                                    <input 
                                        type="text" 
                                        value={newCategory} 
                                        onChange={(e) => setNewCategory(e.target.value.toUpperCase())} 
                                        placeholder="Nome nuova categoria"
                                        maxLength="25"
                                        required 
                                    />
                                    <button className="Formbutton" type="submit">Aggiungi Categoria</button>
                                </form>
                            )}

                        </div>
                            <form onSubmit={handleSubmit}>
                                <div className="ButtonSend">
                                    <button className="Formbutton" type="submit">CONFERMA</button>
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

export default QuestionUpdate;
