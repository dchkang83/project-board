import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getCookieToken, removeCookieToken } from '~/utils/Cookie';
import { requestToken } from '~/api/Auth';
import { setRefreshToken } from '~/utils/Cookie';

import { authActions } from '~/store/slices/authSlice';

export function CheckToken(key) {
  const [ isAuth, setIsAuth ] = useState('Loaded');
  const { authenticated, accessToken, expireTime } = useSelector(state => state.authReducer);  
  const refreshToken = getCookieToken();
  const dispatch = useDispatch();
  
  useEffect(() => {
    const checkAuthToken = async () => {
      if (refreshToken === undefined) {
        dispatch(authActions.delAccessToken());
        setIsAuth('Failed');
      } else {
        setIsAuth('Success');

        if (authenticated && new Date().getTime() < expireTime) {
          setIsAuth('Success');
        } else {
          const response = await requestToken(refreshToken);

          if (response.status) {
            const token = response.json.access_token;
            dispatch(authActions.setAccessToken(response.jwtTokens.access_token));
            setRefreshToken(response.jwtTokens.refresh_token);

            setIsAuth('Success');
          } else {
            dispatch(authActions.delAccessToken());
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