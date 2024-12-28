import { FunctionComponent, useMemo } from "react";
import { useAppSelector } from "../store/redux.hooks";
import { Navigate, Outlet } from "react-router-dom";


const AuthenticationGuard: FunctionComponent = () => {
    const authState = useAppSelector(state => state.authentication);
    
    const isAuthenticated = useMemo(() => authState.isAuthenticated && !!authState.token, [authState])

    return (isAuthenticated ? <Outlet/> : <Navigate to={"/"}/>);
}

export default AuthenticationGuard;