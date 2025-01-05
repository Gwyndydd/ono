import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import React, { FunctionComponent, useCallback } from "react";
import { Button, FieldError, Form, Input, Label, Switch, TextField } from "react-aria-components";
import { SubmitHandler, useForm } from "react-hook-form";
import { useRegisterMutation } from "../../store/api/users.api";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { addToast, removeToast } from "../../store/slices/toast.slice";

import { CreateStudyProgramDto } from "../../models/studyprogram.model";
import { useRegisterStudyProgramMutation } from "../../store/api/studyprogram.api";
import "./CreateStudyProgramPage.css";


const RegisterSchema = z.object({
    name: z.string().min(1,"Champs requis"),
    description: z.string(),
    prive: z.boolean()
});

type RegisterSchemaType = z.infer<typeof RegisterSchema>

const CreateStudyProgramPage: React.FunctionComponent = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const userId = localStorage.getItem("id")

    if(userId === null){
        throw new Error("Pas d'utilisateur")
    }

    const [registerStudyProgram] = useRegisterStudyProgramMutation();
    const { register, handleSubmit, formState:{errors, isLoading}} = useForm<RegisterSchemaType>({ resolver: zodResolver(RegisterSchema)})

    const onSubmit: SubmitHandler<RegisterSchemaType> = useCallback(async (data) =>{
        dispatch(removeToast("create-study-program-error"));
        const dto: CreateStudyProgramDto = {
            name: data.name,
            description: data.description,
            idOwner: userId as string,
            prive: data.prive
        };

        console.log(dto)
        await registerStudyProgram(dto)
                .unwrap()
                .then(()=>{
                    dispatch(addToast({id:"create-study-program-sucess", message:"Creation réussite", type: "success"}));
                    navigate("/study_program",{replace :false});
                })
                .catch((e)=>{
                    console.error("Error during study program creation : %o",e);
                    dispatch(addToast({id:"create-study-program-error",message:"Erreur lors de la creation", type:"error"}))
                });

    }, [registerStudyProgram, navigate,dispatch]);

    const [selected, setSelected] = React.useState(false);

    return (
        <div className="create-study-program-page">
            <h1>Créer un Programme d'Étude</h1>
            <Form 
                onSubmit={handleSubmit(onSubmit)}
                validationErrors={{
                    name:errors.name?.message ?? ''
                }}
            >
                    <TextField name="name-study-program" type="text" >
                        <Label>Nom du programme</Label>
                        <Input {...register("name")} />
                        <FieldError />
                    </TextField>
                    <TextField name="description-study-program" type="text">
                        <Label>Description</Label>
                        <Input {...register("description")} />
                        <FieldError />
                    </TextField>
                    <Switch isSelected={selected} onChange={setSelected}>
                        <div className="indicator"/>
                       Visibilité du programme
                       <Input  className="hidden-input" type="checkbox" {...register("prive")}/>
                    </Switch>
                    <p>{selected ? 'public' : 'privé'}</p>
                    
                    <Button type="submit" isDisabled={isLoading}>Envoyer</Button>
                </Form>
                
        </div>
    )
};

export default CreateStudyProgramPage;
