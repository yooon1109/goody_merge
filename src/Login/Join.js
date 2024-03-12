import React, { useState } from 'react';
import { Link } from 'react-router-dom';

function Join() {

  const [formData, setFormData] = useState({
    userId: '',
    userPw: '',
    confirmPassword: '',
    userName: '',
    userPhoneNum: '',
    nickname: '',
  });


  const [formErrors, setFormErrors] = useState({
    userId: '',
    userPw: '',
    confirmPassword: '',
    userName: '',
    userPhoneNum: '',
    nickname: '',
  });


  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });

    setFormErrors({
      ...formErrors,
      [name]: '',
    });
  };

  // 아이디 중복
  const [data, setData] = useState({
    available: false,
  });

  const handleCheckId = async () => {

    try {
      const response = await fetch(`/goody/user/join/checkId?userId=${formData.userId}`);
      const fetchedData = await response.json();

      console.log(fetchedData);
      setData(fetchedData);

      if (fetchedData === false) {
        setFormErrors({
          ...formErrors,
          userId: '이미 사용 중인 아이디입니다.',
        });
      } else {
        setFormErrors({
          ...formErrors,
          userId: '아이디 사용 가능합니다.',
        });
      }
    } catch (error) {
      console.error('아이디 중복 확인 중 오류 발생:', error);
    }
  };

  // 아이디 유효성
  const handleSubmit = async (e) => {
    e.preventDefault();

    const errors = {};
    if (!formData.userId) {
      errors.userId = '아이디를 입력해주세요.';
    }
    if (!formData.userPw) {
      errors.userPw = '비밀번호를 입력해주세요.';
    }
    if (formData.userPw !== formData.confirmPassword) {
      errors.confirmPassword = '비밀번호가 일치하지 않습니다.';
    }
    if (!formData.userName) {
      errors.userName = '이름을 입력해주세요.';
    }
    if (!formData.nickname) {
      errors.nickname = '닉네임을 입력해주세요.';
    }
    if (!formData.userPhoneNum || formData.userPhoneNum.trim() === '') {
      errors.userPhoneNum = '전화번호를 입력해주세요.';
    } else if (!/^\d{3}-\d{4}-\d{4}$/.test(formData.userPhoneNum)) {
      errors.userPhoneNum = (
        <div>
          올바른 전화번호 형식이 아닙니다.
          <br />
          (예: 010-0000-0000)
        </div>
      );

    }

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      return;
    }

    try {
      const response = await fetch('/goody/user/join', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
        redirect: 'follow',
      });

      if (response.ok) {
        console.log('회원가입 성공');
        window.location.href = '/';
      } else {
        console.error('회원가입 실패:', response.status, response.statusText);
      }
    } catch (error) {
      console.error('회원가입 실패:', error);
    }
  };


  return (
    <div>
      <img className='relative' src='img/LoginActionBar.png' alt='구디' />
      <p className='font-bold text-center text-xl mb-10'> 회원가입</p>

      <form onSubmit={handleSubmit}>
        <div className='text-center'>
          <p className='text-left ml-14 font-bold p-2'> 아이디 </p>
          <div className="flex justify-center">
            <div className="flex">
              <input
                type="text"
                name="userId"
                className="shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[16rem] h-12 pl-4"
                placeholder="hongildong"
                value={formData.userId}
                onChange={handleChange}
              />
              <button type="button" onClick={handleCheckId} className="shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg">
                확인
              </button>
            </div>

          </div>

          {formErrors.userId && <div className={`text-${data.available ? 'blue' : 'red'}-500`}>{formErrors.userId}</div>}

          <p className='text-left ml-14 font-bold p-2' > 비밀번호 </p>
          <input
            type={'password'}
            name='userPw'
            className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[18rem] h-12 pl-4'
            placeholder='********'
            value={formData.userPw}
            onChange={handleChange}
          ></input>

          {formErrors.userPw && <div className="text-red-500">{formErrors.userPw}</div>}

          <p className='text-left ml-14 font-bold p-2' > 비밀번호 재확인 </p>
          <input
            type={'password'}
            name='confirmPassword'
            className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[18rem] h-12 pl-4'
            placeholder='********'
            value={formData.confirmPassword}
            onChange={handleChange}
          ></input>

          {formErrors.confirmPassword && <div className="text-red-500">{formErrors.confirmPassword}</div>}

          <p className='text-left ml-14 font-bold p-2' > 이름 </p>
          <input
            type={'text'}
            name='userName'
            className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[18rem] h-12 pl-4'
            placeholder='홍길동'
            value={formData.userName}
            onChange={handleChange}
          ></input>

          {formErrors.userName && <div className="text-red-500">{formErrors.userName}</div>}

          <p className='text-left ml-14 font-bold p-2' > 닉네임 </p>
          <input
            type={'text'}
            name='nickname'
            className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[18rem] h-12 pl-4'
            placeholder='gdong'
            value={formData.nickname}
            onChange={handleChange}
          ></input>

          {formErrors.nickname && <div className="text-red-500">{formErrors.nickname}</div>}

          <p className='text-left ml-14 font-bold p-2' > 전화번호 </p>
          <input
            type={'text'}
            name='userPhoneNum'
            className='shadow-[0_0_4px_0_rgba(174,174,174,0.7)] rounded-lg w-[18rem] h-12 pl-4'
            placeholder='010-0000-0000'
            value={formData.userPhoneNum}
            onChange={handleChange}
          ></input>

          {formErrors.userPhoneNum && <div className="text-red-500">{formErrors.userPhoneNum}</div>}

        </div>

        <div className='flex justify-center'>
          <button type='submit' className='mt-10 w-[18rem]'>
            <img src='img/JoinButton.png' alt='Join' />
          </button>
        </div>
      </form>

      <div className='text-center mt-5 mb-12'>
        <div className='flex justify-center space-x-2'>
          <Link to="/"><p> 로그인 </p></Link>
        </div>
      </div>
    </div>
  );

}

export default Join;