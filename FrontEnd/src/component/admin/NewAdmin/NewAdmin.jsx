import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from "../../../service/ApiService";
import { MdEmail } from "react-icons/md";
import { FaKey } from "react-icons/fa";
import { FaAddressCard } from "react-icons/fa";
import './NewAdmin.css';

function NewAdmin(){
    document.title = "Aggiungi Admin";
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        role: 'ADMIN'
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [passwordsMatch, setPasswordsMatch] = useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
    
        // Se il campo è "name", accetta solo lettere e spazi
        if (name === "name") {
            const regex = /^[A-Za-zÀ-ÖØ-öø-ÿ\s]*$/; // Consenti lettere (anche con accenti) e spazi
            if (regex.test(value)) {
                setFormData({ ...formData, [name]: value });
            }
        } else {
            // Per altri campi, imposta il valore normalmente
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
        const conferma = window.confirm("Sei sicuro di voler procedere?");
        if(conferma){
            try {
                // Call the register method from ApiService
                const response = await ApiService.registerUser(formData);

                // Check if the response is successful
                if (response.statusCode === 200) {
                    // Clear the form fields after successful registration
                    setFormData({
                        name: '',
                        email: '',
                        password: '',
                        role: ''
                    });
                    setSuccessMessage('ADMIN aggiunto correttamente!');
                    setTimeout(() => {
                        setSuccessMessage('');
                        navigate('/');
                    }, 3000);
                }
            }
            catch (error) {
                setErrorMessage(error.response?.data?.message || error.message);
                setTimeout(() => setErrorMessage(''), 5000);
            }
        }else{
            console.log("Azione annullata dall'utente.");
        }

    };




    return(


        <div className="login-container-admin">
            <div className="testataAD">
                 <h1 className="titolo">Nuovo ADMIN</h1>
             </div>
        <div className="wrapper-admin">
        
        <form onSubmit={handleSubmit}>
             
             <div className="input-box-admin">
             <input
                 type="text"
                 name = "name"
                 value={formData.name}
                 onChange={handleInputChange}
                 placeholder="Nome"
                 required
             />
             <FaAddressCard className="icon-admin" />
             </div>
             <div className="input-box-admin">
                <input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="Email"
                    required
                />
                <MdEmail className="icon-admin" />
                </div>
             <div className="input-box-admin">
             <input
                 type="password"
                 name="password"
                 value={formData.password}
                 onChange={handleInputChange}
                 placeholder="Password"
                 required
             />
             <FaKey className="icon-admin" />
             </div>
             <div className="input-box-admin">
             <input
                 type="password"
                 name="confirmPassword"
                 onBlur={handleConfirmPasswordChange}
                 placeholder="Conferma password"
                 required
             />
             <FaKey className="icon-admin" />
             </div>
             {/* Disabilita il bottone se le password non coincidono */}
             <button type="submit" disabled={!passwordsMatch}>
                    Conferma
                    </button>
         </form>
         
         <div className="error">
            {errorMessage && <p className="error-message">{errorMessage}</p>}
         </div>
         <div className="confirm">
            {successMessage && <p className="success-message">{successMessage}</p>}
         </div>
         </div>
     </div>

    );
}

export default NewAdmin;


