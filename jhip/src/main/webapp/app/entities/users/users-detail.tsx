import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './users.reducer';

export const UsersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const usersEntity = useAppSelector(state => state.users.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="usersDetailsHeading">
          <Translate contentKey="extracookiesApp.users.detail.title">Users</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{usersEntity.id}</dd>
          <dt>
            <span id="uid">
              <Translate contentKey="extracookiesApp.users.uid">Uid</Translate>
            </span>
          </dt>
          <dd>{usersEntity.uid}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="extracookiesApp.users.email">Email</Translate>
            </span>
          </dt>
          <dd>{usersEntity.email}</dd>
          <dt>
            <span id="userName">
              <Translate contentKey="extracookiesApp.users.userName">User Name</Translate>
            </span>
          </dt>
          <dd>{usersEntity.userName}</dd>
          <dt>
            <span id="passWord">
              <Translate contentKey="extracookiesApp.users.passWord">Pass Word</Translate>
            </span>
          </dt>
          <dd>{usersEntity.passWord}</dd>
          <dt>
            <span id="profilePicIUrl">
              <Translate contentKey="extracookiesApp.users.profilePicIUrl">Profile Pic I Url</Translate>
            </span>
          </dt>
          <dd>{usersEntity.profilePicIUrl}</dd>
        </dl>
        <Button tag={Link} to="/users" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/users/${usersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UsersDetail;
