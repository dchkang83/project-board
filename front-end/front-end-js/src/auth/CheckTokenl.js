import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getCookieToken, removeCookieToken } from '~/utils/Cookie';
import { requestToken } from '~/api/Users';

import { authActions } from '~/store/slices/authSlice';

export function CheckToken(key) {
  const [ isAuth, setIsAuth ] = useState('Loaded');
  const { authenticated, expireTime } = useSelector(state => state.authReducer);  
  const refreshToken = getCookieToken();
  const dispatch = useDispatch();

  useEffect(() => {
    const checkAuthToken = async () => {
      if (refreshToken === undefined) {
        // console.log('111');
        dispatch(authActions.DELETE_TOKEN());
        setIsAuth('Failed');
      } else {
        // console.log('222');
        if (authenticated && new Date().getTime() < expireTime) {
          setIsAuth('Success');
        } else {
          const response = await requestToken(refreshToken);

          if (response.status) {
            const token = response.json.access_token;
            dispatch(authActions.SET_TOKEN(token));
            setIsAuth('Success');
          } else {
            dispatch(authActions.DELETE_TOKEN());
            removeCookieToken();
            setIsAuth('Failed');
          }
        }
      }
    };
    checkAuthToken();
  }, [refreshToken, dispatch, key]);

  return {
    isAuth
  };
}