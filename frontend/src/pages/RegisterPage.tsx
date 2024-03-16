import {AppUser, AppUserRequest} from "../types/AppUser.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import {Navigate} from "react-router-dom";
import {
    email_validation,
    checkFieldValid,
    name_validation,
    password_validation,
} from "../helper/Validation-helper.ts";
import {FormInput} from "../components/FormInput.tsx";

type RegisterPageProps = {
    registerUser: (appUserRequest: AppUserRequest) => Promise<void>,
    loadingAppUser: boolean,
    appUser: AppUser | null,
}

export const defaultUserState = {
    username: "",
    email: "",
    password: "",
}

export default function RegisterPage(props: Readonly<RegisterPageProps>) {

    const [appUserRequest, setAppUserRequest] = useState<AppUserRequest>(defaultUserState)
    const [formIsValid,setFormIsValid] = useState(false)

    if (props.appUser) {
        return <Navigate to={"/"}/>
    }

    const handleFormInputChange = (event: ChangeEvent<HTMLInputElement>)=> {
        const {name, value} = event.target;
        const newAppUserState = {...appUserRequest,[name] : value}
        setAppUserRequest(newAppUserState);
        checkFormValid(newAppUserState)
    }

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if(formIsValid) {
            props.registerUser(appUserRequest)
                .then(() => {
                    setAppUserRequest({
                        username: "",
                        email: "",
                        password: "",
                    });
                });
        }
    }

    const checkFormValid = (appUserState : AppUserRequest) => {
        setFormIsValid(
            checkFieldValid(appUserState.email,email_validation) &&
            checkFieldValid(appUserState.username,name_validation) &&
            checkFieldValid(appUserState.password,password_validation)
        );
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1 className="h3 mb-3 fw-normal">Please register to play games</h1>
            <div className="form-floating">
                <FormInput
                    type="text"
                    className="form-control my-2"
                    id="floatingUsername"
                    placeholder="Username"
                    name="username"
                    label="Username"
                    validation={name_validation}
                    setFormValue={handleFormInputChange}
                ></FormInput>
            </div>
            <div className="form-floating">
                <FormInput
                    type="email"
                    className="form-control my-2"
                    id="floatingEmail"
                    placeholder="name@example.com"
                    name="email"
                    label="Email Adress"
                    validation={email_validation}
                    setFormValue={handleFormInputChange}/>
            </div>
            <div className="form-floating">
                <FormInput
                    type="pawword"
                    className="form-control my-2"
                    id="floatingPassword"
                    placeholder="Password"
                    name="password"
                    label="Password"
                    validation={password_validation}
                    setFormValue={handleFormInputChange}/>
            </div>
            <button className="btn btn-primary w-100 py-2 my-2" type="submit">Register
                now
            </button>
        </form>

    );
}
