import React, { useState } from 'react';
import { Grid, Button, Card, CardContent, CardMedia, Typography, Box } from '@mui/material';
import CreatePostModal from './CreatePostModal';

interface Post {
  name: string;
  location: string;
  price: number;
  image: string;
}

const HomePage: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [showModal, setShowModal] = useState(false);

  const addPost = (newPost: Post) => {
    setPosts([...posts, newPost]);
  };

  return (
    <>
      <Typography variant="h4" component="h1" align='center'>
        Food Posts
      </Typography>
      <Box sx={{ display: 'flex', justifyContent: 'center', mb: 2 }}>
        <Button variant="contained" color="primary" onClick={() => setShowModal(true)}>
          Create Post
        </Button>
      </Box>

      <Grid container spacing={4} style={{ marginTop: '16px' }}>
        {posts.map((post, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Card>
              <CardMedia
                component="img"
                alt={post.name}
                height="140"
                image={post.image}
                title={post.name}
              />
              <CardContent>
                <Typography variant="h5" component="div">
                  {post.name}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Location: {post.location} <br />
                  Price: ${post.price}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <CreatePostModal show={showModal} onHide={() => setShowModal(false)} addPost={addPost} />
    </>
  );
};

export default HomePage;
