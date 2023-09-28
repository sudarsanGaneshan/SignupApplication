import React, { useState } from "react";
import ReactDOM from "react-dom";
import axios from "axios";

import "./styles.css";

interface ErrorMessage {
    name: string;
    message: string;
}

function Login() {

    const initialErrorMessages: ErrorMessage = { name: "", message: "" };
    const [errorMessages, setErrorMessages] = useState<ErrorMessage>(initialErrorMessages);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [responseStatus, setResponseStatus] = useState(0);

    const errors = {
        uname: "invalid username",
        pass: "invalid password",
        unauthorized: "Either password is incorrect or User is restricted. Enter correct password or contact admin",
        notFound: "User not Found"
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const form = event.currentTarget;
        const unameInput = form.elements.namedItem("uname") as HTMLInputElement;
        const passInput = form.elements.namedItem("pass") as HTMLInputElement;

        try {
            const response = await axios.post("/api/v1/signup/login?userName=" + unameInput.value + "&password=" + passInput.value);
            const statusCode = response.status
            if (statusCode === 200) {
                setResponseStatus(statusCode)
                setIsSubmitted(true);
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const axiosError = error as import("axios").AxiosError;
                if (axiosError.response) {
                    const statusCode = axiosError.response.status;
                    if (statusCode === 401) {
                        setResponseStatus(statusCode)
                        setErrorMessages({ name: "unauthorized", message: errors.unauthorized });
                    } else if (statusCode === 404) {
                        setResponseStatus(statusCode)
                        setErrorMessages({ name: "notFound", message: errors.notFound });
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
                    <input type="text" name="uname" required />
                    {renderErrorMessage("uname")}
                </div>
                <div className="input-container">
                    <label>Password </label>
                    <input type="password" name="pass" required />
                    {renderErrorMessage("pass")}
                </div>
                <div className="button-container">
                    <input type="submit" value="Login"/>
                </div>
            </form>
        </div>
    );

    return (
        <div className="login-form">
            <div className="title">Sign In</div>
            {(responseStatus === 401 || responseStatus === 404) && renderErrorMessage(errorMessages.name)}
            {isSubmitted ? <div>User is successfully logged in</div> : renderForm}
        </div>
    );
}

export default Login;