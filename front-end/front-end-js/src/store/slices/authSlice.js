import { createSlice } from '@reduxjs/toolkit';

export const TOKEN_TIME_OUT = 600 * 1000;

export const authSlice = createSlice({
  name: 'authToken',
  initialState: {
    authenticated: false,
    accessToken: null,
    expireTime: null
  },
  reducers: {
    setAccessToken: (state, action) => {


      console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> setAccessToken');
      console.log(action);

      state.authenticated = true;
      state.accessToken = action.payload;
      state.expireTime = new Date().getTime() + TOKEN_TIME_OUT;


      console.log('state.accessToken : ', state.accessToken);

    },
    delAccessToken: (state) => {
      state.authenticated = false;
      state.accessToken = null;
      state.expireTime = null
    },
  }
})

// export default tokenSlice.reducer;

export const authReducer = authSlice.reducer;
export const authActions = authSlice.actions;