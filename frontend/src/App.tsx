import Layout from "./components/Layout.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import {Route, Routes} from "react-router-dom";
import RegisterPage from "./pages/RegisterPage.tsx";

export default function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<h1>Hello, ABCD-Games!</h1>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
            </Routes>
        </Layout>
    )
}
