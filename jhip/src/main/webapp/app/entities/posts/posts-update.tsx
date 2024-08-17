import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICustomers } from 'app/shared/model/customers.model';
import { getEntities as getCustomers } from 'app/entities/customers/customers.reducer';
import { IPosts } from 'app/shared/model/posts.model';
import { Tags } from 'app/shared/model/enumerations/tags.model';
import { getEntity, updateEntity, createEntity, reset } from './posts.reducer';

export const PostsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customers = useAppSelector(state => state.customers.entities);
  const postsEntity = useAppSelector(state => state.posts.entity);
  const loading = useAppSelector(state => state.posts.loading);
  const updating = useAppSelector(state => state.posts.updating);
  const updateSuccess = useAppSelector(state => state.posts.updateSuccess);
  const tagsValues = Object.keys(Tags);

  const handleClose = () => {
    navigate('/posts');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getCustomers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.postId !== undefined && typeof values.postId !== 'number') {
      values.postId = Number(values.postId);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    values.availability = convertDateTimeToServer(values.availability);
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }

    const entity = {
      ...postsEntity,
      ...values,
      customers: customers.find(it => it.id.toString() === values.customers?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          availability: displayDefaultDateTime(),
        }
      : {
          tag: 'PRODUCE',
          ...postsEntity,
          availability: convertDateTimeFromServer(postsEntity.availability),
          customers: postsEntity?.customers?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterApp.posts.home.createOrEditLabel" data-cy="PostsCreateUpdateHeading">
            <Translate contentKey="jhipsterApp.posts.home.createOrEditLabel">Create or edit a Posts</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="posts-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterApp.posts.postId')}
                id="posts-postId"
                name="postId"
                data-cy="postId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('jhipsterApp.posts.price')} id="posts-price" name="price" data-cy="price" type="text" />
              <ValidatedField label={translate('jhipsterApp.posts.title')} id="posts-title" name="title" data-cy="title" type="text" />
              <ValidatedField
                label={translate('jhipsterApp.posts.location')}
                id="posts-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.posts.availability')}
                id="posts-availability"
                name="availability"
                data-cy="availability"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('jhipsterApp.posts.rating')} id="posts-rating" name="rating" data-cy="rating" type="text" />
              <ValidatedField label={translate('jhipsterApp.posts.tag')} id="posts-tag" name="tag" data-cy="tag" type="select">
                {tagsValues.map(tags => (
                  <option value={tags} key={tags}>
                    {translate('jhipsterApp.Tags.' + tags)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="posts-customers"
                name="customers"
                data-cy="customers"
                label={translate('jhipsterApp.posts.customers')}
                type="select"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/posts" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PostsUpdate;
