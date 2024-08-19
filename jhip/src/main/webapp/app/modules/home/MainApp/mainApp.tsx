import './mainApp.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

const fetchItems = () => {
  return 'koerieo';
}


export const MainApp = () => {
  const account = useAppSelector(state => state.authentication.account);



  return (
    <div>
        <h2 className="display-4">
        {fetchItems()}
        </h2>
    </div>
        
  );
};

export default MainApp;
