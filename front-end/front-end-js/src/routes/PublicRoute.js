import { Component } from "react";
import { Outlet, useLocation, Navigate } from "react-router-dom";

import { createTheme, ThemeProvider } from '@mui/material/styles';
import Container from '@mui/material/Container';

import { CheckToken } from '~/auth/AuthUtils';
import Loading from '~/component/Loading';

const theme = createTheme();

export default function PublicRoute() {
  const location = useLocation();
  const { isAuth } = CheckToken(location.key);

  if (isAuth === 'Success') {
    return (
      <Navigate to="/" state={{ from: location }} />
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

