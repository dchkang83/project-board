import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { useForm } from 'react-hook-form';

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';

import { setRefreshToken } from '~/utils/Cookie';
import { loginUser } from '~/api/Auth';
import { authActions } from '~/store/slices/authSlice';

function Copyright(props) {
  return (
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://github.com/dchkang83">
        Your Website
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

function Login() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // useForm 사용을 위한 선언
  const { register, setValue, formState: { errors }, handleSubmit } = useForm();

  // submit 이후 동작할 코드
  // 백으로 유저 정보 전달
  const onValid = async ({ username, password }) => {
    const response = await loginUser({ username, password });

    if (response.status) {
      console.log('response.jwtTokens : ', response.jwtTokens);

      // console.log('response.test : ', test);


      // 쿠키에 Refresh Token, store에 Access Token 저장
      /*
      setRefreshToken(response.json11.refresh_token);
      dispatch(authActions.setAccessToken(response.json11.access_token));
      */

      dispatch(authActions.setAccessToken(response.jwtTokens.access_token));
      setRefreshToken(response.jwtTokens.refresh_token);


      return navigate("/");
    } else {
      console.log(response.json11);
    }

    // input 태그 값 비워주는 코드
    setValue("password", "");
  };

  return (
    <>
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign in
        </Typography>
        <Box component="form" onSubmit={handleSubmit(onValid)} noValidate sx={{ mt: 1 }}>
          <TextField
            {...register("username", { required: "Please Enter Your Email" })}
            label="Email Address"
            margin="normal"
            required
            fullWidth
            id="email"
            name="email"
            autoComplete="email"
            autoFocus
            value={"customer@naver.com"}
          />
          <TextField
            {...register("password", { required: "Please Enter Your Password" })}

            label="Password"
            margin="normal"
            required
            fullWidth
            name="password"
            type="password"
            id="password"
            autoComplete="current-password"
            value={"test1234"}
          />
          <FormControlLabel
            label="Remember me"
            control={<Checkbox value="remember" color="primary" />}
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >Sign in</Button>

          <Grid container>
            <Grid item xs>
              <Link href="#" variant="body2">
                Forgot password?
              </Link>
            </Grid>
            <Grid item>
              <Link href="#" variant="body2">
                {"Sign Up"}
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
      <Copyright sx={{ mt: 8, mb: 4 }} />
    </>
  );
}

export default Login;