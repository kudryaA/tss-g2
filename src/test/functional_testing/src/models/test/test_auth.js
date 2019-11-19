const FormData = require('form-data');

exports.testAuth = async (fetch, configration, result = []) => {
  const { host } = configration;
  const login = `test_login_${new Date().getTime()}`;
  const password = 'test_password';

  const reg_1 = await registration(fetch, host, login, password);
  result.push({
    action: `Registation user`,
    status: reg_1
  });
  if (reg_1) {
    result.push({
      action: `Failed in registation exists user`,
      status: !await registration(fetch, host, login, password)
    });
    result.push({
      action: 'Logout',
      status: await logout(fetch, host)
    });
    const resLogin = await loginAction(fetch, host, login, password);
    result.push({
      action: `Login user`,
      status: resLogin
    });
    if (resLogin) {
      const sessionRes = await session(fetch, host);
      result.push({
        action: `Show user information`,
        status: sessionRes.status && sessionRes.obj.login === login
      });
    }
  }
  const loginModerator = await loginModeratorAction(fetch, host, 'a', 'a');
  result.push({
    action: `Login moderator`,
    status: loginModerator
  });
  if (loginModerator) {
    await changeStatus(fetch, host, login);
    const loginBan = !(await loginAction(fetch, host, login, password));
    result.push({
      action: `Ban`,
      status: loginBan
    });
    if (loginBan) {
      await loginModeratorAction(fetch, host, 'a', 'a');
      result.push({
        action: `Login moderator`,
        status: loginModerator
      });
      await changeStatus(fetch, host, login);
      result.push({
        action: `Unban`,
        status: (await loginAction(fetch, host, login, password))
      });
    }
  }
}

async function changeStatus(fetch, host, login) {
  const formData = new FormData();
  formData.append('userLogin', login);
  const answer = await (fetch(`${host}/change/status`, {
    method: 'POST',
    body: formData
  }));
  await (answer).json();
}

async function loginAction(fetch, host, login, password) {
  const formData = new FormData();
  formData.append('login', login);
  formData.append('password', password);
  const answer = await (fetch(`${host}/login`, {
    method: 'POST',
    body: formData
  }));
  const res = await (answer).json();
  return res.obj;
}

async function loginModeratorAction(fetch, host, login, password) {
  const formData = new FormData();
  formData.append('login', login);
  formData.append('password', password);
  const answer = await (fetch(`${host}/login/moderator`, {
    method: 'POST',
    body: formData
  }));
  const res = await (answer).json();
  return res.obj;
}

async function registration(fetch, host, login, password) {
  const formData = new FormData();
  formData.append('login', login);
  formData.append('password', password);
  const answer = await (fetch(`${host}/registration`, {
    method: 'POST',
    body: formData
  }));
  const res = await (answer).json();
  return res.obj;
}

async function session(fetch, host) {
  const answer = await (fetch(`${host}/session`, {
    method: 'POST'
  }));
  const res = await (answer).json();
  return res;
}

async function logout(fetch, host) {
  const answer = await (fetch(`${host}/logout`, {
    method: 'POST'
  }));
  const res = await (answer).json();
  return res;
}

