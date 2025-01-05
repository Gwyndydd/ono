import React, { useEffect } from "react";
import poubelle from "../../assets/poubelle.png"
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/redux.store";
import { useNavigate } from "react-router-dom";
import { useDeleteVocabularyListMutation, useGetVocabularyListsByOwnerQuery } from "../../store/api/vocabularyList.api";
import { setVocabularyLists } from "../../store/slices/vocabulary-list.slice";

import "../../styles/stylegeneral.css"



const UserVocabularyListPage: React.FunctionComponent = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const userId = localStorage.getItem("id"); 
    const { data, isLoading, error, refetch } = useGetVocabularyListsByOwnerQuery(userId as string, {
        refetchOnMountOrArgChange: true, // Force un refetch au montage
    });
    const [deleteVocabularyList] = useDeleteVocabularyListMutation();
    const vocabularyLists = useSelector((state: RootState) => state.vocabularyList.vocabularyLists);

    // Mise à jour des listes de vocabulaires dans le store Redux
    useEffect(() => {
        if (data) {
            dispatch(setVocabularyLists(data));
        }
    }, [data, dispatch]);

    // Nettoyage des listes de vocabulaires lors de la sortie de la page
    useEffect(() => {
        return () => {
            dispatch(setVocabularyLists([])); // Réinitialisation des données
        };
    }, [dispatch]);

    const handleDelete = async (id: string) => {
        try {
            await deleteVocabularyList(id).unwrap();
            dispatch(setVocabularyLists(vocabularyLists.filter((vocaList) => vocaList.id !== id)));
        } catch (err) {
            console.error("Erreur lors de la suppression de la liste :", err);
        }
    };

    const handleAddProgram = () => {
        navigate("/create-vocabulary-list");
    };

    return (
        <div className="user-list-page">
            <div className="header">
                <h1>Mes listes de vocabulaire</h1>
                <button className="add-button" onClick={handleAddProgram}>Créer une nouvelle liste de vocabulaire</button>
            </div>

            <div className="liste-study-programm">

                {error ? (
                    <div>Une erreur s'est produite. Veuillez réessayer.</div>
                ) : (
                    <>
                        {isLoading ? (<div>Chargement des programmes...</div>
                        ):(
                            <div className="users-list">
                            {vocabularyLists && vocabularyLists.length > 0 ? (
                                vocabularyLists.map((vocaList) => (
                                <div className="users-card" key={vocaList.id}>
                                    <div className="clic" onClick={() => navigate(`/vocabulary/`,  { state: { idVocaList: vocaList.id } })}
                                style={{ cursor: "pointer" }}>
                                        <h2 className="users-title">{vocaList.name}</h2>
                                        <p className="users-description">Langue étudié : {vocaList.langeEtudie}</p>
                                        <p className="users-description">Langue de définition : {vocaList.langueDefinition}</p>
                                    </div>
                            <button
                                className="button"
                                onClick={()=> navigate("/modification-vocabulary", {state: {idVocaList: vocaList.id} })}
                            >
                                Modifier
                            </button>
                            <button
                                className="delete-button"
                                onClick={() => handleDelete(vocaList.id)}>
                                    <img className="delete" src={poubelle} alt="Bouton_supprimer"/>
                            </button>
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
    );
};

export default UserVocabularyListPage;
