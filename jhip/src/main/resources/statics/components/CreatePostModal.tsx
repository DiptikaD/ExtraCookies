import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField } from '@mui/material';

interface CreatePostModalProps {
  show: boolean;
  onHide: () => void;
  addPost: (newPost: Post) => void;
}

interface Post {
  name: string;
  location: string;
  price: number;
  image: string;
}

const CreatePostModal: React.FC<CreatePostModalProps> = ({ show, onHide, addPost }) => {
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');
  const [price, setPrice] = useState('');
  const [image, setImage] = useState('');

  const handleSubmit = () => {
    const newPost: Post = { name, location, price: Number(price), image };
    addPost(newPost);
    onHide();
  };

  return (
    <Dialog open={show} onClose={onHide}>
      <DialogTitle>Create Post</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          label="Food Name"
          type="text"
          fullWidth
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <TextField
          margin="dense"
          label="Location"
          type="text"
          fullWidth
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
        <TextField
          margin="dense"
          label="Price"
          type="number"
          fullWidth
          value={price}
          onChange={(e) => setPrice(e.target.value)}
        />
        <TextField
          margin="dense"
          label="Image URL"
          type="text"
          fullWidth
          value={image}
          onChange={(e) => setImage(e.target.value)}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onHide} color="secondary">
          Close
        </Button>
        <Button onClick={handleSubmit} color="primary">
          Create Post
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CreatePostModal;
