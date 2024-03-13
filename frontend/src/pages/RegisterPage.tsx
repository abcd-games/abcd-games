import {AppUser, AppUserRequest} from "../types/AppUser.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import {Navigate} from "react-router-dom";
import {email_validation, isValid, name_validation, password_validation} from "../helper/Validation-helper.ts";
import {FormInput} from "../components/FormInput.tsx";
import ValidationError from "../components/ValidationError.tsx";

type RegisterPageProps = {
    registerUser: (appUserRequest: AppUserRequest) => Promise<void>,
    loadingAppUser: boolean,
    appUser: AppUser | null,
}

export default function RegisterPage(props: Readonly<RegisterPageProps>) {
    const [appUserRequest, setAppUserRequest] = useState<AppUserRequest>({
        username: "",
        email: "",
        password: "",
    });
    const [formInvalid,setFormInvalid] = useState(false)


    if (props.appUser) {
        return <Navigate to={"/"}/>
    }

    function handleFormInputChange(event: ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        setAppUserRequest(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        const emailValid = isValid(appUserRequest.email,email_validation);
        const usernameValid = isValid(appUserRequest.username,name_validation);
        const passwordValid = isValid(appUserRequest.password,password_validation);
        if(emailValid && usernameValid && passwordValid) {
            props.registerUser(appUserRequest)
                .then(() => {
                    setAppUserRequest({
                        username: "",
                        email: "",
                        password: "",
                    });
                });
            setFormInvalid(false);
        }
        else {
            setFormInvalid(true);
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1 className="h3 mb-3 fw-normal">Please register to play games</h1>

            <div className="form-floating">
                <FormInput
                    type="text"
                    className="form-control"
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
            {formInvalid &&
                <ValidationError errorMessage={"Please fill in all fields"}></ValidationError>
            }
        </form>

    );
}
