import { FunctionComponent } from "react";
import { Link } from "react-router-dom";

import './NotFoundPage.css'

const NotFoundPage: FunctionComponent = () => {

    return (
            <div className="not-found-page">
                <h1>404 - Not Found</h1>
                <br/>
                <Link to={"/"}>Retour à l'accueil</Link>
            </div>
    );
}

export default NotFoundPage;