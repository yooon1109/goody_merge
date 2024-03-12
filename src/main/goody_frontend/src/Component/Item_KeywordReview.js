import React from 'react';
import PropTypes from 'prop-types';


const Item_KeywordReview = ({ keywords }) => {

    const good1Keyword = { good1: keywords.good1 };
    const good2Keyword = { good2: keywords.good2 };
    const good3Keyword = { good3: keywords.good3 };
    const good4Keyword = { good4: keywords.good4 };
    const good5Keyword = { good5: keywords.good5 };
    const good6Keyword = { good6: keywords.good6 };

   
    const bad1Keyword = { bad1: keywords.bad1 };
    const bad2Keyword = { bad2: keywords.bad2 };
    const bad3Keyword = { bad3: keywords.bad3 };
    const bad4Keyword = { bad4: keywords.bad4 };
    const bad5Keyword = { bad5: keywords.bad5 };
    const bad6Keyword = { bad6: keywords.bad6 };
    return (
        <>
            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600 text-sm">친절해요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="친절해요" className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm ">{good1Keyword.good1}</p>
                </div>
            </div>

            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">응답이 빨라요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="응답이 빨라요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{good2Keyword.good2}</p>
                </div>
            </div>

            <hr />


            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">믿어도 돼요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="믿어도 돼요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{good3Keyword.good3}</p>
                </div>
            </div>

            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">상품 상태가 좋아요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="상품 상태가 좋아요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{good4Keyword.good4}</p>
                </div>
            </div>

            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">저렴해요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="저렴해요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{good5Keyword.good5}</p>
                </div>
            </div>

            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">시간약속을 잘지켜요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="저렴해요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{good6Keyword.good6}</p>
                </div>
            </div>

            <hr />

            <hr />
            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">불친절해요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="시간 약속을 잘 지켜요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{bad1Keyword.bad1}</p>
                </div>
            </div>
            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">응답이 느려요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="시간 약속을 잘 지켜요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{bad2Keyword.bad2}</p>
                </div>
            </div>
            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">믿지 못하겠어요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="시간 약속을 잘 지켜요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{bad3Keyword.bad3}</p>
                </div>
            </div>
            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">상품상태가 안좋아요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="시간 약속을 잘 지켜요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{bad4Keyword.bad4}</p>
                </div>
            </div>
            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">비싸요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="시간 약속을 잘 지켜요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{bad5Keyword.bad5}</p>
                </div>
            </div>
            <hr />

            <div className="flex pb-2 mb-2 mt-4 justify-between">
                <p className="font-bold text-gray-600  text-sm">시간 약속을 못지켜요</p>
                <div className='flex items-center'>
                    <img src="img/Icon_Person.png" alt="시간 약속을 잘 지켜요"   className="h-3.5 w-3.5 mr-2" />
                    <p className="font-bold text-gray-600 text-sm">{bad6Keyword.bad6}</p>
                </div>
            </div>
            <hr />

        </>
    );
};

Item_KeywordReview.propTypes = {
    keywords: PropTypes.shape({
        good1: PropTypes.number,
        good2: PropTypes.number,
        good3: PropTypes.number,
        good4: PropTypes.number,
        good5: PropTypes.number,
        good6: PropTypes.number,
        bad1: PropTypes.number,
        bad2: PropTypes.number,
        bad3: PropTypes.number,
        bad4: PropTypes.number,
        bad5: PropTypes.number,
        bad6: PropTypes.number,
    }).isRequired,
};
export { Item_KeywordReview };
