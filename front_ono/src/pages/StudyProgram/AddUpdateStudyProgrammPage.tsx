import { zodResolver } from "@hookform/resolvers/zod";
import { string, z } from "zod";
import React, { FunctionComponent, useCallback, useEffect } from "react";
import { RootState } from "../../store/redux.store";
import { Button, FieldError, Form, Input, Label, Switch, TextField } from "react-aria-components";
import { SubmitHandler, useForm } from "react-hook-form";
import { useRegisterMutation } from "../../store/api/users.api";
import { useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { addToast, removeToast } from "../../store/slices/toast.slice";

import { CreateStudyProgramDto } from "../../models/studyprogram.model";
import { useGetStudyProgramByIDQuery, useRegisterStudyProgramMutation, useUpdateStudyProgramMutation } from "../../store/api/studyprogram.api";
import "./CreateStudyProgramPage.css";
import { useAddSPtoVocabularyListMutation, useGetVocabularyListsByOwnerQuery } from "../../store/api/vocabularyList.api";
import { setVocabularyLists } from "../../store/slices/vocabulary-list.slice";
import { setCurrentStudyProgram } from "../../store/slices/study-program.slice";

const RegisterSchema = z.object({
    name: z.string().min(1,"Champs requis"),
    description: z.string(),
    prive: z.boolean()
});


type RegisterSchemaType = z.infer<typeof RegisterSchema>

const AddUpdateStudyProgramPage: React.FunctionComponent = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const userId = localStorage.getItem("id")
    const { state: { idSPList } = {} } = useLocation(); // Récupération de l'ID de la liste via la navigation

    if(userId === null){
        throw new Error("Pas d'utilisateur")
    }

    const { data:spData} = useGetStudyProgramByIDQuery(idSPList as string, {
        refetchOnMountOrArgChange: true, // Force un refetch au montage
    });

    const currentSP = useSelector((state: RootState)=> state.studyProgram.currentStudyProgram);

    useEffect(() => {
        if (spData) {
            dispatch(setCurrentStudyProgram(spData));
        }
    }, [spData, dispatch]);

    useEffect(() => {
        if (spData) {
            setValue("name", spData.name || "");
            setValue("description", spData.description || "");
        }
    }, [spData, setValue]);

    const { data: vocaData, isLoading:vocaLoading, error, refetch } = useGetVocabularyListsByOwnerQuery(userId as string, {
        refetchOnMountOrArgChange: true, // Force un refetch au montage
    });
    const vocabularyLists = useSelector((state: RootState) => state.vocabularyList.vocabularyLists);

    // Mise à jour des listes de vocabulaires dans le store Redux
    useEffect(() => {
        if (vocaData) {
            dispatch(setVocabularyLists(vocaData));
        }
    }, [vocaData, dispatch]);

    //UPDATE SP
    const [updateStudyProgram] = useUpdateStudyProgramMutation();
    const { register, handleSubmit, formState:{errors, isLoading}} = useForm<RegisterSchemaType>({ resolver: zodResolver(RegisterSchema)})

    //ADD VOCALIST
    const [addSPtoVoca] = useAddSPtoVocabularyListMutation();

    const add = useCallback(async (id: string)=>{
        await addSPtoVoca({ vocabularyListId:id, studyProgramId: idSPList})
        .unwrap()
        .then(()=>{
            dispatch(addToast({ id: "add-success", message: "Ajout réussi", type: "success" }));
        })
        .catch((e)=>{
            console.error("Error during adding study program : %o",e);
            dispatch(addToast({id:"add-study-program-error",message:"Erreur lors de l'ajout", type:"error"}))
        });

    },[addSPtoVoca,dispatch])


    const onSubmit: SubmitHandler<RegisterSchemaType> = useCallback(async (data) =>{
        dispatch(removeToast("create-study-program-error"));
        const dto: CreateStudyProgramDto = {
            name: data.name,
            description: data.description,
            idOwner: userId as string,
            prive: data.prive
        };

        console.log(dto)
        await updateStudyProgram({ studyProgramId: idSPList, dto: dto })
                .unwrap()
                .then(()=>{
                    dispatch(addToast({id:"update-study-program-sucess", message:"Update réussite", type: "success"}));
                    navigate("/study_program",{replace :false});
                })
                .catch((e)=>{
                    console.error("Error during study program update : %o",e);
                    dispatch(addToast({id:"update-study-program-error",message:"Erreur lors de l'update", type:"error"}))
                });

    }, [updateStudyProgram, navigate,dispatch]);

    const [selected, setSelected] = React.useState(false);

    return (
        <div className="create-study-program-page">
            <h1>Mise à jour d'un Programme d'Étude</h1>
            <Form 
                onSubmit={handleSubmit(onSubmit)}
                validationErrors={{
                    name:errors.name?.message ?? ''
                }}
            >
                    <TextField name="name-study-program" type="text" >
                        {currentSP?.name}
                        <Label>Nom du programme</Label>
                        <Input {...register("name")} />
                        <FieldError />
                    </TextField>
                    <TextField name="description-study-program" type="text">
                        <Label>Description</Label>
                        <Input {...register("description")} />
                        {currentSP?.description}
                        <FieldError />
                    </TextField>
                    <Switch isSelected={selected} onChange={setSelected}>
                        <div className="indicator"/>
                       Visibilité du programme
                       <Input  className="hidden-input" type="checkbox" {...register("prive")}/>
                    </Switch>
                    <p>{spData?.prive ? 'public' : 'privé'}</p>
                    
                    <Button type="submit" isDisabled={isLoading}>Envoyer</Button>
                </Form>
            <div>
                <div className="header">
                <h1>Mes listes de vocabulaire</h1>
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
                                    <div className="clic">
                                        <h2 className="users-title">{vocaList.name}</h2>
                                        <p className="users-description">Langue étudié : {vocaList.langeEtudie}</p>
                                        <p className="users-description">Langue de définition : {vocaList.langueDefinition}</p>
                                    </div>
                                    {vocaList.idProgrammeEtude !== idSPList && (
                                        <button
                                            className="delete-button"
                                            onClick={() => add(vocaList.id)}
                                        >
                                            ajouter
                                        </button>
                                    )}
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
    )
};

export default AddUpdateStudyProgramPage;

function setValue(arg0: string, arg1: string) {
    throw new Error("Function not implemented.");
}
