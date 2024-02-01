import {ReactNode} from "react";
import Header from "./Header.tsx";
import Footer from "./Footer.tsx";

type LayoutProps = {
    children: ReactNode;
}

export default function Layout(props: Readonly<LayoutProps>) {
    return (
        <>
            <Header/>
            <main className="container">
                {props.children}
            </main>
            <Footer/>
        </>
    );
}