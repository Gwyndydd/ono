import { FunctionComponent, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { RootState } from "../../store/redux.store";
import { setStudyPrograms } from "../../store/slices/study-program.slice";
import { useGetStudyProgramsPublicQuery } from "../../store/api/studyprogram.api";

import "./HomePage.css";
import "../../styles/stylegeneral.css"

/**
 * Home page => acceuil 
 * Items needs :
 *      Navbar, lien de connexion
 *      Affichage Programm study le plus récent
 *      Liste de voca la plus récente
 *      Liste de grammair la plus récente
 *      
 */

const HomePage: FunctionComponent = () => {

    const dispatch = useDispatch();
    const { data, isLoading, error } = useGetStudyProgramsPublicQuery(); // Récupère les programmes publics depuis l'API
    const studyPrograms = useSelector((state: RootState) => state.studyProgram.studyPrograms); // Sélectionne les programmes depuis le store

    // Utilisation de useEffect pour stocker les programmes récupérés dans le store
    useEffect(() => {
        if (data) {
            dispatch(setStudyPrograms(data)); // Sauvegarde les programmes dans le store
        }
    }, [data, dispatch]); // Le dispatch est exécuté à chaque fois que `data` change

    // Affiche un message de chargement si les données sont en cours de récupération
    //if (isLoading) return <div>Loading...</div>;

    // Affiche un message d'erreur si la récupération des données échoue
    /*if (error){
        const errorMessage = (error as { status: number; data: unknown }).data
        ? (error as { data: string }).data
        : "Une erreur s'est produite. Veuillez réessayer.";
        return <div>Error: {errorMessage}</div>;
    };*/

    const navigate = useNavigate();

    const handleClick = () => {
        navigate("/demo/123", { replace: false, state: { from: "HomePage" } });
    };

    return (
        <div className="home-page">
            <h1>Bienvenue sur ONO</h1>
            <div className="liste-study-programm">

                {error ? (
                    <div>Une erreur s'est produite. Veuillez réessayer.</div>
                ) : (
                    <>
                        {isLoading ? (<div>Chargement des programmes...</div>
                        ):(
                            <div className="study-programs-list">
                            {studyPrograms && studyPrograms.length > 0 ? (
                                studyPrograms.map((program) => (
                                <div className="study-program-card" key={program.id}>
                                    <h2 className="program-title">{program.name}</h2>
                                    <p className="program-description">{program.description}</p>
                                </div>
                                ))
                            ) : (
                                <div>Aucun programme d'étude n'est disponible pour le moment</div>
                            )}
                        </div>
                        )}
                    </>
                
                )}
            </div>

        </div>
    );
};

export default HomePage;