import {useEffect, useState} from "react";
import {AppUser, AppUserRequest} from "../types/AppUser.ts";
import axios from "axios";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";

export default function useAppUser() {
    const [appUser, setAppUser] = useState<AppUser | null | undefined>(undefined);
    const [isLoadingAppUser, setIsLoadingAppUser] = useState<boolean>(true);
    const BASE_URI: string = "/api/users";
    const navigate = useNavigate();

    function fetchMe() {
        axios.get(`${BASE_URI}/me`)
            .then(response => setAppUser(response.data))
            .catch(error => {
                setAppUser(null);
                console.log(error);
            });
    }

    function login(username: string, password: string) {
        setIsLoadingAppUser(true);
        axios.post(`${BASE_URI}/login`, {}, {
            auth: {
                username: username,
                password: password
            }
        })
            .then(response => {
                setAppUser(response.data);
                navigate("/");
                toast.success("Welcome back, " + response.data.username + "!");
            })
            .catch(error => toast.error(error.response.data.message))
            .finally(() => setIsLoadingAppUser(false));
    }

    function loginWithGoogle() {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin

        window.open(host + '/oauth2/authorization/google', '_self')
    }

    function logout() {
        setIsLoadingAppUser(true);
        axios.post(`${BASE_URI}/logout`)
            .then(() => {
                setAppUser(null);
                navigate("/login");
                toast.success("You have been logged out!");
            })
            .catch(error => toast.error(error.response.data.message))
            .finally(() => setIsLoadingAppUser(false));
    }

    async function register(appUserRequest: AppUserRequest) {
        setIsLoadingAppUser(true);
        return axios.post(`${BASE_URI}`, appUserRequest)
            .then(response => {
                toast.success("Welcome, " + response.data.username + "! You can now login.");
                navigate("/login");
            })
            .catch(error => {
                toast.error(error.response.data.message);
                throw error;
            })
            .finally(() => setIsLoadingAppUser(false));
    }

    useEffect(() => {
        fetchMe();
        return () => {
            setInterval(fetchMe, 30000);
        }
    }, []);

    return {appUser, loadingAppUser: isLoadingAppUser, login, register, logout, loginWithGoogle};
}
