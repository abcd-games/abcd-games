import {ReactNode} from "react";
import Header from "./Header.tsx";
import Footer from "./Footer.tsx";
import {AppUser} from "../types/AppUser.ts";

type LayoutProps = {
    children: ReactNode;
    appUser: AppUser | null;
}

export default function Layout(props: Readonly<LayoutProps>) {
    return (
        <>
            <Header appUser={props.appUser}/>
            <main className="container">
                {props.children}
            </main>
            <Footer/>
        </>
    );
}