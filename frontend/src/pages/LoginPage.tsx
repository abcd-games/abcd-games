import {ChangeEvent, FormEvent, useState} from "react";
import {AppUser} from "../types/AppUser.ts";
import {Navigate} from "react-router-dom";

type LoginPageProps = {
    login: (username: string, password: string) => void;
    loadingAppUser: boolean;
    appUser: AppUser | null;
};

export default function LoginPage(props: Readonly<LoginPageProps>) {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    if (props.appUser) {
        return <Navigate to={"/"}/>
    }

    function handleChange(event: ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        if (name === "username") setUsername(value);
        if (name === "password") setPassword(value);
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        props.login(username, password);
        setPassword("");
        setUsername("");
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1 className="h3 mb-3 fw-normal">Please sign in</h1>

            <div className="form-floating">
                <input
                    type="text"
                    className="form-control"
                    id="floatingInput"
                    autoComplete="username"
                    name="username"
                    value={username}
                    onChange={handleChange}
                />
                <label htmlFor="floatingInput">Email address or Username</label>
            </div>
            <div className="form-floating">
                <input
                    type="password"
                    className="form-control my-2"
                    id="floatingPassword"
                    autoComplete="current-password"
                    name="password"
                    value={password}
                    onChange={handleChange}
                />
                <label htmlFor="floatingPassword">Password</label>
            </div>
            <button className="btn btn-primary w-100 py-2" type="submit" disabled={props.loadingAppUser}>Sign in
            </button>
        </form>
    );
}