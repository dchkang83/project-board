// import { all, call, fork, getContext, put, take } from 'redux-saga/effects';
// import { all, call, fork, put, take } from 'redux-saga/effects';
import { all, call, retry, fork, put, take } from 'redux-saga/effects';

import qs from "query-string";

import { signActions } from '~/store/slices/sign/signSlice';
import axios from '~/utils/axios';

const SECOND = 1000;

function apiPostSign(email, password) {
  console.log(`apiPostSign email : ${email}`);
  console.log(`apiPostSign password : ${password}`);

  email = 'user1';
  password = '1234';

  const data = {
    username: email,
    password: password
  };


  // const headers = {
  //   withCredentials: true,
  //   headers: {
  //     "Accept": "application/json",
  //     "Content-Type": "application/json"
  //   }
  // };
  // const headers = { 
  //   'Authorization': 'Bearer my-token',
  //   'My-Custom-Header': 'foobar'
  // };
  const headers = {
    "Accept": "application/json",
    "Content-Type": "application/json"
  };

  // axios.post('/login', data)

  axios.post('/login', data, { headers })
    .then(function (response) {
      if (response.headers.authorization) {
        localStorage.setItem('login-token', response.headers.authorization);
      }
    }).catch(function (error) {
      console.log('Error on Authentication : ', error);
    });



  // axios.post('/login', {
  //   headers: { 'Authorization': + basicAuth }
  // }).then(function(response) {
  //   console.log('Authenticated');
  // }).catch(function(error) {
  //   console.log('Error on Authentication');
  // });


  // return axios.post(`/login/${boardId}`);
}

function* asyncPostSign(action) {
  try {
    // TODO. test
    // console.log('action.payload : ', action.payload);

      const response = yield call(apiPostSign, action.payload);
      if (response?.status === 200) {
          yield put(signActions.postSignSuccess(response));
      } else {
          yield put(signActions.postSignFail(response));
      }
  } catch(e) {
      console.error(e);
      yield put(signActions.postSignFail(e.response));
  }
}

function* watchPostSign() {
  while(true) {
      const action = yield take(signActions.postSign);
      yield call(asyncPostSign, action);
  }
}

export default function* signSaga() {
  yield all([
    fork(watchPostSign)
  ]);
}