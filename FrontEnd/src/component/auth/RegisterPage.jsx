import React, { useState } from "react"; 
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";
import { MdEmail } from "react-icons/md";
import { FaKey } from "react-icons/fa";
import { FaArrowLeft } from "react-icons/fa";
import { FaAddressCard } from "react-icons/fa";

function RegisterPage(){
    
    
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: ''
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [passwordsMatch, setPasswordsMatch] = useState(false);
    const [nameWrote, setNameWrote] = useState(false);
    const [emailWrote, setEmailWrote] = useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
    
        // Se il campo è "name", accetta solo lettere e spazi
        if (name === "name") {
            const regex = /^[A-Za-zÀ-ÖØ-öø-ÿ\s]*$/; // Consenti lettere (anche con accenti) e spazi
            if (regex.test(value)) {
                setFormData({ ...formData, [name]: value });
                setNameWrote(value.trim() !== ''); // Controlla se il campo nome è stato scritto
            }
        } else if (name === "email") {
            setFormData({ ...formData, [name]: value });
            setEmailWrote(value.trim() !== ''); // Controlla se il campo email è stato scritto
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };



    
    const validateForm = () => {
        const { name, email, password } = formData;
        if (!name || !email || !password) {
            return false;
        }
        return true;
    };

    const handleConfirmPasswordChange = (e) => {
        const { value } = e.target;
        if (formData.password !== value) {
            setPasswordsMatch(false);
            setErrorMessage("Le password non coincidono!");
        } else {
            setPasswordsMatch(true);
            setErrorMessage('');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) {
            setErrorMessage("Compila tutti i campi!");
            setTimeout(() => setErrorMessage(''), 5000);
            return;
        }
        try {
            // Call the register method from ApiService
            const response = await ApiService.registerUser(formData);

            // Check if the response is successful
            if (response.statusCode === 200) {
                // Clear the form fields after successful registration
                setFormData({
                    name: '',
                    email: '',
                    password: ''
                });
                setSuccessMessage('Utente registrato correttamente!');
                setTimeout(() => {
                    setSuccessMessage('');
                    navigate('/FAQ');
                }, 3000);
            }
        }
         catch (error) {
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000);
        }
    };

    document.title = "Registrati";
    const isButtonDisabled = nameWrote && emailWrote && passwordsMatch;

    return(


        <div className="login-container">
        <div className="wrapper">
        
        <form onSubmit={handleSubmit}>
             <div className="testataLogin">
             <a href="/FAQ"> <FaArrowLeft className="icon"/></a>
                 <h1 className="H1_title">Registrati</h1>
             </div>
             <div className="input-box">
             <input
                 type="text"
                 name = "name"
                 value={formData.name}
                 onChange={handleInputChange}
                 placeholder="Nome"
                 required
             />
             <FaAddressCard className="icon" />
             </div>
             <div className="input-box">
                <input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="Email"
                    required
                />
                <MdEmail className="icon" />
                </div>
             <div className="input-box">
             <input
                 type="password"
                 name="password"
                 value={formData.password}
                 onChange={handleInputChange}
                 placeholder="Password"
                 required
             />
             <FaKey className="icon" />
             </div>
             <div className="input-box">
             <input
                 type="password"
                 name="confirmPassword"
                 onBlur={handleConfirmPasswordChange}
                 placeholder="Conferma password"
                 required
             />
             <FaKey className="icon" />
             </div>
             {/* Disabilita il bottone se le password non coincidono */}
             <button type="submit" disabled={!isButtonDisabled}>
                        Registrati
                    </button>
         </form>
         
         <div className="error">
            {errorMessage && <p className="error-message">{errorMessage}</p>}
         </div>
         <div className="confirm">
            {successMessage && <p className="success-message">{successMessage}</p>}
         </div>
         <div className="register-link">
             <p>Hai già un account? <a href="/login">Accedi</a></p>
         </div>
         </div>
     </div>

    );
}

export default RegisterPage;


