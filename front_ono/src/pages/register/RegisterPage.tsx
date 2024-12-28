import { zodResolver } from "@hookform/resolvers/zod";
import { FunctionComponent, useCallback } from "react";
import { Button, FieldError, Form, Input, Label, TextField } from "react-aria-components";
import { SubmitHandler, useForm } from "react-hook-form";
import { z } from "zod";
import { useRegisterMutation } from "../../store/api/users.api";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { addToast, removeToast } from "../../store/slices/toast.slice";

import './RegisterPage.css';
import { UserRegistrationDto } from "../../models/users.model";

const RegisterSchema = z.object({
    username: z.string().min(1, "Champs requis"),
    email: z.string().email("Email invalide"),
    password: z.string()
        .min(8, "8 caractères minimum")
        .regex(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/, "Doit contenir : 1 majuscule, 1 minuscule, 1 chiffre"),
    confirmPassword: z.string()
        .min(1, "Champs requis")
}).refine((data) => data.password === data.confirmPassword, {
    message: "Le mot de passe doit être identique",
    path: ["confirmPassword"],
});

type RegisterSchemaType = z.infer<typeof RegisterSchema>

const RegisterPage: FunctionComponent = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [registerUser] = useRegisterMutation();
    const { register, handleSubmit, formState: { errors, isLoading } } = useForm<RegisterSchemaType>({ resolver: zodResolver(RegisterSchema) })

    const onSubmit: SubmitHandler<RegisterSchemaType> = useCallback(async (data) => {
        dispatch(removeToast("register-error"));
        const dto: UserRegistrationDto = {
            username: data.username,
            email: data.email,
            password: data.password
        };

        await registerUser(dto)
            .unwrap()
            .then(() => {
                dispatch(addToast({ id: "register-success", message: "Inscription réussie", type: "success" }));
                navigate("/login", { replace: false });
            })
            .catch((e) => {
                console.error("Error during registration : %o", e);
                dispatch(addToast({ id: "register-error", message: "Erreur lors de l'inscription", type: "error" }));
            });

    }, [registerUser, navigate, dispatch]);

    return (
        <div className="register-page">
            <h1>Register Page</h1>
            <div className="register-form">
                <Form
                    onSubmit={handleSubmit(onSubmit)}
                    validationErrors={{
                        username: errors.username?.message ?? '',
                        email: errors.email?.message ?? '',
                        password: errors.password?.message ?? '',
                        confirmPassword: errors.confirmPassword?.message ?? ''
                    }}
                >
                    <TextField name="username" type="text" >
                        <Label>Username</Label>
                        <Input {...register("username")} />
                        <FieldError />
                    </TextField>
                    <TextField name="email" type="email">
                        <Label>Email</Label>
                        <Input {...register("email")} />
                        <FieldError />
                    </TextField>
                    <TextField name="password" type="password">
                        <Label>Password</Label>
                        <Input {...register("password")} />
                        <FieldError />
                    </TextField>
                    <TextField name="confirmPassword" type="password">
                        <Label>Confirmation du password</Label>
                        <Input {...register("confirmPassword")} />
                        <FieldError />
                    </TextField>
                    <Button type="submit" isDisabled={isLoading}>Envoyer</Button>
                </Form>
            </div>
        </div>
    )
};

export default RegisterPage;