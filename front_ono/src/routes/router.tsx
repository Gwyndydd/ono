import { createBrowserRouter } from "react-router-dom";
import HomePage from "../pages/HomePage/HomePage";
import LoginPage from "../pages/Login/LoginPage";
import NotFoundPage from "../pages/not-found/NotFoundPage";
import Root from "../pages/Root";
import Layout from "../components/layout/Layout";
import RegisterPage from "../pages/register/RegisterPage";
import AuthenticationGuard from "./AuthenticationGuard";
import UserStudyProgramsPage from "../pages/StudyProgram/UserStudyProgramsPage";
import CreateStudyProgramPage from "../pages/StudyProgram/CreateStudyProgramPage";

const router = createBrowserRouter([
    {
      id: "root",
      element: <Root/>,
      errorElement: <NotFoundPage/>,
      children: [
        {
          Component: Layout,
          errorElement: <NotFoundPage/>,
          children: [
            {
              id: "login",
              path: "/login",
              element: <LoginPage />,
            },
            {
              id: "register",
              path: "/register",
              element: <RegisterPage />,
            },
          ]
        },
        {
          element: <Layout displayNavBar={true}/>,
          children: [
            {
              id: "default",
              path: "/",
              element: <HomePage />,
            },
            {
              id: "study_program",
              path: "/study_program",
              element: <UserStudyProgramsPage />,
            },
            {
              id: "cr_sp",
              path: "/create-studyprogram",
              element: <CreateStudyProgramPage />,
            },
            {
              element: <AuthenticationGuard/>, 
              //Page non accessible sans authentification => redirect vers l'authentification
              children: [
                /*{
                  id: "study-program",
                  path: "/study-program",
                  element: <StudyProgramPage />,
                }*/
              ]
            },
          ],
        },
      ],
    }
  ]);
  
  export default router;