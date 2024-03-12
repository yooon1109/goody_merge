import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
// import './font.css';
import AppBar from '@mui/material/AppBar';
import TextField from '@mui/material/TextField';
import { useNavigate } from 'react-router-dom';


const token = localStorage.getItem('token');

const Chatting = () => {
 
  const { roomId } = useParams();
  const [ info, setInfo ] = useState(null);
  const navigate = useNavigate();

  let apiURL = `/goody/addressInfo?roomId=${roomId}`;
  const headers = {
    Authorization: `${token}`,
  };

  const handleBack = () => {
    navigate(-1); // 이전 페이지로 이동하는 함수
  };
  
  const handleChange = (e, fieldName) => {
    const updatedInfo = { ...info };
    updatedInfo[fieldName] = e.target.value;
    setInfo(updatedInfo);
  };

  const fetchData = async () => {
    try {
      const response = await fetch(apiURL, {
        method: 'GET',
        headers,
      });

      if (response.ok) {
        const data = await response.json();
        setInfo(data);
      } else {
        console.error('An error occurred while fetching.');
      }
    } catch (error) {
      console.error('An error occurred:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // 데이터 로딩 중
  if (info === null) {
    return <div>Loading...</div>;
  }
  console.log(info);

  const handleAccountButtonClick = async () => {
    const response = await fetch(`/goody/updateAccount?accountNum=${info.MyAccountNum}&accountBank=${info.MyAccountBank}`, {
      method: 'POST',
      headers,
    });
    if(response.ok){
      console.log("수정성공");
    }
  }

  const handleAddressButtonClick = async () => {
    const response = await fetch(`/goody/updateAddress?address=${info.MyAddress}`, {
      method: 'POST',
      headers,
    });
    if(response.ok){
      console.log("주소수정성공");
    }
  }

  return (
    <>
     <AppBar component="nav" className='fixed top-0 w-full'>
          <img src='../../img/ActionBar.png' className='absolute' alt="ActionBar"></img>
          <p id="actionBar_name" className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] font-bold text-white p-6 ml-2 text-xl absolute '>주소 & 계좌번호</p>
          <button className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] absolute top-5 right-4 h-full' onClick={handleBack}>
              <img src="../../img/close.png" alt='닫기' width={'30px'} height={'30px'} />
            </button>
      </AppBar>
      <div className='pb-20 pt-10'/>

      <div className="flex justify-center pl-5 pr-5">
  <div className="flex flex-col">
    <div style={{ width: '400px' ,justifyContent: 'center'}} className="mx-auto flex items-center border pt-5 pb-5 mb-10 rounded-lg shadow-lg">
      <div className='m-3 flex items-center justify-center'>
        <div className='flex flex-col items-center'>
          {/* <img src="img/profile.png" alt="프로필사진" style={{ height: '4rem', width: '4rem' }} className="rounded-full border" /> */}
          <p style={{ width: '5em', textAlign: 'left'}} className='pt-1 font-semibold'>{info.role === 'buyer' ? '판매자 계좌' : '내 계좌'}</p>
        </div>
      </div>

      <div className='flex flex-col items-center'>
        {info.role === 'buyer' ? (
          <>
          <TextField
              id="outlined-read-only-input"
              value={info.sellerAccountBank || ''}
              placeholder="판매자 계좌 은행"
              InputProps={{
                readOnly: true,
              }}
              sx={{ "& .MuiOutlinedInput-root": { "& > fieldset": { border: "none" } }, width:'17em' }}//테두리제거
            />
            <TextField
              id="outlined-read-only-input"
              value={info.sellerAccountNum || ''}
              placeholder="판매자 계좌 번호"
              InputProps={{
                readOnly: true,
              }}
              sx={{ "& .MuiOutlinedInput-root": { "& > fieldset": { border: "none" } }, width:'17em' }}//테두리제거
            />
            
          </>
        ) : (
          <>
            <TextField
              id="outlined-basic"
              variant="outlined"
              value={info.MyAccountNum || ''}
              onChange={(e) => handleChange(e, 'MyAccountNum')}
              sx={{width:'17rem'}}
            />
            <TextField
              id="outlined-basic"
              variant="outlined"
              value={info.MyAccountBank || ''}
              onChange={(e) => handleChange(e, 'MyAccountBank')}
              sx={{width:'17rem'}}
            />
            
          </>
        )}
        {info.role === 'buyer' ? null : (
          <button className='mt-1 mb-1 border text-gray-500 pl-1 ' 
                style={{ height: '2rem', width: '17rem', fontSize: '18px', borderRadius: '10px 0 10px 10px', backgroundColor: '#FFF2C6' }}
                onClick={handleAccountButtonClick}>
            수정
          </button>
        )}
      </div>
    </div>

    <div style={{ width: '400px', justifyContent: 'center' }} className="mx-auto flex items-center border pt-5 pb-5 mb-10 rounded-lg shadow-lg">
      <div className='m-3 flex items-center justify-center'>
        <div className='flex flex-col items-center'>
          {/* <img src="img/profile.png" alt="프로필사진" style={{ height: '4rem', width: '4rem' }} className="rounded-full border" /> */}
          <p style={{ width: '5em', textAlign: 'left'}} className='pt-1 font-semibold'>{info.role === 'buyer' ? '내 주소' : '구매자 주소'}</p>
        </div>
      </div>

      <div className='flex flex-col items-center'>
        {info.role === 'buyer' ? (
          <>
            <TextField
              id="outlined-basic"
              placeholder="주소를 입력해주세요"
              variant="outlined"
              value={info.MyAddress || ''}
              onChange={(e) => handleChange(e, 'MyAddress')}
              sx={{width:'17rem'}}
            />
            <button className='mt-1 mb-1 border text-gray-500 pl-1 ' 
                    style={{ height: '2rem', width: '17rem', fontSize: '18px', borderRadius: '10px 0 10px 10px', backgroundColor: '#FFF2C6' }}
                    onClick={handleAddressButtonClick}>
                수정
            </button>
          </>
        ) : (
          <>
            {info.role === 'seller' && (
              <>
                {Object.entries(info.address).map(([key, value]) => (
                  <TextField
                    key={key}
                    label={`${key} 주소`}
                    value={value}
                    multiline
                    InputProps={{
                      readOnly: true,
                    }}
                    sx={{ width: '17em',  "& .MuiOutlinedInput-root": { "& > fieldset": { border: "none" } }  }}
                  />
                ))}
              </>
            )}
          </>
        )}
       
      </div>
    </div>
  </div>
</div>
     
    </>
  );
};

export default Chatting;