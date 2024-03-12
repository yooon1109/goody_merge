import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

const ChatProfileBtn2 = ({ ItemInfo, chattingEnteruser }) => {
    const MAX_TITLE_LENGTH = window.innerWidth <= 390 ? 12 : 14;
    const formatPrice = (price) => {
        return price.toLocaleString(); // 가격에 천 단위 구분 기호(쉼표) 추가
    };

    const truncatedTitle = ItemInfo.title.length > MAX_TITLE_LENGTH
        ? ItemInfo.title.slice(0, MAX_TITLE_LENGTH) + '...'
        : ItemInfo.title;


    return (
        <>
            <div className='flex border relative'
                style={{
                    width: '25rem', height: '4em', borderRadius: '50px 0 10px 50px',
                    backgroundColor: '#FFF2C6'
                }}>

                <img src={ItemInfo.thumbnailImg} alt="프로필사진" className="rounded-full border" style={{ width: '4rem', height: '4rem' }}></img>

                <div className='flex flex-col justify-center'>
                    <p className='pl-5 font-semibold text-black'>{truncatedTitle}</p>
                    <p className='pl-5 text-black '> {formatPrice(ItemInfo.price)}원</p>
                </div>

                <Link
                    to={'/review'}
                    state={{ itemInfoDocumentId: ItemInfo.documentId, itemInforeceiveId: ItemInfo.writerId, chattingEnteruser: chattingEnteruser }}
                >

                    <button className='border absolute bottom-4 right-1 mr-2 flex justify-center font-bold text-sm items-center text-black'
                        style={{ borderRadius: '10px 0 10px 10px', width: '6rem', height: '2rem', backgroundColor: '#FFFFFF' }}>
                        거래확정
                    </button>
                </Link>
            </div>
        </>
    );
};

ChatProfileBtn2.propTypes = {
    ItemInfo: PropTypes.shape({
        title: PropTypes.string.isRequired,
        price: PropTypes.number.isRequired,
        thumbnailImg: PropTypes.string.isRequired,
        documentId: PropTypes.string.isRequired,
        writerId: PropTypes.string.isRequired,
    }).isRequired,

    chattingEnteruser: PropTypes.array.isRequired,
};

export default ChatProfileBtn2;