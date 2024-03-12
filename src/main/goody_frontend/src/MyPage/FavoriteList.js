import React, { useState, useEffect } from 'react';
import { ActionBarClose } from '../Component/ActionBarClose';
import Item_width from '../Component/Item_width';
import { Link } from 'react-router-dom';
import { Empty } from 'antd';

const FavoriteList = () => {
  const [productData, setProductData] = useState([]);
  const [loading, setLoading] = useState(true);

  const actionBarName = "찜목록";


  useEffect(() => {
    // 토큰 가져오기
    const token = localStorage.getItem('token');

    const fetchData = async () => {
      try {
        const headers = {
          Authorization: `${token}`,
        };

        const response = await fetch(
          `/goody/myPage/contentsLikeList`,
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

        if (data.dto && Array.isArray(data.dto) && data.dto.length > 0) {
          setProductData(data.dto);
          setLoading(false);
        } else {
          setLoading(false);
        }
      } catch (error) {
        console.error('API에서 데이터를 가져오는 중 오류 발생:', error);
        setLoading(false);
      }
    };

    fetchData();
  }, [actionBarName]);

  return (
    <>
      <ActionBarClose actionBarName={actionBarName} />
      {loading ? (
        <div className='flex justify-center items-center h-[50rem]'>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
        </div>
      ) : productData.length === 0 ? (
        <div className='flex justify-center items-center h-[50rem]'>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
        </div>
      ) : (
        productData.map((item, index) => (
          <div key={index}>
            <Link to={`/WriteDetail/${item.documentId}`}>
              <Item_width data={item} />
              {index === productData.length - 1 && <div style={{ marginBottom: '6rem' }}></div>}
            </Link>
          </div>
        ))
      )}
    </>
  );
};

export default FavoriteList;