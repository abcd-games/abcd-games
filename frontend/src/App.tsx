import Layout from "./components/Layout.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import {Route, Routes} from "react-router-dom";
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import useAppUser from "./hooks/useAppUser.ts";
import MyProfilePage from "./pages/MyProfilePage.tsx";

export default function App() {
    const {appUser, loadingAppUser, login, logout} = useAppUser();

    if (appUser === undefined) {
        return (
            <div className="spinner-border">
                <span className="visually-hidden">Loading...</span>
            </div>
        );
    }

    return (
        <Layout login={login} logout={logout} appUser={appUser}>
            <Routes>
                <Route path="/" element={<h1>Hello, ABCD-Games!</h1>}/>
                <Route path="/login"
                       element={<LoginPage appUser={appUser} login={login} loadingAppUser={loadingAppUser}/>}/>
                {
                    appUser &&
                    <Route path="/my-profile" element={<MyProfilePage appUser={appUser}/>}/>
                }
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
