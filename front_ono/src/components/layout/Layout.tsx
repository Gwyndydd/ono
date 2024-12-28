import { FunctionComponent } from "react";
import { Outlet } from "react-router-dom";

import NavBar from "./navbar/NavBar";

import "./Layout.css";

export type LayoutProps = {
  displayNavBar?: boolean;
}

const Layout: FunctionComponent<LayoutProps> = ({ displayNavBar = false }) => {

  return (
    <div id="layout" className="layout">
        {displayNavBar && <NavBar />}
        <main className="page-main">
            <Outlet />
        </main>
    </div>
  );
};

export default Layout;