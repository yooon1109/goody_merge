import React, { useState} from 'react';
import { Link } from 'react-router-dom';
import './SearchDatail_Item.css'; 
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart as faHeartRegular } from '@fortawesome/free-regular-svg-icons';
import PropTypes from 'prop-types';

const SearchDatail_Item = (props) => {
  
const { title, price, createdDate,thumbnailImg } = props;


const Item = ( ) => {

    const [liked, setLiked] = useState([false, false, false, false]);
    
 
    const handleLikeClick = (index) => {
        const updatedLiked = [...liked];
        updatedLiked[index] = !updatedLiked[index];
        setLiked(updatedLiked);
      };

    

    return (
        <div>
 
            {/*1줄*/}
            <div style={{ width: '100%', height:'300px' }}>
                <div className='absolute mt-1'>
                    <FontAwesomeIcon onClick={() => handleLikeClick(0)} icon={faHeartRegular} className={`absolute mt-[2.5rem] ml-[6.5rem] ${liked[0] ? 'text-color' : 'fa-heart-regular'}`} size="lg" />
                    <Link to="/sightseeing">
                    <button className='mt-[2rem] ml-[1.4rem]'>
                    <img width={'110px'} src={thumbnailImg} alt='아이템1'></img>
                    </button>
                    </Link>
                    <div className='mt-2 ml-[1.6rem] text-[0.9rem]'>
                    <Link to="/sightseeing">
                    <button className="" style={{ textAlign: 'left' }}>
                   
                    <p className='font-bold'>{title}</p>
                    <p className='font-bold'>{price}원</p>
                    <p className='font-bold'>{createdDate}</p>
              
                    </button>
                    </Link>
                    </div>
                    <div className='border rounded-[0.6rem] text-[0.85rem] border-[#B4B4B4] p-[0.08rem] px-[0.4rem] inline-block ml-[1.6rem] mt-[0.5rem]'>거래상태</div>
                </div>
            </div>
        </div>
    );
};

SearchDatail_Item.propTypes = {
  title: PropTypes.string.isRequired,
  price: PropTypes.number.isRequired,
  createdDate: PropTypes.string.isRequired,
  thumbnailImg: PropTypes.string.isRequired,
};

    return (
    <div className='flex'>
        <Item />
      </div>
    );
};

export { SearchDatail_Item };