import {useEffect, useState} from "react";
import {AppUser} from "../types/AppUser.ts";
import axios from "axios";

export default function useAppUser() {
    const [appUser, setAppUser] = useState<AppUser | null | undefined>(undefined);
    const [isLoadingAppUser, setIsLoadingAppUser] = useState<boolean>(true);
    const BASE_URI: string = "/api/auth";

    function fetchMe() {
        setIsLoadingAppUser(true);
        axios.get(`${BASE_URI}/me`)
            .then(response => setAppUser(response.data))
            .catch(error => {
                setAppUser(null);
                console.log(error);
            })
            .finally(() => setIsLoadingAppUser(false));
    }

    function login() {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin

        window.open(host + '/oauth2/authorization/okta', '_self')
    }

    function logout() {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin

        window.open(host + '/logout', '_self')
    }

    useEffect(() => {
        fetchMe();
    }, []);

    return {appUser, loadingAppUser: isLoadingAppUser, login, logout};
}
