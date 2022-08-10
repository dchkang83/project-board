import { map } from 'ramda';
import { all, fork  } from "redux-saga/effects"

import signSaga from "~/store/sagas/sign/signSaga";

let combineSagas = {};
combineSagas = Object.assign(combineSagas, { signSaga });

export default function* rootSaga() {
    yield all(map(fork, combineSagas));
}