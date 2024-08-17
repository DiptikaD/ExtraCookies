import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './posts.reducer';

export const PostsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postsEntity = useAppSelector(state => state.posts.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postsDetailsHeading">
          <Translate contentKey="extracookiesApp.posts.detail.title">Posts</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postsEntity.id}</dd>
          <dt>
            <span id="postId">
              <Translate contentKey="extracookiesApp.posts.postId">Post Id</Translate>
            </span>
          </dt>
          <dd>{postsEntity.postId}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="extracookiesApp.posts.price">Price</Translate>
            </span>
          </dt>
          <dd>{postsEntity.price}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="extracookiesApp.posts.title">Title</Translate>
            </span>
          </dt>
          <dd>{postsEntity.title}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="extracookiesApp.posts.location">Location</Translate>
            </span>
          </dt>
          <dd>{postsEntity.location}</dd>
          <dt>
            <span id="availability">
              <Translate contentKey="extracookiesApp.posts.availability">Availability</Translate>
            </span>
          </dt>
          <dd>{postsEntity.availability ? <TextFormat value={postsEntity.availability} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="extracookiesApp.posts.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{postsEntity.rating}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="extracookiesApp.posts.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{postsEntity.tag}</dd>
          <dt>
            <Translate contentKey="extracookiesApp.posts.pid">Pid</Translate>
          </dt>
          <dd>{postsEntity.pid ? postsEntity.pid.id : ''}</dd>
          <dt>
            <Translate contentKey="extracookiesApp.posts.customers">Customers</Translate>
          </dt>
          <dd>{postsEntity.customers ? postsEntity.customers.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/posts" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/posts/${postsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostsDetail;