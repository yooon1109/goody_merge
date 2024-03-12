import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart as faHeartSolid } from '@fortawesome/free-solid-svg-icons';
import './WriteDetail.css';
import { useNavigate } from 'react-router-dom';
import Chip from '@mui/material/Chip';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import addChattingRoom from '../Chatting/addChattingRoom';
import { Dropdown, Space, message } from 'antd';
import { Modal } from 'antd';

const theme = createTheme({
  palette: {
    primary: {
      main: '#FFD52B',
    },
  },
});

function WriteDetail() {
  const [writeDetailData, setWriteDetailData] = useState({});
  const { documentId } = useParams();
  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');
  const navigate = useNavigate();

  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [liked, setLiked] = useState(false);
  const [myContents, setMyContents] = useState(false);
  const [nickname, setNickname] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const [isChatEntered, setIsChatEntered] = useState(false);
  const [grade, setGrade] = useState(false);
  const [profile, setProfile] = useState(false);
  const [sold, setSold] = useState(false);

  const fetchOptions = {
    headers: {
      Authorization: token,
      'Content-Type': 'application/json',
    },
  };

  const dataToSend = {
    documentId: documentId,
    liked: !liked,
  };

  const handleLikeClick = () => {
    setLiked(!liked);

    const likeEndpoint = liked
      ? `/goody/contents/removeLike?documentId=${documentId}`
      : `/goody/contents/addlike?documentId=${documentId}`;

    fetch(likeEndpoint, {
      method: 'POST',
      headers: fetchOptions.headers,
      body: JSON.stringify(dataToSend),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('HTTP 오류 ' + response.status);
        }
      })
      .catch((error) => {
        console.error('좋아요 처리 중 오류 발생:', error);
      });
  };


  const handleHome = () => {

    const addWriteURL = document.referrer;
    if (addWriteURL.includes('addWrite')) {
      navigate('/home');
    }
    else {
      navigate(-1);
    }
  };


  const handleAddChat = () => {
    Modal.confirm({
      title: '구매하기',
      content: '구매하시겠습니까? 채팅방으로 연결 됩니다.',
      okButtonProps: {
        type: "primary",
        style: { backgroundColor: '#FFD52B', color: 'black' },
      },

      onCancel: () => {
        // 취소 버튼 눌렀을 때 수행할 작업
      },
      onOk: async () => {
        try {
          await addChattingRoom({
            writerId: writeDetailData.writerId,
            documentId: documentId,
            token: token,
            userId: userId,
            title: writeDetailData.title
          });

        } catch (error) {
          console.error('Error adding chatting room:', error);
        }
      },
    });
  };


  const nextImage = () => {
    if (writeDetailData.imgPath && writeDetailData.imgPath.length > 0) {
      setCurrentImageIndex((currentImageIndex + 1) % writeDetailData.imgPath.length);
    }
  };

  const prevImage = () => {
    if (writeDetailData.imgPath && writeDetailData.imgPath.length > 0) {
      setCurrentImageIndex((currentImageIndex - 1 + writeDetailData.imgPath.length) % writeDetailData.imgPath.length);
    }
  };

  const items = [
    ...(myContents === true
      ? [
        {
          label: '삭제',
          key: '1',
        },
      ]
      : []),
  ];

  const handleDeleteClick = async () => {
    try {
      const responseDel = await fetch(
        `/goody/contents/delete?documentId=${documentId}`,
        {
          method: 'DELETE',
          headers: fetchOptions.headers,
        }
      );

      if (responseDel.ok) {
        console.log('삭제 성공');
        navigate(-1);
      } else {
        console.error('삭제 실패');
      }
    } catch (error) {
      console.error('오류 발생:', error);
    }
  };

  const warning = (content) => {
    messageApi.open({
      type: 'warning',
      content: content,
      duration: 2,
      style: {
        marginTop: '72vh',
      },
    });
  };


  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          `/goody/contents/detail?documentId=${documentId}`,
          {
            method: 'GET',
            headers: fetchOptions.headers,
          }
        );

        if (!response.ok) {
          throw Error('HTTP 오류 ' + response.status);
        }

        const data = await response.json();
        setWriteDetailData(data);
        setLiked(data.like || false);
        setMyContents(data.myContents || false);
        setNickname(data.nickname);
        setGrade(data.writerGrade);
        setProfile(data.profileImg);
        setSold(data.sold);
        setIsChatEntered(data.isChatEntered || false); // Assuming there's a property isChatEntered in your API response
        console.log(data);
      } catch (error) {
        console.error('API에서 데이터를 가져오는 중 오류 발생:', error);
      }
    };

    fetchData();
  }, [documentId]);

  const isFirstImage = currentImageIndex === 0;
  const isLastImage = writeDetailData.imgPath && currentImageIndex === writeDetailData.imgPath.length - 1;

  return (
    <ThemeProvider theme={theme}>
      <div>
        {writeDetailData.imgPath && writeDetailData.imgPath.length > 0 && (
          <div className='relative w-full'>
            <div className='absolute right-0 p-4'>
              <button onClick={handleHome}>
                <img src="/img/close.png" alt="닫기" className="w-[1.9rem] h-[1.9rem] drop-shadow-[0_2px_1px_rgba(220,166,19,100)]" />
              </button>
            </div>

            {writeDetailData.myContents && (
              <Dropdown
                menu={{
                  items: items.map((item) => {
                    if (item.key === '1') {
                      return { ...item, onClick: handleDeleteClick }; // key가 1인 경우 핸들러 1 연결
                    }
                    return item;
                  }),
                }}
                trigger={['click']}
                style={{ border: '1px solid #000', width: '23px', height: '23px' }}
                className='absolute top-4 right-14'
              >
                <a onClick={(e) => e.preventDefault()}>
                  <Space>
                    <img src='../img/Icon_Info_White.png' className="w-[1.8rem] h-[1.8rem] drop-shadow-[0_2px_1px_rgba(220,166,19,100)]" />
                  </Space>
                </a>
              </Dropdown>
            )}

            {writeDetailData.imgPath.length > 1 && (
              <>
                <div className="flex absolute top-56 left-0 pl-3">
                  {!isFirstImage && <button onClick={prevImage} className='nav_button'>&lt;</button>}
                </div>
                <div className="flex absolute top-56 right-0 pr-3">
                  {!isLastImage && <button onClick={nextImage} className='nav_button'>&gt;</button>}
                </div>
              </>
            )}

            <div>
              <img src={writeDetailData.imgPath[currentImageIndex]} alt="상세 이미지" className="sliding-image" />
            </div>
          </div>
        )}

        {/* 아이디 부분 */}
        <div className=''>
          <div className='flex'>
            <div className='flex mt-5 ml-5'>
              <div className='flex w-full'>
                <img src={profile} className="rounded-full border" style={{ width: '3rem', height: '3rem' }}></img>
             
              <div className='ml-2'>
                <div className=''>
                  <label className='font-bold'> {nickname} </label>
                </div>
                <div>
                  <label className='text-xs'>{grade}</label>
                </div> </div>
              </div>
            </div>
          </div>

          {/* 제목 */}
          <div className='m-5 mt-[1.5rem] ml-[1rem]'>
            <label className='ml-[0.5rem] font-bold'>{writeDetailData?.title}</label>
          </div>

          {/* 카테고리 */}
          <div className='mt-5 ml-5 w-300 flex'>
            <Chip
              label={writeDetailData?.category}
              color="primary"
              size="medium"
              className='mr-5'
            />
            <Chip
              label={writeDetailData?.transType}
              color="primary"
              size="medium"
            />
          </div>

          {/* 설명 */}
          <div className='m-10 mt-[1.5rem] ml-[1rem] mb-32'>
            <label className='ml-[0.5rem] text-[#565656]'>{writeDetailData?.explain}</label>
          </div>

          <br />

          {writeDetailData?.transType === '같이해요' && writeDetailData?.people && (
            <div className="m-4">
              <h2 className="text-sm font-bold ml-3">품목 리스트</h2>
              <div>
                {writeDetailData.people.map((person, index) => (
                  <Chip
                    key={index}
                    label={person}
                    variant="outlined"
                    style={{ margin: '0.5rem' }}
                  />
                ))}
              </div>
              {writeDetailData.people.length > 0 && <div style={{ marginBottom: '6rem' }}></div>}
            </div>
          )}

          {/* 가격 및 구매하기 */}
          <div className='bottom-0 fixed w-full'>
            <div className='flex justify-center bg-[#f8f8f8] h-15 p-2'>
              <FontAwesomeIcon
                icon={faHeartSolid}
                className={`heart-icon ${liked ? 'text-color' : ''} p-3`}
                size="lg"
                onClick={handleLikeClick} />

              <div className='p-3 pl-5 font-semibold text-sm'>
                <label>{writeDetailData.price}원</label>
              </div>
              <div className='flex ml-auto mr-[0.5rem]'>
                <div className='flex ml-auto mr-[0.5rem]'>
                  <button
                    onClick={() => {
                      if (writeDetailData.writerId === userId) {
                        warning("내 글입니다.");
                      } else if (isChatEntered) {
                        window.location.href = `/chatdetails/${userId}-${documentId}?contentsId=${documentId}`;
                      } else if (sold) {
                        warning("판매 되었습니다.");
                      }
                        else {
                        handleAddChat();
                      }
                    }}
                    className={`${
                      sold
                        ? 'bg-gray-400 w-[6.5rem] h-[2.2rem] right-0 mt-[0rem] font-bold rounded-xl content-center text-white'
                        : 'bg-[#FFD52B] w-[6.5rem] h-[2.2rem] right-0 mt-[0rem] font-bold rounded-xl content-center'
                    }`}
                    >
                      {sold ? '판매완료' : isChatEntered ? '채팅방가기' : '구매하기'}
                  </button>
                </div>

              </div>
            </div>
          </div>
        </div>
      </div>

      <div >
        {contextHolder}
      </div>
    </ThemeProvider>
  );
}


export default WriteDetail;
