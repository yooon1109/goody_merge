import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const Login = () => {
  const [userId, setUserId] = useState('');
  const [userPw, setUserPw] = useState('');
  const [formError, setFormError] = useState('');


  useEffect(() => {
    // 스택 추가
    history.pushState(null, null, location.href);

    // 뒤로가기 이벤트 감지 -> 현재 페이지로 이동
    const handleBack = () => {
      history.go(1);
    };

    // popstate 이벤트 리스너(뒤로가기 버튼)를 연결합니다
    window.onpopstate = handleBack;

    // 정리: 컴포넌트가 언마운트될 때 이벤트 리스너를 제거합니다
    return () => {
      window.removeEventListener('popstate', handleBack);
    };
  }, []);


  const handleLogin = async (e) => {
    e.preventDefault();

    if (!userId || !userPw) {
      setFormError('아이디와 비밀번호를 입력해주세요.');
      return;
    }

    setFormError('');

    const user = {
      userId: userId,
      userPw: userPw,
    };

    const requestOptions = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(user),
    };

    try {
      const response = await fetch('/goody/user/login', requestOptions);

      if (response.ok) {
        const accessToken = response.headers.get('Authorization');
        if (accessToken) {
          // 토큰을 localStorage에 저장
          localStorage.setItem('token', accessToken);
          localStorage.setItem('userId', userId);

          
          // 로그인 성공 시 홈페이지로 이동
          window.location.href = '/home';
        } else {
          console.error('로그인 실패');
          setFormError('아이디 또는 비밀번호가 올바르지 않습니다.');
        }
      } else {
        console.error('로그인 요청 중 오류 발생', response);
        setFormError('아이디 또는 비밀번호가 올바르지 않습니다.');
      }
    } catch (error) {
      console.error('로그인 요청 중 오류 발생', error);
      setFormError('로그인 요청 중 오류가 발생했습니다.');
    }
  };

  return (
    <div>
      <img className='relative' src='img/LoginActionBar.png' alt='구디' />
      <p className='font-bold text-center text-xl mb-10'> 로그인</p>

      <form onSubmit={handleLogin} method="POST" action="/login">
        <div className='text-center'>
          <p className='text-left ml-14 font-bold p-2'> 이름 </p>
          <input
            type={'text'}
            className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[18rem] h-12 pl-4'
            placeholder='hongildong'
            name='userId'
            onChange={(e) => setUserId(e.target.value)}
          ></input>
          <p className='text-left ml-14 font-bold p-2'> 비밀번호 </p>
          <input
            type={'password'}
            className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)]  rounded-lg w-[18rem] h-12 pl-4'
            placeholder='********'
            name='userPw'
            onChange={(e) => setUserPw(e.target.value)}
          ></input>
        </div>

        {formError && <div className="text-red-500 text-center">{formError}</div>}

        <div className='flex justify-center'>
          <button type={'submit'} className='mt-5 w-[18rem] mb-5'>
            <img src='img/LoginButton.png' alt='Login' />
          </button>
        </div>
      </form>

      <div className='text-center mt-5'>
        <div className='flex justify-center space-x-2'>
          <Link to="/join"><p> 회원가입 </p></Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
