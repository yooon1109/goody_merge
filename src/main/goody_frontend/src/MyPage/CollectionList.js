import React, { useState, useEffect } from 'react';
import { ActionBarClose } from '../Component/ActionBarClose';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Empty } from 'antd';

const CollectionItem = ({ item }) => {
  return (
    <div className='inline-flex'>
      <Link to={`/collectionDetail/${item.documentId}`}>
        <button className='rounded-2xl'>
          <div>
            <img
              src={item.thumbnailPath}
              className='drop-shadow-[0_2px_1px_rgba(220,166,19,100)] Collecthin_image col_item rounded-2xl'
            />
          </div>
        </button>
      </Link>
    </div>
  );
};

const CollectionList = () => {
  const actionBarName = '컬렉션 찜 목록';
  const [collectionItems, setCollectionItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');

    const fetchData = async () => {
      try {
        const headers = {
          Authorization: `${token}`,
        };

        const response = await fetch(
          '/goody/myPage/collectionLikeList',
          {
            method: 'GET',
            headers,
          }
        );

        if (!response.ok) {
          throw new Error('HTTP 오류 ' + response.status);
        }

        const data = await response.json();

        setCollectionItems(data.collectionLikes);
        setLoading(false); // Set loading to false once data is fetched

      } catch (error) {
        console.error('API에서 데이터를 가져오는 중 오류 발생:', error);
        setLoading(false); // Set loading to false in case of an error
      }
    };

    fetchData();

  }, []);

  return (
    <>
      <ActionBarClose actionBarName={actionBarName} />
      <div className='flex flex-col items-center pt-10'>
        <div className='grid grid-cols-3 gap-1 p-3 rounded-xl'>
          {loading ? (
            <div className='flex justify-center items-center h-[50rem]'>
              <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
            </div>
          ) : (
            Array.isArray(collectionItems) &&
            (collectionItems.length === 0 ? (
              <div className='flex justify-center items-center h-[50rem]'>
                <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
              </div>
            ) : (
              collectionItems.map((item, index) => (
                <CollectionItem key={index} item={item} />
              ))
            ))
          )}
        </div>
      </div>
    </>
  );
};

CollectionItem.propTypes = {
  item: PropTypes.shape({
    documentId: PropTypes.string.isRequired,
    thumbnailPath: PropTypes.string.isRequired,
  }).isRequired,
};

export default CollectionList;
