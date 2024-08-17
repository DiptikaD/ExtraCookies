import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './users.reducer';

export const Users = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const usersList = useAppSelector(state => state.users.entities);
  const loading = useAppSelector(state => state.users.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="users-heading" data-cy="UsersHeading">
        <Translate contentKey="extracookiesApp.users.home.title">Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="extracookiesApp.users.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/users/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="extracookiesApp.users.home.createLabel">Create new Users</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {usersList && usersList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="extracookiesApp.users.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('uid')}>
                  <Translate contentKey="extracookiesApp.users.uid">Uid</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('uid')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="extracookiesApp.users.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('userName')}>
                  <Translate contentKey="extracookiesApp.users.userName">User Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userName')} />
                </th>
                <th className="hand" onClick={sort('passWord')}>
                  <Translate contentKey="extracookiesApp.users.passWord">Pass Word</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('passWord')} />
                </th>
                <th className="hand" onClick={sort('profilePicIUrl')}>
                  <Translate contentKey="extracookiesApp.users.profilePicIUrl">Profile Pic I Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profilePicIUrl')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {usersList.map((users, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/users/${users.id}`} color="link" size="sm">
                      {users.id}
                    </Button>
                  </td>
                  <td>{users.uid}</td>
                  <td>{users.email}</td>
                  <td>{users.userName}</td>
                  <td>{users.passWord}</td>
                  <td>{users.profilePicIUrl}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/users/${users.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/users/${users.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/users/${users.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="extracookiesApp.users.home.notFound">No Users found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Users;
