import { Component } from "react";
import { Outlet, useLocation, Navigate } from "react-router-dom";

import { createTheme, ThemeProvider } from '@mui/material/styles';
import Container from '@mui/material/Container';

import { CheckToken } from '~/auth/CheckTokenl';
import Loading from '~/component/Loading';

const theme = createTheme();

export default function PrivateRoute() {
  const location = useLocation();
  const { isAuth } = CheckToken(location.key);

  if (isAuth === 'Failed') {
    return (
      // <Navigate to="/user/login" state={{ from: location }} />
      <Navigate to="/login" state={{ from: location }} />
    )
  } else if (isAuth === 'Loading') {
    return <Loading />
  }


  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <Outlet />
      </Container>
    </ThemeProvider>
  )
}

