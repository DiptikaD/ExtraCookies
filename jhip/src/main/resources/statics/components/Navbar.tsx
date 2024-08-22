import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Dialog, DialogTitle, DialogContent, TextField, DialogActions } from '@mui/material';

const AppNavbar: React.FC = () => {
  const [open, setOpen] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    // Pseudo-code for login functionality
    console.log(`Username: ${username}, Password: ${password}`);
    setOpen(false);
  };

  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h2" sx={{ flexGrow: 1 }}>
            Extra Cookies
          </Typography>
          <Button color="inherit" onClick={() => setOpen(true)}>
            User
          </Button>
        </Toolbar>
      </AppBar>

      <Dialog open={open} onClose={() => setOpen(false)}>
        <DialogTitle>User Login</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Username"
            type="text"
            fullWidth
            variant="standard"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            margin="dense"
            label="Password"
            type="password"
            fullWidth
            variant="standard"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)} color="secondary">
            Close
          </Button>
          <Button onClick={handleLogin} color="primary">
            Log In
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default AppNavbar;
