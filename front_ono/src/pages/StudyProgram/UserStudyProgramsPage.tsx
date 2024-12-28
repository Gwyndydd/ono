import React, { useEffect } from "react";
import poubelle from "../../assets/poubelle.png"
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/redux.store";
import { setStudyPrograms } from "../../store/slices/study-program.slice";
import { useNavigate } from "react-router-dom";
import { useDeleteStudyProgramMutation, useGetStudyProgramsByOwnerQuery } from "../../store/api/studyprogram.api";

//import "./UserStudyProgramsPage.css";
import "../../styles/stylegeneral.css"

const UserStudyProgramsPage: React.FunctionComponent = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const userId = localStorage.getItem("id"); 
    const { data, isLoading, error, refetch } = useGetStudyProgramsByOwnerQuery(userId as string, {
        refetchOnMountOrArgChange: true, // Force un refetch au montage
    });
    const [deleteStudyProgram] = useDeleteStudyProgramMutation();
    const studyPrograms = useSelector((state: RootState) => state.studyProgram.studyPrograms);

    // Mise à jour des programmes dans le store Redux
    useEffect(() => {
        if (data) {
            dispatch(setStudyPrograms(data));
        }
    }, [data, dispatch]);

    // Nettoyage des programmes lors de la sortie de la page
    useEffect(() => {
        return () => {
            dispatch(setStudyPrograms([])); // Réinitialisation des données
        };
    }, [dispatch]);

    const handleDelete = async (id: string) => {
        try {
            await deleteStudyProgram(id).unwrap();
            dispatch(setStudyPrograms(studyPrograms.filter((program) => program.id !== id)));
        } catch (err) {
            console.error("Erreur lors de la suppression du programme :", err);
        }
    };

    const handleAddProgram = () => {
        navigate("/create-studyprogram");
    };

    return (
        <div className="user-study-programs-page">
            <div className="header">
                <h1>Mes Programmes d'Étude</h1>
                <button className="add-button" onClick={handleAddProgram}>Ajouter un programme</button>
            </div>

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
                            <button
                                className="delete-button"
                                onClick={() => handleDelete(program.id)}>
                                    <img className="delete" src={poubelle} alt="Bouton_supprimer"/>
                            </button>
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

export default UserStudyProgramsPage;
