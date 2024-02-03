import {useEffect, useState} from "react";
import {AppUser, AppUserRequest} from "../types/AppUser.ts";
import axios from "axios";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";

export default function useAppUser() {
    const [appUser, setAppUser] = useState<AppUser | null | undefined>(undefined);
    const [loadingAppUser, setLoadingAppUser] = useState<boolean>(true);
    const BASE_URI: string = "/api/users";
    const navigate = useNavigate();

    function fetchMe() {
        setLoadingAppUser(true);
        axios.get(`${BASE_URI}/me`)
            .then(response => setAppUser(response.data))
            .catch(error => {
                setAppUser(null);
                console.log(error);
            })
            .finally(() => setLoadingAppUser(false));
    }

    function login(username: string, password: string) {
        setLoadingAppUser(true);
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
            .finally(() => setLoadingAppUser(false));
    }

    function register(appUserRequest: AppUserRequest) {
        setLoadingAppUser(true);
        axios.post(`${BASE_URI}`, appUserRequest)
            .then(response => {
                setAppUser(response.data);
                toast.success("Welcome, " + response.data.username + "!");
            })
            .catch(error => toast.error(error.response.data.message))
            .finally(() => setLoadingAppUser(false));
    }

    useEffect(() => {
        fetchMe();
    }, []);

    return {appUser, loadingAppUser, login, register};
}