import {AppUser, AppUserRequest} from "../types/AppUser.ts";
import {ChangeEvent, FormEvent, useState} from "react";
import {Navigate} from "react-router-dom";

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

    if (props.appUser) {
        return <Navigate to={"/"}/>
    }

    function handleChange(event: ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        setAppUserRequest(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        props.registerUser(appUserRequest)
            .then(() => {
                setAppUserRequest({
                    username: "",
                    email: "",
                    password: "",
                });
            });
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1 className="h3 mb-3 fw-normal">Please register to play games</h1>

            <div className="form-floating">
                <input
                    type="text"
                    minLength={3}
                    maxLength={20}
                    className="form-control"
                    id="floatingInput"
                    placeholder="Username"
                    name="username"
                    value={appUserRequest.username}
                    onChange={handleChange}
                />
                <label htmlFor="floatingInput">Username</label>
            </div>
            <div className="form-floating">
                <input
                    type="email"
                    className="form-control my-2"
                    id="floatingInput"
                    placeholder="name@example.com"
                    name="email"
                    value={appUserRequest.email}
                    onChange={handleChange}
                />
                <label htmlFor="floatingInput">Email address</label>
            </div>
            <div className="form-floating">
                <input
                    type="password"
                    className="form-control my-2"
                    id="floatingPassword"
                    placeholder="Password"
                    name="password"
                    value={appUserRequest.password}
                    onChange={handleChange}
                />
                <label htmlFor="floatingPassword">Password</label>
            </div>
            <button className="btn btn-primary w-100 py-2 my-2" type="submit" disabled={props.loadingAppUser}>Register
                now
            </button>
        </form>
    );
}
