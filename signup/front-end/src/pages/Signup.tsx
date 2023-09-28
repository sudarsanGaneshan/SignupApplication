import React, { useState } from "react";
import ReactDOM from "react-dom";
import axios from "axios";

interface ErrorMessage {
    name: string;
    message: string;
}

function Signup() {

    const initialErrorMessages: ErrorMessage = { name: "", message: "" };
    const [errorMessages, setErrorMessages] = useState<ErrorMessage>(initialErrorMessages);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [responseStatus, setResponseStatus] = useState(0);

    const errors = {
        internalError: "Error creting user"
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const form = event.target as HTMLFormElement; // Explicitly cast to HTMLFormElement
        const userName = form.elements.namedItem("userName") as HTMLInputElement;
        const password = form.elements.namedItem("password") as HTMLInputElement;
        const email = form.elements.namedItem("email") as HTMLInputElement;
        const firstName = form.elements.namedItem("firstName") as HTMLInputElement;
        const lastName = form.elements.namedItem("lastName") as HTMLInputElement;


        try {
            const response = await axios.post("/api/v1/signup/create", {
                userName: userName.value,
                password: password.value,
                email: email.value,
                firstName: firstName.value,
                lastName: lastName.value
            });
            const statusCode = response.status
            if (statusCode === 200) {
                setIsSubmitted(true);
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const axiosError = error as import("axios").AxiosError;
                if (axiosError.response) {
                    const statusCode = axiosError.response.status;
                    if (statusCode === 500) {
                        setResponseStatus(statusCode)
                        setErrorMessages({ name: "internalError", message: errors.internalError });
                    }
                }
            }
        }
    };

    const renderErrorMessage = (name: string) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const renderForm = (
        <div className="form">
            <form onSubmit={handleSubmit}>
                <div className="input-container">
                    <label>Username </label>
                    <input type="text" name="userName" required />
                    {renderErrorMessage("userName")}
                </div>
                <div className="input-container">
                    <label>Password </label>
                    <input type="password" name="password" required />
                    {renderErrorMessage("password")}
                </div>
                <div className="input-container">
                    <label>Email </label>
                    <input type="text" name="email" required />
                    {renderErrorMessage("email")}
                </div>
                <div className="input-container">
                    <label>First Name </label>
                    <input type="text" name="firstName" required />
                    {renderErrorMessage("firstName")}
                </div>
                <div className="input-container">
                    <label>Last Name </label>
                    <input type="text" name="lastName" required />
                    {renderErrorMessage("lastName")}
                </div>
                <div className="button-container">
                    <input type="submit" value="Signup"/>
                </div>
            </form>
        </div>
    );

    return (
        <div className="login-form">
            <div className="title">Sign Up</div>
            {(responseStatus === 500) && renderErrorMessage(errorMessages.name)}
            {isSubmitted ? <div>User is successfully Signed Up</div> : renderForm}
        </div>
    );

}

export default Signup;