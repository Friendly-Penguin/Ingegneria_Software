import React from 'react';
import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { ProtectedRoute, AdminRoute } from './service/Guard';
import ApiService from './service/ApiService';

//Common
import Navbar from './component/common/Navbar';
import FooterComponent from './component/common/Footer';

//USER SECTION
import HomePage from './component/home/HomePage';
import LoginPage from './component/auth/LoginPage';
import RegisterPage from './component/auth/RegisterPage';
import OpenTicket from './component/home/OpenTicket';
import Profile from './component/profile/Profile';

//ADMIN SECTION
import AdminHome from './component/admin/Home/AdminHome';
  //AGGIUNGI ADMIN
import AddAdmin from './component/admin/NewAdmin/NewAdmin';
  //GESTIONE TICKET
import TicketHome from './component/admin/Ticket/TicketHome';
import TicketUpdate from './component/admin/Ticket/TicketUpdate';
  //GESTIONE DOMANDE
import ViewQuest from './component/admin/Question/QuestionHome';
import QuestionUpdate from './component/admin/Question/QuestionUpdate';
import AddQuestion from './component/admin/Question/AddQuestion';
  //GESTIONE CATEGORIE
import ViewCat from './component/admin/Category/ViewCat';
  //GESTIONE UTENTI
import UserHome from './component/admin/User/UserHome';

  //GESTIONE SCADENZA TOKEN
import TokenExpirationWarning from './Token/TokenExpiration';


function App() {

  const isAuthenticated = ApiService.isAuthenticated();
  const isAdmin = ApiService.isAdmin();
  const location = useLocation();
  return (
    
    <div className="App">
      
      {/* Mostra il TokenExpirationWarning su tutte le pagine tranne /login e /register */}
      {location.pathname !== '/login' && location.pathname !== '/register' && <TokenExpirationWarning />}

      {/* Mostra la Navbar solo se il percorso non è "/login" o "/register" */}
      {location.pathname !== '/login' && location.pathname !== '/register'  && <Navbar />}

      
      <div className="content">
        <Routes>

          {/* Se l'utente non è autenticato, reindirizza alla pagina FAQ */}
          {!isAuthenticated && (
              <Route path="/" element={<Navigate to="/FAQ" />} />
          )}

          {/* Se l'utente è autenticato ma NON è un admin, reindirizza alla pagina FAQ */}
          {isAuthenticated && !isAdmin && (
            <Route path="/" element={<Navigate to="/FAQ" />} />
          )}

          {/* Se l'utente è autenticato ed è un admin, reindirizza alla pagina adminHome */}
          {isAuthenticated && isAdmin && (
            <Route path="/" element={<Navigate to="/adminHome" />} />
          )}
          
          <Route path='/FAQ' element={<HomePage />} />
          <Route path='/login' element={<LoginPage />} />
          <Route path='/register' element={<RegisterPage />} />

          {/* AUTENTICATED USER ROUTES */}
          <Route path='/ticket' element={ <ProtectedRoute element={<OpenTicket/>} /> } />
          <Route path='/profile' element={ <ProtectedRoute element={<Profile/>} /> } />
        
          {/* ADMIN ROUTES */}
          <Route path='/adminHome' element={ <AdminRoute element={<AdminHome/>} /> } />

          <Route path='/addAdmin' element={ <AdminRoute element={<AddAdmin/>} /> } />

          <Route path='/openTicket' element={ <AdminRoute element={<TicketHome/>} /> } />
          <Route path='/ticketUpdate/:id' element={<AdminRoute element={<TicketUpdate/>} /> } />

          <Route path='/questionUpdate/:id' element={ <AdminRoute element={<QuestionUpdate/>} /> } />
          <Route path='/viewQuest' element={ <AdminRoute element={<ViewQuest/>} /> } />
          <Route path='/addQuestion' element={ <AdminRoute element={<AddQuestion/>} /> } />

          <Route path='/viewCat' element={ <AdminRoute element={<ViewCat/>} /> } />
          
          <Route path='/allUser' element={ <AdminRoute element={<UserHome/>} /> } />
          
          </Routes>



      </div>
      
      {/* Mostra il Footer solo se il percorso non è "/login" o "/register" */}
      {location.pathname !== '/login' && location.pathname !== '/register'  && <FooterComponent />}
    
    </div>
    
  );
}

// Il componente App deve essere avvolto da BrowserRouter
export default function AppWrapper() {
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}
