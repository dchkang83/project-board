import { Component } from "react";
import { Outlet } from "react-router-dom";

import { createTheme, ThemeProvider } from '@mui/material/styles';
import Container from '@mui/material/Container';

const theme = createTheme();

class SimpleLayout extends Component {
  render() {
    return (
      <ThemeProvider theme={theme}>
        <Container component="main" maxWidth="xs">
          <Outlet />
        </Container>
      </ThemeProvider>
    )
  }
}

export default SimpleLayout;
