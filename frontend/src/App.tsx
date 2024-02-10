import Layout from "./components/Layout.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import {Route, Routes} from "react-router-dom";
import RegisterPage from "./pages/RegisterPage.tsx";
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import useAppUser from "./hooks/useAppUser.ts";
import BattleshipsPage from "./pages/BattleshipsPage.tsx";

export default function App() {
    const {appUser, loadingAppUser, login, logout, register} = useAppUser();

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
                       element={<LoginPage appUser={appUser} login={login} loadingAppUser={loadingAppUser}/>}/>
                <Route path="/register"
                       element={<RegisterPage appUser={appUser} loadingAppUser={loadingAppUser}
                                              registerUser={register}/>}/>
                <Route path="/games">
                    <Route path="battleships/*" element={<BattleshipsPage/>}/>
                </Route>
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
