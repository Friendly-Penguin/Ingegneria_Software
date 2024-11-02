import axios from "axios"


export default class ApiService{

    //static BASE_URL = "http://192.168.178.20:8080"

    static getHeader(){
        const token = localStorage.getItem("token");
        return{
            Authorization : `Bearer ${token}`,
            "Content-Type": "application/json"
        };
    }



/*AUTH*/

    /* This is used to register a new user*/
    static async registerUser(registration){
        const response = await axios.post('/auth/register', registration)
        return response.data
    }

    /* This is used to login a registered user */
    static async loginUser(loginDetails){
        const response = await axios.post(`/auth/login`, loginDetails)
        return response.data
    }

/* USER */

    /* This is used to retrive all the USER PROFILE */
    static async getAllUser(){
        const response = await axios.get(`/user/all`, {headers: this.getHeader()})
        return response.data
    }

    /* This is used to retrive a USER PROFILE (logged-in)*/
    static async getUser(userID){
        const response = await axios.get(`/user/get-by-id/${userID}`)
        return response.data
    }

    /* This is used to delete a USER profile */
    static async deleteUser(userID){
        const response = await axios.get(`/user/delete/${userID}`,{headers: this.getHeader()})
        return response.data
    }


   

/* QUESTION */

    /* This is used to update a QUESTION */
    static async updateQuestion( questionID, formData ){
        const result = await axios.post(`/question/update/${questionID}`, formData, {
            headers: {
                ...this.getHeader(),
                'Content-Type': 'multipart/form-data'
            }
        });
        return result.data
    }

    /* This is used to add a QUESTION*/
    static async addQuestion(formData){
        const result = await axios.post(`/question/add`, formData, {
            headers: {
                ...this.getHeader(),
                'Content-Type': 'multipart/form-data'
            }
        });
        return result.data
    }

    /* This is used to retrive all the QUESTION */
    static async getAllQuestion(){
        const response = await axios.get(`/question/all`)
        return response.data
    }

    /* This is used to delete a QUESTION */
    static async deleteQuestion(questionID){
        const response = await axios.get(`/question/delete/${questionID}`,{headers: this.getHeader()})
        return response.data
    }

    /* This is used to retrive a QUESTION by is its ID */
    static async getQuestionByID(questionID){
        const result = await axios.get(`/question/retrive-${questionID}`, {headers: this.getHeader()})
        return result.data
    }


/* TICKET */

    /* This is used by a user to add a new TICKET */
    static async openTicket(formData){
        const result = await axios.post(`/ticket/open`, formData);
        return result.data
    }
   
    /* This is used to update a TICKET */
    static async updateTicket(id,formData){
        const response = await axios.post(`/ticket/update/${id}`, formData, {
            headers: {
                ...this.getHeader(),
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data
    }

    /* This is used to retrive all the TICKET with an answer */
    static async getAllYTicket(){
        const response = await axios.post(`/ticket/getAllAnswered`,null, {headers: this.getHeader()})
        return response.data
    }

    /* This is used to retrive all the TICKET without an answer */
    static async getAllNTicket(){
        const response = await axios.post(`/ticket/getAllNotAnswered`,null, {headers: this.getHeader()})
        return response.data
    }

     /* This is used to retrive all the TICKET of a USER */
     static async getAllUserTicket(userID){
        const response = await axios.get(`/ticket/getUser/${userID}`)
        return response.data
    }

    /* This is used to retrive a specific TICKET */
    static async retriveTicket(ticketID){
        const response = await axios.get(`/ticket/get/${ticketID}`, {headers: this.getHeader()} )
        return response.data
    }


/* CATEGORY SECTION */

    /* This is used to retrive all the category */
    static async getAllCategories(){
        const result = await axios.get(`/category/all`)
        return result.data
    }

    /* This is used to add a new CATEGORY */
    static async addCategory(categoryType){
        const response = await axios.post(`/category/add`, {categoryType}, {headers: this.getHeader()})
        return response.data
    }

    /* This is used to remove a CATEGORY */
    static async removeCategory(categoryID){
        const response = await axios.post(`/category/delete/${categoryID}`,null,{headers: this.getHeader()})
        return response.data
    }


/**AUTHENTICATION CHECKER */

    static logout() {
        localStorage.removeItem('token')
        localStorage.removeItem('role')
        localStorage.removeItem('userID')
    }

    static isAuthenticated() {
        const token = localStorage.getItem('token')
        return !!token
    }

    static isAdmin() {
        const role = localStorage.getItem('role')
        return role === 'ADMIN'
    }

    static isUser() {
        const role = localStorage.getItem('role')
        return role === 'USER'
    }



}