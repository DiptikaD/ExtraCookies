import users from 'app/entities/users/users.reducer';
import posts from 'app/entities/posts/posts.reducer';
import customers from 'app/entities/customers/customers.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  users,
  posts,
  customers,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
