import { useEffect } from 'react';
import { useNavigate } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import { getCookieToken, removeCookieToken } from '~/utils/Cookie';
// import { authActions } from '~/store/Auth';
import { authActions } from '~/store/slices/authSlice';
import { logoutUser } from '~/api/Auth';

function Logout() {
    const { accessToken } = useSelector(state => state.token);

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const refreshToken = getCookieToken();

    async function logout() {
        const data = await logoutUser({ refresh_token: refreshToken }, accessToken);

        if (data.status) {
            dispatch(authActions.delAccessToken());
            removeCookieToken();
            return navigate('/login');
        } else {
            window.location.reload();
        }
    }
    useEffect( () => {
        logout();
    }, [])

    return (
        <>
            <Link to="/login" />
        </>
    );
}

export default Logout;