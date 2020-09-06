import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './foo-bar.reducer';
import { IFooBar } from 'app/shared/model/foo-bar.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFooBarUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FooBarUpdate = (props: IFooBarUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { fooBarEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/foo-bar');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...fooBarEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sampleApp.fooBar.home.createOrEditLabel">
            <Translate contentKey="sampleApp.fooBar.home.createOrEditLabel">Create or edit a FooBar</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : fooBarEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="foo-bar-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="foo-bar-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="firstNameLabel" for="foo-bar-firstName">
                  <Translate contentKey="sampleApp.fooBar.firstName">First Name</Translate>
                </Label>
                <AvField id="foo-bar-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="foo-bar-lastName">
                  <Translate contentKey="sampleApp.fooBar.lastName">Last Name</Translate>
                </Label>
                <AvField id="foo-bar-lastName" type="text" name="lastName" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/foo-bar" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  fooBarEntity: storeState.fooBar.entity,
  loading: storeState.fooBar.loading,
  updating: storeState.fooBar.updating,
  updateSuccess: storeState.fooBar.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FooBarUpdate);
