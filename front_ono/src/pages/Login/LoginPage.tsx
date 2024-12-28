import { zodResolver } from "@hookform/resolvers/zod";
import { FunctionComponent } from "react";
import { Button, FieldError, Form, Input, Label, Link, TextField } from "react-aria-components";
import { SubmitHandler, useForm } from "react-hook-form";
import { z } from "zod";
import { useNavigate } from "react-router-dom";
import { useLoginMutation } from "../../store/api/authentication.api";
import { DateTime } from "luxon";

import './LoginPage.css';
import { useDispatch } from "react-redux";
import { addToast } from "../../store/slices/toast.slice";
import { uid } from "radash";

const LoginSchema = z.object({
    email: z.string()
        .min(1, "Email requis")
        .email("Email invalide"),
    password: z.string()
        .min(1, "Mot de passe requis")
  });

type LoginSchemaType = z.infer<typeof LoginSchema>;

const LoginPage: FunctionComponent = () => {
    const dispatch = useDispatch();
    
    const navigate = useNavigate();
    const [login, { isLoading }] = useLoginMutation();

    const { register, handleSubmit, formState: { errors } } = useForm<LoginSchemaType>({ resolver: zodResolver(LoginSchema) });

    const onSubmit: SubmitHandler<LoginSchemaType> = async (data) => {
        try {
            const result = await login(data).unwrap();
            // set auth on local storage
            localStorage.setItem("id", result.id)
            localStorage.setItem("token", result.token);
            localStorage.setItem("expiresAt", DateTime.now().plus({ seconds: result.expiresIn }).toString());

            // redirect to search page
            navigate("/compte"); // ATTENTION VERIFIE LA ROUTE

        } catch (e) {
            console.error("Error during login : %o", e);
            dispatch(addToast({ id: uid(7), message: 'Erreur lors de la connexion', type: 'error' }));
        }
    }

    return (
        <div className="login-page">
            <h1 className="page-title">Login Page</h1>
            <div className="login-form">
                <Form 
                    onSubmit={handleSubmit(onSubmit)} 
                    validationErrors={{ 
                        email: errors.email?.message ?? '',
                        password: errors.password?.message ?? ''
                    }}
                >
                    <TextField name="email" type="text" aria-errormessage={errors.email?.message} >
                        <Label>Email</Label>
                        <Input {...register("email")}/>
                        <FieldError/>
                    </TextField>
                    <TextField name="password" type="password">
                        <Label>Mot de passe</Label>
                        <Input {...register("password")}/>
                        <FieldError/>   
                    </TextField>
                    <div className="login-buttons">
                        <Link className="react-aria-Button create-account-link" target="_blank" onPress={() => navigate('/register')}>
                            Créer un compte
                        </Link>
                        <Button type="submit" isDisabled={isLoading}>Login</Button>
                    </div>
                </Form>
            </div>
        </div>
    );
};

export default LoginPage;