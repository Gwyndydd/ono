import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/redux.store";
import "./flip_card.css"; // Assurez-vous d'avoir les styles CSS appropriés
import { useGetAllVocabularyInListQuery, useGetVocabularyByIDQuery } from "../../store/api/vocabulary.api copy";
import { setVocabularyList } from "../../store/slices/vocabulary.slice";
import { addToast } from "../../store/slices/toast.slice";

const VocabularyPage: React.FunctionComponent = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    
    const { state: { idVocaList } = {} } = useLocation(); // Récupération de l'ID de la liste via la navigation

    const { data, isLoading, error, refetch } = useGetAllVocabularyInListQuery(idVocaList as string, {
        refetchOnMountOrArgChange: true, // Force un refetch au montage
    });
  
    // Sélectionnez les vocabulaires depuis le store Redux
    const allVocabulary = useSelector((state: RootState) => state.vocabulary.vocabularyList);

  
    // État local pour gérer l'index courant
    const [currentIndex, setCurrentIndex] = useState(0);
  
    // Vérifiez que des vocabulaires sont disponibles
    useEffect(() => {
      if (data) {
        dispatch(setVocabularyList(data));
      }
      else{
        dispatch(addToast({id:"access-vocabulary-unsuccessful", message:"acces denied", type: "info"}));
        navigate("/vocabulary-list"); // Redirigez si aucune donnée
      }
    }, [data,dispatch, navigate]);

    //Nettoyage
    useEffect(()=>{
      return () => {
        dispatch(setVocabularyList([]))
      };
    },[dispatch])

  // Gestion de la navigation précédente/suivante
  const handlePrevious = () => {
    setCurrentIndex((prevIndex) => (prevIndex > 0 ? prevIndex - 1 : allVocabulary.length - 1));
  };

  const handleNext = () => {
    setCurrentIndex((prevIndex) => (prevIndex < allVocabulary.length - 1 ? prevIndex + 1 : 0));
  };

  // Récupération de la carte actuelle
  const currentCard = allVocabulary[currentIndex];

  return (
    <div className="vocabulary-page">
      {/* Bouton de retour */}
      <button className="back-button" onClick={() => navigate("/vocabulary-list")}>
      Précédent
      </button>

      {/* Carte de vocabulaire */}
      {currentCard ? (
        <div className="box">
          <div className="box-inner">
              {/* Face avant */}
              <div className="box-front">
                <h2 className="word">{currentCard.word}</h2>
              </div>

            {/* Face arrière */}
            <div className="box-back">
              <h2 className="definition">{currentCard.definition}</h2>
            </div>
          </div>
        </div>
      ) : (
        <p>Chargement des données ou aucune carte disponible.</p>
      )}

      {/* Contrôles de navigation */}
      <div className="navigation-controls">
        <button onClick={handlePrevious} className="nav-arrow">
          ◀
        </button>
        <button onClick={handleNext} className="nav-arrow">
          ▶
        </button>
      </div>
    </div>
  );
};

export default VocabularyPage;