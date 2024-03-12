import React, { useState, useEffect } from 'react';
import { ActionBarClose } from '../Component/ActionBarClose';
import { Item_KeywordReview } from '../Component/Item_KeywordReview';
import { Empty } from 'antd';

const actionBarName = "리뷰 목록";

const ReviewList = () => {
  const [userInfo3, setUserInfo3] = useState([]);
  const [loading, setLoading] = useState(true);
  const [profile, setProfile] = useState('');
  const [nickname, setNickname] = useState(false);
  const [grade, setGrade] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (!token) {
      console.error('토큰이 없습니다.');
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      try {
        const headers = {
          Authorization: `${token}`,
        };
        const response = await fetch(
          '/goody/myPage/myReviewList',
          {
            method: 'GET',
            headers,
          }
        );

        if (!response.ok) {
          throw new Error('HTTP 오류 ' + response.status);
        }

        const data = await response.json();
        console.log(data);

        if (data && data.nickname && data.grade) {
          setUserInfo3(data.keywords);
          setProfile(data.profileImg);
          setNickname(data.nickname);
          setGrade(data.grade);
          setLoading(false);
        } else {
          console.error('API 응답에서 유효한 데이터가 없습니다.');
          setLoading(false);
        }
      } catch (error) {
        console.error('API에서 데이터를 가져오는 중 오류 발생:', error);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  return (
    <>
      <ActionBarClose actionBarName={actionBarName} />

      {loading ? (
        <div className='flex justify-center items-center h-[50rem]'>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />;
        </div>
      ) : (
        <div className='p-5'>
          <div>
            <div className='flex m-4'>
            <img src={`https://firebasestorage.googleapis.com/v0/b/goody-4b16e.appspot.com/o/${profile}?alt=media&token=`} className="rounded-full border" style={{ width: '2.75rem', height: '2.75rem' }} alt="Profile" />

              <div className='ml-2'>
                <div className=''>
                  <label className='font-bold'> {nickname} </label>
                </div>
                <div>
                  <label className='text-xs'>{grade}</label>
                </div>
              </div>

            </div>
            <hr/>
            <div className="p-2">
              <p className="mt-4 mb-2 flex items-center">
                <img src="img/Icon_List.png" alt="키워드 리뷰" className="h-6 w-6 mr-5" />
                <span className="font-extrabold">키워드 리뷰</span>
              </p>
             
              <div className='p-4'>
                <Item_KeywordReview keywords={userInfo3} />
              </div>
            </div>
            <br />
          </div>
        </div>
      )}
    </>
  );
};

export default ReviewList;
