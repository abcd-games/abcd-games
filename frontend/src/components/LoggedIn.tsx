type LoggedInProps = {
    logout: () => void,
}
export default function LoggedIn(props: Readonly<LoggedInProps>) {
    return (
        <div className="dropdown text-end">
            <a href="/" className="d-block link-body-emphasis text-decoration-none dropdown-toggle"
               data-bs-toggle="dropdown" aria-expanded="false">
                <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32" className="rounded-circle"/>
            </a>
            <ul className="dropdown-menu text-small">
                <li><a href="/" className="dropdown-item">Profile</a></li>
                <li>
                    <hr className="dropdown-divider"/>
                </li>
                <li>
                    <button onClick={props.logout} className="dropdown-item">Sign out</button>
                </li>
            </ul>
        </div>
    );
}
