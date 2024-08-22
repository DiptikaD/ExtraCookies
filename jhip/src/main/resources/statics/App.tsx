import React from 'react';
import { ThemeProvider, createTheme, CssBaseline, Container } from '@mui/material';
import AppNavbar from './components/Navbar';
import HomePage from './components/HomePage';
import SearchAppBar from './components/SearchBar';

const theme = createTheme({
  palette: {
    primary: {
      main: '#ccb9f0',
    },
    secondary: {
      main: '#92d49f',
    },
    background: {
      default: '#f0dfb9',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 600,
    },
  },
});

const App: React.FC = () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AppNavbar />
      <Container maxWidth="lg">
      <SearchAppBar/>
        <HomePage />
      </Container>
    </ThemeProvider>
  );
};

export default App;
