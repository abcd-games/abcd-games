import {AppUser} from "../types/AppUser.ts";
import LoggedIn from "./LoggedIn.tsx";
import {Link} from "react-router-dom";

type HeaderProps = {
    appUser: AppUser | null;
    logout: () => void;
    login: () => void;
}

export default function Header(props: Readonly<HeaderProps>) {
    return (
        <header className="p-3 text-bg-dark">
            <div className="container">
                <div
                    className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
                    <a href="/" className="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                        <i className="fa-solid fa-gamepad"></i>
                    </a>

                    <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                        <li><Link to="/" className="nav-link px-2 text-white disabled">Home</Link></li>
                        <li><a href="/games" className="nav-link px-2 text-white disabled">Games</a></li>
                        <li><a href="/leaderboard" className="nav-link px-2 text-white disabled">Leaderboard</a></li>
                    </ul>

                    <form className="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3" role="search"
                          data-np-autofill-form-type="other" data-np-checked="1" data-np-watching="1">
                        <input type="search" className="form-control form-control-dark text-bg-dark"
                               placeholder="Search..." aria-label="Search"/>
                    </form>

                    {
                        props.appUser ? <LoggedIn appUser={props.appUser} logout={props.logout}/> :
                            (
                                <div className="text-end">
                                    <button type="button" onClick={props.login}
                                            className="btn btn-outline-light me-2">Login
                                    </button>
                                    <button type="button" onClick={props.login}
                                            className="btn btn-warning">Sign-up
                                    </button>
                                </div>
                            )
                    }
                </div>
            </div>
        </header>
    );
}