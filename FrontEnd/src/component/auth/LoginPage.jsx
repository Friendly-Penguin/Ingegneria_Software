import React, { useState } from "react"; 
import { useNavigate, useLocation } from "react-router-dom";
import ApiService from "../../service/ApiService";
import { MdEmail } from "react-icons/md";
import { FaKey } from "react-icons/fa";
import { FaArrowLeft } from "react-icons/fa";
import "./Login_Register.css";

function LoginPage(){
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const location = useLocation();

    const fromUser = location.state?.from?.pathname || '/FAQ';
    const fromAdmin = '/AdminHome';

    const handleSubmit = async(e) => {
        e.preventDefault();

        if(!email || !password){
            setError("Compila tutti i campi!");
            setTimeout(() => setError(''), 5000);
            return;
        }


        try{

            const response = await ApiService.loginUser({email,password});
            if(response.statusCode === 200){
                localStorage.setItem('token', response.token);
                localStorage.setItem('role', response.role);
                localStorage.setItem('userID', response.userID);
                
                if(response.role === 'USER'){
                    navigate(fromUser, { replace: true });
                }
                else
                    navigate(fromAdmin, { replace: true });
            }

        }catch(error){
            setError(error.response?.data?.message || error.message);
            setTimeout(() => setError(''), 5000);
        }
    };



    return(

        <div className="login-container">
           <div className="wrapper">
                <div className="testataLogin">
                    <a href="/FAQ"> <FaArrowLeft /></a>
                        <h1>Accedi</h1>
                </div>
                
                <form onSubmit={handleSubmit}>
                    <div className="input-box">
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="Email"
                            required/>
                        <MdEmail className="icon" />
                        
                    </div>
                    <div className="input-box">
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Password"
                            required/>
                        <FaKey className="icon" />
                    </div>

                        <button type="submit">Login</button>
                </form>
            
            <div className="error">
                {error && <p className="error-message">{error}</p>}
            </div>
            <div className="register-link">
                <p>Non hai un account? <a href="/register">Registrati</a></p>
            </div>
            </div>
        </div>
    );
}

export default LoginPage;