import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FooBar from './foo-bar';
import FooBarDetail from './foo-bar-detail';
import FooBarUpdate from './foo-bar-update';
import FooBarDeleteDialog from './foo-bar-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FooBarUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FooBarUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FooBarDetail} />
      <ErrorBoundaryRoute path={match.url} component={FooBar} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FooBarDeleteDialog} />
  </>
);

export default Routes;
