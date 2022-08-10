import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "http://127.0.0.1:8080/",
    headers: {
      "withCredentials": true,  // axios에 withCredentials를 true로 설정해줘야 refreshToken cookie를 주고받을 수 있다.
      "Content-Type": "application/json",
      "Authorization": localStorage.getItem('login-token')
    },
    timeout: 3000,
});

// API 요청하는 콜마다 헤더에 accessToken 담아 보내도록 설정
// axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

export default axiosInstance;