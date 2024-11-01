import React, {useState, useEffect} from "react";
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { TiPencil } from "react-icons/ti";
import { RxCross2 } from "react-icons/rx";
import ApiService from "../../../service/ApiService";
import { FaPlus } from "react-icons/fa6";
import './QuestionHome.css';



function ViewQuest(){

    const [questions, setQuestions] = useState([]); // Stato per le domande e categorie
    const [expandedCategory, setExpandedCategory] = useState(null); // Stato per gestire l'espansione dei menu a tendina
    const [loading, setLoading] = useState(true);

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

            // Chiamata API per ottenere tutte le domande con risposta
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
        
        }finally {
            setLoading(false); // Imposta il caricamento a false quando i dati sono stati recuperati o si è verificato un errore
        }
    };

    useEffect(() => {
        fetchQuestionsAndCategories();
    }, []);

    // Funzione per gestire l'espansione delle categorie
    const toggleCategory = (category) => {
        setExpandedCategory(expandedCategory === category ? null : category); // Espande/chiude il menu a tendina
    };

    // Logica per eliminare l'elemento
    const handleDelete = async(id,event) => {
        event.preventDefault(); // Evita il comportamento di navigazione predefinito di <a>
        
        const confirmDelete = window.confirm("Sei sicuro di voler eliminare questa domanda?");
        try{

            const response = await ApiService.deleteQuestion(id);

            if(confirmDelete){
            
            }
            if(response.message === "Success"){
                
                window.alert("Domanda eliminata correttamente!")
                fetchQuestionsAndCategories();
            
            }else{
                console.log("Errore nella cancellazione!")
            }
        
        }catch(error){
            console.error("Error updating question:", error.message);
        }
    };

    const handleClick = async (e) => {
        e.preventDefault();
    
        navigate('/addQuestion');
    
    }

    if (loading) {
        return <p className="categories-Loading">Caricamento informazioni...</p>; // Mostra il messaggio di caricamento
    }


    return(

        <div className="all-wrapper"> 
            <h1 className="titolo">Domande</h1>
            <button
                    className="bottoneTestata"
                    onClick={handleClick}>
                    <FaPlus />
                    Nuova domanda
                </button>   
            {Object.values(questions).some((categoryQuestions) => categoryQuestions.length > 0) ? (
                <div className="container">
                    <h1 className="categories-Title">Categorie</h1>
                    <div className="category-container">
                        {Object.keys(questions).map((category) => (
                            <div
                                key={category}
                                onClick={() => toggleCategory(category)}
                                className={`categories-obj ${expandedCategory === category ? 'active' : ''}`}
                            >
                                <span>{category}</span>
                            </div>
                        ))}
                    </div>

                    {expandedCategory && (
                        <div className="questions-wrapper-admin">
                            <h2 style={{ marginBottom: '10px' }}>Domande per la categoria: {expandedCategory}</h2>
                            
                            {questions[expandedCategory].map((question) => (
                            <div className="question-div" key={question.id} style={{ marginBottom: '20px' }}>
                                <div className="question-div-info">
                                    <div className="questionInfo">
                                        <h2>• {question.title}</h2>
                                        <p> <b>Risposta:</b> {question.content}</p>
                                    </div>
                                    <div className="iconDiv">
                                        <Link className="Link Update" to={`/questionUpdate/${question.id}`}>
                                            <TiPencil className="icon-Update" />Aggiorna
                                        </Link>
                                        <button href="#" className="buttonDelete" onClick={(event) => handleDelete(question.id, event)}>
                                            <RxCross2 className="icon-Delete"/>
                                            <h1>Elimina</h1>
                                        </button>
                                    </div>
                                </div>
                            <hr style={{ border: '1px solid #ccc', margin: '10px 0' }} />
                            </div>
                            ))}
                        </div>
                    )}

                </div>

            ) : (
            
                <h1 className="categories-Title">Nessuna Domanda Presente</h1>
            )}
            
            </div>

    );







}

export default ViewQuest;