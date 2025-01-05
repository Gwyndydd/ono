import { FunctionComponent, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { RootState } from "../../store/redux.store";
import { setStudyPrograms } from "../../store/slices/study-program.slice";
import { useGetStudyProgramsPublicQuery } from "../../store/api/studyprogram.api";

import "./HomePage.css";
import "../../styles/stylegeneral.css"
import { useGetVocabularyListsPublicQuery } from "../../store/api/vocabularyList.api";
import { setVocabularyLists } from "../../store/slices/vocabulary-list.slice";

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

    const { data: studyProgramsData, isLoading: isLoadingPrograms, error: errorPrograms } = useGetStudyProgramsPublicQuery();
    const { data: vocabularyListsData, isLoading: isLoadingVocabulary, error: errorVocabulary } = useGetVocabularyListsPublicQuery();

    const studyPrograms = useSelector((state: RootState) => state.studyProgram.studyPrograms);
    const vocabularyLists = useSelector((state: RootState) => state.vocabularyList.vocabularyLists);


    // Utilisation de useEffect pour stocker les programmes récupérés dans le store
    useEffect(() => {
        if (studyProgramsData) {
          dispatch(setStudyPrograms(studyProgramsData));
        }
        if (vocabularyListsData) {
          dispatch(setVocabularyLists(vocabularyListsData));
        }
      }, [studyProgramsData, vocabularyListsData, dispatch]); // Le dispatch est exécuté à chaque fois que `data` change

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
            <div className="listes">
            <div>
            <h2 className="title-liste">Programme d'étude</h2>
                <div className="liste-elements">
                {errorPrograms ? (
                    <div>Une erreur s'est produite. Veuillez réessayer.</div>
                ) : (
                    <>
                        {isLoadingPrograms ? (<div>Chargement des programmes...</div>
                        ):(
                            <div className="card-list">
                            {studyPrograms && studyPrograms.length > 0 ? (
                                studyPrograms.map((program) => (
                                <div className="card" key={program.id}>
                                    <h3 className="card-title">{program.name}</h3>
                                    <p className="card-description">{program.description}</p>
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
            
            <div>
                <h2 className="title-liste">Liste de vocabulaire</h2>
                <div className="liste-elements">
                    
                    {errorVocabulary ? (
                        <div>Une erreur s'est produite. Veuillez réessayer.</div>
                    ) : (
                        <>
                            {isLoadingVocabulary ? (<div>Chargement des listes de vocabulaire...</div>
                            ):(
                                <div className="card-list">
                                {vocabularyLists && vocabularyLists.length > 0 ? (
                                    vocabularyLists.map((vocaList) => (
                                    <div className="card" key={vocaList.id}>
                                        <h3 className="card-title">{vocaList.name}</h3>
                                        <p className="card-description">Langue etudié : {vocaList.langeEtudie}</p>
                                        <p className="card-description">Langue de définition : {vocaList.langueDefinition}</p>
                                    </div>
                                    ))
                                ) : (
                                    <div>Aucune liste de vocabulaire n'est disponible pour le moment</div>
                                )}
                            </div>
                            )}
                        </>

                    )}
                </div>
            </div>
                
            </div>
            

        </div>
    );
};

export default HomePage;