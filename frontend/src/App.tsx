import Layout from "./components/Layout.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import {Route, Routes} from "react-router-dom";
import RegisterPage from "./pages/RegisterPage.tsx";
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import useAppUser from "./hooks/useAppUser.ts";

export default function App() {
    const {appUser, loadingAppUser, login, logout, register, loginWithGoogle} = useAppUser();

    if (appUser === undefined) {
        return (
            <div className="spinner-border">
                <span className="visually-hidden">Loading...</span>
            </div>
        );
    }

    return (
        <Layout logout={logout} appUser={appUser}>
            <Routes>
                <Route path="/" element={<h1>Hello, ABCD-Games!</h1>}/>
                <Route path="/login"
                       element={<LoginPage loginWithGoogle={loginWithGoogle} appUser={appUser} login={login}
                                           loadingAppUser={loadingAppUser}/>}/>
                <Route path="/register"
                       element={<RegisterPage appUser={appUser} loadingAppUser={loadingAppUser}
                                              registerUser={register}/>}/>
            </Routes>
            <ToastContainer
                position="top-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="dark"
            />
        </Layout>
    )
}
