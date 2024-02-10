import {AppUser} from "../types/AppUser.ts";

type MyProfilePageProps = {
    appUser: AppUser;
}
export default function MyProfilePage(props: Readonly<MyProfilePageProps>) {
    return (
        <div className="container">
            <h1>My Profile</h1>
            <p>Username: {props.appUser.username}</p>
            <p>Email: {props.appUser.email}</p>
            <img src={props.appUser.avatarUrl} className="img-thumbnail" alt={props.appUser.username + " avatar"}/>
        </div>
    );
}