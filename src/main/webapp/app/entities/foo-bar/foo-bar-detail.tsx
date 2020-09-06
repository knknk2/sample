import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './foo-bar.reducer';
import { IFooBar } from 'app/shared/model/foo-bar.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFooBarDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FooBarDetail = (props: IFooBarDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { fooBarEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="sampleApp.fooBar.detail.title">FooBar</Translate> [<b>{fooBarEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="firstName">
              <Translate contentKey="sampleApp.fooBar.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{fooBarEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="sampleApp.fooBar.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{fooBarEntity.lastName}</dd>
        </dl>
        <Button tag={Link} to="/foo-bar" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/foo-bar/${fooBarEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ fooBar }: IRootState) => ({
  fooBarEntity: fooBar.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FooBarDetail);
