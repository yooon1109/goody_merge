import React from 'react';
import PropTypes from 'prop-types';
import Chip from '@mui/material/Chip';

const Item_width = ({ data }) => {
  const MAX_TITLE_LENGTH = window.innerWidth <= 390 ? 14 : 16;

  const formatPrice = (price) => {
    return price.toLocaleString(); // 가격에 천 단위 구분 기호(쉼표) 추가
  };

  const truncatedTitle = data.title.length > MAX_TITLE_LENGTH
    ? data.title.slice(0, MAX_TITLE_LENGTH) + '...'
    : data.title;

  return (
    <>
      <div className='flex mt-7 ml-5 mb-5'>
        <div>
          <img src={data.thumbnailImg} alt={data.title} className='rounded-xl'  style={{ width:'60px', height: '60px', objectFit: 'cover' }} />
        </div>
        <div className='ml-3 '>
          <p className='font-bold text-sm'>{truncatedTitle}</p>
          <p className='font-bold text-xs'>{formatPrice(data.price)}원</p>
          <p className='text-xs'>{data.createdDate}</p>
        </div>
        <Chip
            label={data.transType}
            variant="outlined"
            size="small"
            className='h-6 ml-auto mt-auto mr-4'
          />
      </div>
    </>
  );
};

Item_width.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string.isRequired,
    price: PropTypes.number.isRequired,
    createdDate: PropTypes.string,
    transType: PropTypes.string.isRequired,
    category: PropTypes.string.isRequired,
    thumbnailImg: PropTypes.string.isRequired,
    documentId: PropTypes.string.isRequired,
  }).isRequired,
};

export default Item_width;
