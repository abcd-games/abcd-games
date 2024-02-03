import Layout from "./components/Layout.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import {Route, Routes} from "react-router-dom";
import RegisterPage from "./pages/RegisterPage.tsx";
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import useAppUser from "./hooks/useAppUser.ts";

export default function App() {
    const {appUser, loadingAppUser, login} = useAppUser();

    if (appUser === undefined) {
        return (
            <div className="spinner-border">
                <span className="visually-hidden">Loading...</span>
            </div>
        );
    }

    return (
        <Layout appUser={appUser}>
            <Routes>
                <Route path="/" element={<h1>Hello, ABCD-Games!</h1>}/>
                <Route path="/login" element={<LoginPage login={login} loadingAppUser={loadingAppUser}/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
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
