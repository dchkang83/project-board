// This is a React Router v6 app
import { Routes, Route, Navigate } from "react-router-dom";
// import { SimpleLayout } from '~/views/layouts';
import Login from '~/views/Login';
import Home from '~/views/Home';
import Logout from '~/views/Logout';

import PublicRoute from './PublicRoute';
import PrivateRoute from './PrivateRoute';

const RootRoutes = () => (
  <Routes>
    <Route element={<PublicRoute />}>
      {/* <Route path="/" element={<Navigate replace to="/login" />} /> */}
      <Route path="/login/" element={<Login />} />
    </Route>

    <Route element={<PrivateRoute />}>
        <Route path="/" element={<Home />} />
        <Route path="/logout" element={<Logout />} />
    </Route>
  </Routes>
)

export default RootRoutes;