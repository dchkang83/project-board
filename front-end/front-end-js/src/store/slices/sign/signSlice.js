import { createSlice } from "@reduxjs/toolkit";

const name = "sign";

const initialState = {
  sign: {},
  status: 0,
  statusText: "Loading"
};

const reducers = {
  postSign: (state, action) => { },
  postSignSuccess: (state, action) => {
    // TODO. test started
    state.sign = action.payload?.data ?? [];
    // TODO. test eneded

  },
  postSignFail: (state, action) => { },

};

const signSlice = createSlice({
  name,
  initialState,
  reducers
});

export const signReducer = signSlice.reducer;
export const signActions = signSlice.actions;