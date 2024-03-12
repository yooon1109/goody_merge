import React from 'react';
import './SearchDatail_Item.css'; 
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
const SearchDatail_Item = (props) => {

const { title, price, createdDate,thumbnailImg,documentId,transType } = props;
    return (
     <>
              <div width={'150px'} height={'150px'}>
              <Link to={`/WriteDetail/${documentId}`}>
                <div width={'150px'} height={'150px'}>
 
                    <button style={{ width: '115px', height: '115px' }} className='mt-[1rem] border rounded-[1rem] flex items-center overflow-hidden m-2'>
                    <img style={{ width: '100%', height: '100%', objectFit: 'cover' }} src={thumbnailImg} alt='아이템' />
                    </button>
          
                    <div className='mt-2 text-[0.9rem] ml-2'>
             
                    <button style={{ textAlign: 'left' }}>

                    <p className='font-bold text-overflow'>{title}</p>
                    <p className='font-bold text-overflow'>{price}원</p>
                    <p className='font-bold text-overflow'>{createdDate}</p>

                    </button>
                
                    </div>
                    
                    <div className='border rounded-[0.6rem] text-[0.85rem] border-[#B4B4B4] p-[0.08rem] px-[0.4rem] inline-block ml-10 text-center w-20'>{transType}</div>
                </div>
              </Link>
              </div>
   </>
    );
};

SearchDatail_Item.propTypes = {
  title: PropTypes.string.isRequired,
  price: PropTypes.number.isRequired,
  createdDate: PropTypes.string.isRequired,
  thumbnailImg: PropTypes.string.isRequired,
  documentId:PropTypes.string.isRequired,
  transType:PropTypes.string.isRequired,
};


export { SearchDatail_Item };