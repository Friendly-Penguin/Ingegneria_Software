import React, {useState, useEffect} from "react";
import ApiService from "../../../service/ApiService";
import './ViewCat.css';

function ViewCat(){

    const [categories, setCategories] = useState([]);
    const [newCategory, setNewCategory] = useState("");

    const [errorMessage, setErrorMessage] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null); // Stato per il messaggio di successo
    const [loading, setLoading] = useState(true);

    // Funzione per ottenere le categorie
    const fetchCategories = async () => {
        try {
            const categoryResponse = await ApiService.getAllCategories();
            setCategories(categoryResponse.categoryDTOList);
        } catch (error) {
            console.error("Error fetching categories:", error.message);
        }finally {
            setLoading(false); // Imposta il caricamento a false quando i dati sono stati recuperati o si è verificato un errore
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);


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
                setTimeout(() => {
                    setSuccessMessage("");
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


    // Funzione per rimuovere una categoria
    const handleRemoveCategory = async (categoryID, categoryName) => {
        
        const response = window.confirm(`Sei sicuro di voler rimuovere la categoria: ${categoryName}?`);
        
        if(response){
            try {
                await ApiService.removeCategory(categoryID);
                setCategories(categories.filter(category => category.id !== categoryID));
                setSuccessMessage("Categoria rimossa con successo!");
                setTimeout(() => setSuccessMessage(""), 3000);
            } catch (error) {
                console.error("Error removing category:", error.message);
                setErrorMessage("Si è verificato un errore nella rimozione della categoria.");
            }
        }
    };

    if (loading) {
        return <p className="categories-Loading">Caricamento informazioni...</p>; // Mostra il messaggio di caricamento
    }


    return(

        <div className="category-wrapper">

            <h1>CATEGORIE</h1>
            
            <div className="contenitoreTotale">
        
                <div className="category-section">
                    
                        <div className="left-section">
                            <h2>Categorie Presenti</h2>
                            <div className="categories-list">
                                {categories
                                    .filter(category => category.id !== 1) // Filtra la categoria con id 1
                                    .map((category) => (
                            <div key={category.id} className="Categories-obj">
                                <button 
                                    onClick={() => handleRemoveCategory(category.id, category.type)}>X</button>
                                    <span>{category.type}</span>
                            </div>
                            ))
                            }
                        </div>
                    </div>
                        <div className="right-section">
                            <h2>Nuova Categoria</h2>
                            <form onSubmit={handleNewCategorySubmit} className="new-Category-form">
                                <h2 className="testiD">Aggiungi Nuova Categoria</h2>
                                <input 
                                    type="text" 
                                    value={newCategory} 
                                    onChange={(e) => setNewCategory(e.target.value.toUpperCase())} 
                                    placeholder="Nome nuova categoria"
                                    maxLength="25"
                                    required />
                                <button className="FormbuttonAddCat" type="submit">Aggiungi Categoria</button>
                            </form>

                        </div>
                </div>

                <div className="testiErrore">
                    {successMessage && <p className="success-message">{successMessage}</p>}
                    {errorMessage && <p className="error-message">{errorMessage}</p>}
                </div>

            </div>
        </div>



    );






}

export default ViewCat;