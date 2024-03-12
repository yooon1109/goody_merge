import React, { useState, useEffect } from 'react';
import './collectionDetail.css';
import { useParams } from 'react-router-dom';
import { Dropdown, Space } from 'antd';
import { useNavigate } from 'react-router-dom';
import '../Review/font.css';


const token = localStorage.getItem('token');

function CollectionDetail() {

  const [isSliding, setIsSliding] = useState(false);
  const [isDescriptionVisible1, setIsDescriptionVisible1] = useState(true);
  const [isDescriptionVisible, setIsDescriptionVisible] = useState(false);
  const [marginTop, setMarginTop] = useState(0);
  const [collectionData, setCollectionData] = useState(null);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [liked, setLiked] = useState(null);
  const [likesCount, setLikesCount] = useState(0);

  const navigate = useNavigate();


  const { collectionId } = useParams();

  let apiURL = `/goody/collection/detail?collectionId=${collectionId}`;

  const headers = {
    Authorization: `${token}`,
  };
  const fetchData = async () => {
    try {
      const response = await fetch(apiURL, {
        method: 'GET',
        headers,
      });

      if (response.ok) {
        const data = await response.json();
        setCollectionData(data);
        setCurrentImageIndex(0);
        setLikesCount(data ? data.likeCount : 0);
        setLiked(data ? data.liked : 0);
        console.log(data);
      } else {
        console.error('An error occurred while fetching collection item list.');
      }
    } catch (error) {
      console.error('An error occurred:', error);
    }
  };

  const handleDeleteClick = async () => {
    try {
      const responseDel = await fetch(
        `/goody/collection/delete?collectionId=${collectionId}`,
        {
          method: 'DELETE',
          headers,
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

  const handleImageClick = () => {
    const newMarginTop = marginTop === 0 ? -225 : 0;
    setMarginTop(newMarginTop);

    setIsSliding(true);
    setIsDescriptionVisible1(isDescriptionVisible);
    setIsDescriptionVisible(!isDescriptionVisible);
  };

  const showPreviousImage = () => {
    if (currentImageIndex > 0) {
      setCurrentImageIndex(currentImageIndex - 1);
    }
  };

  const showNextImage = () => {
    if (currentImageIndex < collectionData.filePath.length - 1) {
      setCurrentImageIndex(currentImageIndex + 1);
    }
  };

  const handleLinkClick = () => {

    navigate('/addWrite',
      { state: { datatitle: collectionData.title, dataexplain: collectionData.explain } }); // '/collectionWrite'로 이동하도록 설정
  };

  const handleBack = () => {
    navigate(-1);
  }

  const handleModifyClick = () => {
    navigate('/collectionmodify',
      {
        state: {
          datatitle: collectionData.title, dataexplain: collectionData.explain,
          datahashtag: collectionData.hashTags, datadocumentid: collectionData.documentId
        }
      });
  }
  const items = [
    ...(collectionData && collectionData.myCollection === true
      ? [
        {
          label: '삭제',
          key: '1',
        },
      ]
      : []),

    ...(collectionData && collectionData.myCollection === true
      ? [
        {
          label: '수정',
          key: '2',
        },
      ]
      : []),

    {
      type: 'divider',
    },

    ...(collectionData && collectionData.myCollection === true
      ? [
        {
          label: '판매하기',
          key: '3',
        },] : []),
  ];


  const handleLike = async () => {
    try {
      const response = await fetch(`/goody/collection/addLike?documentId=${collectionId}`, {
        method: 'POST',
        headers,
      });


      if (response.ok) {
        setLiked(true);
        setLikesCount(await response.text());
      }

    } catch (error) {
      console.error('Error toggling like:', error);
    }
  };

  const handledeleteLike = async () => {
    try {
      const response = await fetch(`/goody/collection/removeLike?documentId=${collectionId}`, {
        method: 'POST',
        headers,
      });


      if (response.ok) {
        setLiked(false);
        setLikesCount(await response.text());
      }

    } catch (error) {
      console.error('Error toggling like:', error);
    }
  };


  useEffect(() => {
    fetchData();
    // 페이지 로드 시 스크롤 비활성화
    document.body.style.overflow = 'hidden';
    // 컴포넌트가 언마운트 될 때 스크롤 다시 활성화
    return () => {
      document.body.style.overflow = 'auto';
    };
  }, []);

  return (
    <>
      <div> {/*전체*/}
        <button className='absolute left-0 w-[10rem] h-[50rem]' onClick={showPreviousImage}></button> {/*이미지*/}
        <button className='absolute right-0 w-[10rem] h-[50rem]' onClick={showNextImage}> </button>{/*이미지*/}

        {/*이미지*/}
        <img
          src={collectionData && collectionData.filePath[currentImageIndex]}
          alt={`Image ${currentImageIndex}`}
          className='relative w-full h-[700px] bg-background-image -z-40 object-cover' />

        <div className='flex'>

          <div className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)'>
            <p className='absolute top-5 left-14 w-[5rem] h-[5rem] font-size text-xl text-yellow-500 text-extrabold 
        '> {likesCount} </p>
          </div>
          {liked ?
            <button className='absolute top-4 left-4 w-[2rem] h-[2rem]'
              onClick={handledeleteLike} >
              <img src='../img/yellowheart.png' alt='하트 꽉' />
            </button> :
            <button className='absolute top-4 left-4 w-[2rem] h-[2rem] drop-shadow-[0_2px_1px_rgba(220,166,19,100)]'
              onClick={handleLike} >
              <img src='../img/heart.png' alt='하트 안꽉' />
            </button>}



          <button onClick={handleBack}>
            <img src="../img/close.png" className='absolute top-4 right-4 w-[2rem] h-[2rem] drop-shadow-[0_2px_1px_rgba(220,166,19,100)]' />
          </button>

          <Dropdown
            menu={{
              items: items.map((item) => {
                if (item.key === '1') {
                  return { ...item, onClick: handleDeleteClick }; // key가 1인 경우 핸들러 1 연결
                }
                if (item.key === '2') {
                  return { ...item, onClick: handleModifyClick }; // key가 1인 경우 핸들러 1 연결
                }
                if (item.key === '3') {
                  return { ...item, onClick: handleLinkClick };
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
                <img src='../img/Icon_Info_White.png ' className="w-[1.9rem] h-[1.9rem] drop-shadow-[0_2px_1px_rgba(220,166,19,100)]" />
              </Space>
            </a>
          </Dropdown>

        </div>

        <div className="relative" style={{ marginTop: `${marginTop}px` }}> {/*아래 상세설명*/}
          <button className={`overflow-hidden absolute  -bottom-[33rem] w-full h-[600px] bg-white rounded-3xl justify-center flex z-50 
               ${isSliding ? 'transition duration-200 ease-in-out sliding ' : ''}`}

            style={{ marginTop: `${marginTop}px` }}
            onClick={handleImageClick}>

            <p className="text-3xl p-3  absolute text-center">
              {collectionData ? collectionData.title : 'Loading...'}
            </p>


            <div className="mt-[2.2rem] flex flex-col items-center p-5">
              <div className="mb-3">
                <p>
                  {collectionData ? new Date(collectionData.createdDate).toLocaleDateString() : 'Loading...'}
                </p>
              </div>

              <div>
                {collectionData && collectionData.hashTags ? (
                  <div className='text-center flex items-center justify-center '>
                    {collectionData.hashTags.map((tag, index) => (
                      <p className='px-3' key={index}>
                        # {tag}
                      </p>
                    ))}
                  </div>
                ) : (
                  <p />
                )}
              </div>
            </div>


            {isDescriptionVisible1 && (
              <p onClick={handleImageClick} />
            )}
            {isDescriptionVisible && (
              <div>
                <p className="left-[1.25rem] mt-[8rem] mr-[1.25rem] absolute whitespace-pre-line">
                  {collectionData ? collectionData.explain : 'Loading...'}
                </p>

              </div>
            )}


          </button>
        </div> {/*아래 상세 설명 끝*/}
      </div>

    </>
  );
}

export default CollectionDetail;
