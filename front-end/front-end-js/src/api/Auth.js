const TIME_OUT = 300 * 1000;

const statusError = {
  status: false,
  json: {
    error: ["연결이 원활하지 않습니다. 잠시 후 다시 시도해 주세요"]
  }
};

const requestPromise = (url, option) => {
  return fetch(url, option);
};

const timeoutPromise = () => {
  return new Promise((_, reject) => setTimeout(() => reject(new Error('timeout')), TIME_OUT));
};

const getPromise = async (url, option) => {
  return await Promise.race([
    requestPromise(`http://localhost:8080${url}`, option),
    timeoutPromise()
  ]);
};

export const loginUser = async (credentials) => {
  const option = {
    method: 'POST',
    headers: {
      // 'Content-Type': 'application/json;charset=UTF-8'
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(credentials)
  };

  const data = await getPromise('/login', option).catch(() => {
    return statusError;
  });

  if (parseInt(Number(data.status) / 100) === 2) {
    const status = data.ok;
    const code = data.status;
    const text = await data.text();
    const json11 = text.length ? JSON.parse(text) : "";


    console.log('response.headers.authorization : ', data.headers.get('authorization'));
    for (let header of data.headers.entries()) {
      console.log(header);
    }

    const jwtTokens = {
      access_token: data.headers.get('authorization'),
      refresh_token: data.headers.get('refreshtoken'),
    };


    return {
      status,
      code,
      json11,
      jwtTokens
    };
  } else {
    return statusError;
  }
};

export const logoutUser = async (credentials, accessToken) => {
  const option = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    body: JSON.stringify(credentials)
  };

  const data = await getPromise('/logout-url', option).catch(() => {
    return statusError;
  });

  if (parseInt(Number(data.status) / 100) === 2) {
    const status = data.ok;
    const code = data.status;
    const text = await data.text();
    const json = text.length ? JSON.parse(text) : "";

    return {
      status,
      code,
      json
    };
  } else {
    return statusError;
  }
}

export const requestToken = async (refreshToken) => {


  console.log('################ requestToken #################');
  // console.log('accessToken : ', accessToken);
  console.log('refreshToken : ', refreshToken);
  const option = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      // 'X-ACCESS-TOKEN': accessToken,
      'X-REFRESH-TOKEN': refreshToken
    },
    // body: JSON.stringify({ refresh_token: refreshToken })
  }

  const data = await getPromise('/api/v1/auth/refresh', option).catch(() => {
    return statusError;
  });

  if (parseInt(Number(data.status) / 100) === 2) {
    const status = data.ok;
    const code = data.status;
    const text = await data.text();
    const json = text.length ? JSON.parse(text) : "";
    
    const jwtTokens = {
      access_token: data.headers.get('authorization'),
      refresh_token: data.headers.get('refreshtoken'),
    };


    return {
      status,
      code,
      json,
      jwtTokens
    };
  } else {
    return statusError;
  }
};
