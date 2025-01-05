import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { string, z } from "zod";
import { useDeleteVocabularyMutation, useGetAllVocabularyInListQuery, useRegisterVocabularyMutation, useUpdateVocabularyMutation } from "../../store/api/vocabulary.api copy";
import { useCallback, useEffect, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { setVocabularyList } from "../../store/slices/vocabulary.slice";
import { RootState } from "../../store/redux.store";
import { zodResolver } from "@hookform/resolvers/zod";
import { CreateVocabularyDto } from "../../models/vocabulary.model";
import { addToast } from "../../store/slices/toast.slice";

import "./modif_voca.css"


const RegisterSchema = z.object({
    word: z.string().min(1,"Entrer un mot"),
    definition: z.string(),
    idList: z.boolean(),
    type: z.enum(["Nombre","Nom","Adjectif","Verbe","Preposition"]),
});

type RegisterSchemaType = z.infer<typeof RegisterSchema>

const CreateVocabularyPage : React.FunctionComponent = () =>{

    const dispatch = useDispatch();

    const { state: { idVocaList } = {} } = useLocation(); // Récupération de l'ID de la liste via la navigation

    const { data, isLoading, error, refetch } = useGetAllVocabularyInListQuery(idVocaList as string, {
        refetchOnMountOrArgChange: true, // Force un refetch au montage
    });

    const [editingId, setEditingId] = useState<string | null>(null); // ID de l'élément en cours de modification

    //register 
    const [registerVocabulary] = useRegisterVocabularyMutation();
    const { register, handleSubmit, reset, formState: { errors } } = useForm<RegisterSchemaType>({
    resolver: zodResolver(RegisterSchema),
    });

    const [deleteVocabulary] = useDeleteVocabularyMutation();
    const vocabularyList = useSelector((state:RootState)=> state.vocabulary.vocabularyList);

    const [updateVocabulary] = useUpdateVocabularyMutation();

    useEffect(()=>{
        if(data){
            dispatch(setVocabularyList([]));
        }
    }, [data,dispatch])

    // Nettoyage des listes de vocabulaires lors de la sortie de la page
    useEffect(() => {
        return () => {
            dispatch(setVocabularyList([])); // Réinitialisation des données
        };
    }, [dispatch]);

    const updateSubmit : SubmitHandler<RegisterSchemaType> = useCallback(async (data)=>{
        const dto : CreateVocabularyDto ={
            word: data.word,
            definition: data.definition,
            idList: idVocaList,
            type: data.type
        };
        if(editingId){
            await updateVocabulary({ vocaId: editingId, dto: dto})
            .unwrap()
            .catch((e)=>{
                console.error("error during vocabulary update: %o", e)
                dispatch(addToast({id:"create-vocabulary-error",message:"erreur lors de l'update", type:"error"}))
            });
            setEditingId(null);
        }

    },[updateVocabulary,dispatch]);

    const onSubmit : SubmitHandler<RegisterSchemaType> = useCallback(async (data)=>{
        console.log("submit");
        const dto : CreateVocabularyDto ={
            word: data.word,
            definition: data.definition,
            idList: idVocaList,
            type: data.type
        };
        console.log(dto);
        await registerVocabulary(dto)
            .unwrap()
            .then(()=>{
                refetch
            })
            .catch((e)=>{
                console.error("error during vocabulary creation: %o", e)
                dispatch(addToast({id:"create-vocabulary-error",message:"erreur lors de la creation", type:"error"}))
            });
    },[registerVocabulary,updateVocabulary,dispatch,setEditingId,refetch]);

    const handleDelete = async (id: string) => {
        try {
            await deleteVocabulary(id).unwrap();
            dispatch(setVocabularyList(vocabularyList.filter((voca) => voca.id !== id)));
        } catch (err) {
            console.error("Erreur lors de la suppression du vocabulaire :", err);
        }
    };

      // Gestion du clic sur le bouton Annuler
    const handleCancel = () => {
        setEditingId(null);
        reset();
    };

      // Gestion du clic sur le bouton Modifier
    const handleEdit = (voca: typeof vocabularyList[number]) => {
        setEditingId(voca.id);
        //reset(voca);
    };

    return(
        <div className="create-vocabulary-page">
            <h1>Gérer le Vocabulaire</h1>

            <div className="vocabulary-list">
                {/* Liste des vocabulaires */}
                {vocabularyList.map((voca) => (
                <div className="vocabulary-card" key={voca.id}>
                    {editingId === voca.id ? (
                    <form onSubmit={handleSubmit(updateSubmit)} className="vocabulary-form">
                        <input
                        {...register("word")}
                        placeholder="Mot"
                        className={`input ${errors.word ? "error" : ""}`}
                        />
                        {errors.word && <span className="error-message">{errors.word.message}</span>}

                        <input
                        {...register("definition")}
                        placeholder="Définition"
                        className="input"
                        />

                        <select {...register("type")} className="input">
                        <option value="">Type</option>
                        <option value="Nombre">Nombre</option>
                        <option value="Nom">Nom</option>
                        <option value="Adjectif">Adjectif</option>
                        <option value="Verbe">Verbe</option>
                        <option value="Préposition">Préposition</option>
                        </select>

                        <button type="submit" className="save-button">
                        Sauvegarder
                        </button>
                        <button type="button" onClick={handleCancel} className="cancel-button">
                        Annuler
                        </button>
                    </form>
                    ) : (
                    <>
                        <h2>{voca.word}</h2>
                        <p>{voca.definition || ""}</p>
                        <span className="vocabulary-type">{voca.type}</span>
                        <button onClick={() => handleEdit(voca)} className="edit-button">
                        Modifier
                        </button>
                    </>
                    )}
                </div>
                ))}

                {/* Formulaire pour ajouter un nouveau vocabulaire */}
                {editingId === null && (
                <form onSubmit={handleSubmit(onSubmit)} className="vocabulary-form">
                    <input
                    {...register("word")}
                    placeholder="Mot"
                    className={`input ${errors.word ? "error" : ""}`}
                    />
                    {errors.word && <span className="error-message">{errors.word.message}</span>}

                    <input
                    {...register("definition")}
                    placeholder="Définition"
                    className="input"
                    />

                    <select {...register("type")} className="input">
                    <option value="">Type</option>
                    <option value="Nombre">Nombre</option>
                    <option value="Nom">Nom</option>
                    <option value="Adjectif">Adjectif</option>
                    <option value="Verbe">Verbe</option>
                    <option value="Preposition">Préposition</option>
                    </select>

                    <button type="submit" className="save-button">
                    Sauvegarder
                    </button>
                </form>
                )}
            </div>
        </div>
    );

};

export default CreateVocabularyPage;