import { combineReducers } from 'redux';

import { authReducer } from '~/store/slices/authSlice';
// import { signReducer } from '~/store/slices/sign/signSlice';

const rootReducer = combineReducers({
  authReducer,
  // signReducer
});

export default rootReducer;