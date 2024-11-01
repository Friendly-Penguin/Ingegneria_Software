import React, {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";
import ApiService from "../../../service/ApiService";
import './AddQuestion.css';





function AddQuestion(){

    const navigate = useNavigate();
    
    const [newCategory, setNewCategory] = useState("");
    
    //campi formData
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [selectedCategory, setSelectCategory] = useState("");
   
    const [categories, setCategories] = useState([]); // Stato per le categorie
    const [showNewCategoryForm, setShowNewCategoryForm] = useState(false); // Stato per visualizzare il form "Nuova Categoria"
    
    const [errorMessage, setErrorMessage] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null); // Stato per il messaggio di successo
    


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
   
    

    // Gestisce l'invio del form della domanda
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const risposta = window.confirm("Sei sicuro di voler procedere?");

        if (risposta) {
            
            // Controllo se non è stata scritta alcuna risposta
            if (title === "") {
                setErrorMessage("Errore: non è stato scritto alcun titolo!");
                setTimeout(() => setErrorMessage(""), 3000); // Rimuove l'errore dopo 3 secondi
                return; // Impedisce l'invio del form
            }
            
            // Controllo se non è stata scritta alcuna risposta
            if(content === ""){
                setErrorMessage("Errore: non è stata scritta alcuna risposta!");
                setTimeout(() => setErrorMessage(""), 3000); // Rimuove l'errore dopo 3 secondi
                return; // Impedisce l'invio del form
            }

            // Controllo se non è stata selezionata alcuna categoria
            if(selectedCategory === ""){
                setErrorMessage("Errore: non è stata scritta alcuna risposta!");
                setTimeout(() => setErrorMessage(""), 3000); // Rimuove l'errore dopo 3 secondi
                return; // Impedisce l'invio del form
            }

            // Passo oltre i controlli, quindi imposto la categoria e creo il formData
            
            
            const formData = new FormData();
            formData.append('title', title)
            formData.append('category', selectedCategory);
            formData.append('userID', localStorage.getItem("userID"))
            formData.append('content', content);
            
            
            console.log("Sono fuori dalle condizioni\nCampi:\nTitolo: " + formData.get('title') + " Content: " + formData.get('content') + " Cat: " + formData.get('category'));
            
            

            try {
                const response = await ApiService.addQuestion(formData);

                if (response.message === "Success") {
                    setSuccessMessage("Operazione completata correttamente!");
                    setTimeout(() => {
                        navigate('/');
                    }, 2000);
                } else {
                    setErrorMessage("Errore nell'aggiunta della domanda. Riprova.");
                }
            } catch (error) {
                console.error("Error adding question:" + error.message);
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
                    fetchCategories();
                }, 3000);

            } else {
                setErrorMessage("Errore nell'aggiunta della categoria. Riprova.");
            }
        } catch (error) {
            console.error("Error adding category:", error.message);
            setErrorMessage("Si è verificato un errore nell'aggiunta della categoria.");
        }


    };


    return(
    <div className="add-question1-wrapper">
            
            <div className="addQuestion-wrapper">
                <h1 className="addQuestion-title">Nuova DOMDANDA</h1>
                
                <div className="campiQuestion">
                    
                    <div className="titolo-Domanda">
                        
                        <div className="FormAddQuestionTitle">
                            <h2 className="testiD">Titolo</h2>
                            <textarea className="textAreaD" 
                                type="text" 
                                value={title}
                                onChange={(e) => setTitle(e.target.value)} 
                                placeholder="Titolo"
                                maxLength="300" 
                            />
                        </div>

                        <div className="FormAddQuestionAnswer">
                            <h2 className="testiD">Nuova Risposta</h2>
                            <textarea className="textAreaD" 
                                type="text" 
                                value={content}
                                onChange={(e) => setContent(e.target.value)} 
                                placeholder="Risposta"
                                maxLength="500" 
                            />
                        </div>

                    </div>

                    
                    <div className="bottom-addQuestion">
                        <div className="FormAddQuestionCategory">
                        <h2 className="testiD">Categoria</h2>
                            <select 
                                value={selectedCategory}
                                onChange={(e) => {
                                    const selectedValue = e.target.value;
                                    setSelectCategory(selectedValue);
                                    if (selectedValue === "new") {
                                        setShowNewCategoryForm(true); // Mostra il form per la nuova categoria
                                    }else if (selectedValue === ""){
                                        setShowNewCategoryForm(false);
                                    }else {
                                        setShowNewCategoryForm(false);
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
                        </div>

                        {/* Form per aggiungere una nuova categoria, visibile solo se si seleziona "Nuova Categoria" */}
                        {showNewCategoryForm && (
                            <form onSubmit={handleNewCategorySubmit} className="new-category-form-addQuestion">
                                <h2 className="testiD">Aggiungi Nuova Categoria</h2>
                                <input 
                                    type="text" 
                                    value={newCategory} 
                                    onChange={(e) => setNewCategory(e.target.value.toUpperCase())} 
                                    placeholder="Nome nuova categoria"
                                    maxLength="25"
                                    required 
                                />
                                <button className="FormbuttonAddQuestion" type="submit">Aggiungi Categoria</button>
                            </form>
                        )}

                    </div>
                        <form onSubmit={handleSubmit}>
                            <div className="ButtonSendNewQuestion">
                                <button className="FormbuttonAddQuestion" type="submit">CONFERMA</button>
                            </div>
                        </form>
                </div>
                {successMessage && <p className="success-message">{successMessage}</p>}
                {errorMessage && <p className="error-message">{errorMessage}</p>}
            </div>
    </div>

);

}

export default AddQuestion;