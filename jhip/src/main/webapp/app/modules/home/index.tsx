import React from 'react';

import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import MainApp from './MainApp/mainApp';

const HomeRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="mainApp" element={<MainApp />} />
         <p className="lead">
          This is your homepage
        </p>
    </ErrorBoundaryRoutes>
  </div>
);

export default HomeRoutes;
