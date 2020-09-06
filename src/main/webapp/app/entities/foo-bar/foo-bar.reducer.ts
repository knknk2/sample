import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFooBar, defaultValue } from 'app/shared/model/foo-bar.model';

export const ACTION_TYPES = {
  FETCH_FOOBAR_LIST: 'fooBar/FETCH_FOOBAR_LIST',
  FETCH_FOOBAR: 'fooBar/FETCH_FOOBAR',
  CREATE_FOOBAR: 'fooBar/CREATE_FOOBAR',
  UPDATE_FOOBAR: 'fooBar/UPDATE_FOOBAR',
  DELETE_FOOBAR: 'fooBar/DELETE_FOOBAR',
  RESET: 'fooBar/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFooBar>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FooBarState = Readonly<typeof initialState>;

// Reducer

export default (state: FooBarState = initialState, action): FooBarState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FOOBAR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FOOBAR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FOOBAR):
    case REQUEST(ACTION_TYPES.UPDATE_FOOBAR):
    case REQUEST(ACTION_TYPES.DELETE_FOOBAR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FOOBAR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FOOBAR):
    case FAILURE(ACTION_TYPES.CREATE_FOOBAR):
    case FAILURE(ACTION_TYPES.UPDATE_FOOBAR):
    case FAILURE(ACTION_TYPES.DELETE_FOOBAR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FOOBAR_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_FOOBAR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FOOBAR):
    case SUCCESS(ACTION_TYPES.UPDATE_FOOBAR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FOOBAR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/foo-bars';

// Actions

export const getEntities: ICrudGetAllAction<IFooBar> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FOOBAR_LIST,
    payload: axios.get<IFooBar>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFooBar> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FOOBAR,
    payload: axios.get<IFooBar>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFooBar> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FOOBAR,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IFooBar> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FOOBAR,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFooBar> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FOOBAR,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
