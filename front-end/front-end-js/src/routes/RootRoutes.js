// This is a React Router v6 app
import { Routes, Route, Navigate } from "react-router-dom";
import { SimpleLayout } from '~/views/layouts';
import SignIn from '~/views/components/sign/SignIn';

const RootRoutes = () => (
  <Routes>
    <Route element={<SimpleLayout />}>
      <Route path="/" element={<Navigate replace to="/sign-in/" />} />
      <Route path="/sign-in/" element={<SignIn />} />
    </Route>
  </Routes>
)

export default RootRoutes;