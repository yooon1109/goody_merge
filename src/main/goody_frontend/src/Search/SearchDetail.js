import React, { useState,useEffect } from 'react';
import { Nav } from '../Component/Nav';
import PropTypes from 'prop-types';
import { SearchDatail_Item } from './SearchDetail_item';
import SearchSelectbox from './SearchSelectbox';
import { useNavigate} from 'react-router-dom';
import { useParams } from 'react-router-dom';
import { Empty } from 'antd';



const OPTIONS1 = [
  { value: "" , name: "카테고리" },
  { value: "ENT", name: "연예인" },
  { value: "SPO", name: "스포츠" },
  { value: "MOV", name: "영화" },
  { value: "GAME", name: "게임" },
  { value: "ANI", name: "애니 / 만화" },
 
];

const OPTIONS2 = [
  { value: "", name: "구매형태" },
  { value: "판매해요", name: "판매해요" },
  { value: "교환해요", name: "교환해요" },
  { value: "나눔해요", name: "나눔해요" },
  { value: "같이해요", name: "같이해요" },

];

const OPTIONS3 = [
  { value: "", name: "품절유무" },
  { value: "true", name: "품절됨" },
  { value: "false", name: "품절안됨" },
  
];


const SearchDetail = () => {
  const navigate = useNavigate(); //네비게이션
  const { Searchtext } = useParams(); //게시글 파라미터 추출
  
  const [text, setText] = useState(Searchtext); //입력한 값
  const [category,setCategory] = useState(''); //

  const [transtype,setTransType] = useState('');
  const [sold,setSold] = useState('');
  
  const [loading, setLoading] = useState(false); // setLoading 상태를 추가
  const [data, setData] = useState([]);
  const token = localStorage.getItem('token');

  const queryParameters = [];

  let apiUrl = '/goody/contents/search';
 
   if (text) {
      apiUrl += `?search=${text}`;
   }
  
  if (category) {
    queryParameters.push(`category=${category}`);
  }
  
  if (transtype) {
    queryParameters.push(`transType=${transtype}`);
  }
  
  if (sold) {
    queryParameters.push(`sold=${sold}`);
  }
  
  if (queryParameters.length > 0) {
    apiUrl += `?${queryParameters.join('&')}`;
  }

  const handleBack = () => {
  
      navigate(-1); // Navigate to the previous route
    
  }
  const handleCategoryChange = (selectedCategory) => {
    setCategory(selectedCategory); // 선택한 카테고리 값을 상태 변수에 저장
  };

  const handleTranstypeChange = (selectedTransType) => {
    setTransType(selectedTransType); // 선택한 카테고리 값을 상태 변수에 저장
  };

  const handleSoldChange = (selectedSold) => {
    setSold(selectedSold); // 선택한 카테고리 값을 상태 변수에 저장
  };

  const handleTextChange = (event) => {
    setText(event.target.value);
   
  };





  const handleSearch = () => {
    setLoading(true);

    if(text){
      navigate(`/SearchDetail/${text}`, { replace: true });
    }else{
      navigate(`/SearchDetail/`, { replace: true });
    }
    // 요청 헤더 설정
    const headers = {
      Authorization: `${token}`,
    };

    // GET 요청을 수행
    fetch(apiUrl, {
      method: 'GET',
      headers: headers,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((responseData) => {
        // 데이터를 추출하고 상태 변수에 저장
        setData(responseData);
        setLoading(false); // 데이터 로딩 완료
      })
      .catch((error) => {
        console.error('API 요청 중 오류 발생:', error);
        setLoading(false); // 데이터 로딩 오류
      });
  };


  useEffect(() => {
    handleSearch();
   
  }, []);
  
  const handleSubmit = (event) => {
    event.preventDefault();
    console.log('검색 버튼을 눌렀습니다. 입력값:', text);
  };

  return (
    <>
    <div className='w-full'>
      <div className='flex mt-10 bg-yellow justify-center' style={{ alignItems: 'center' }}>
        <div className='flex mt-[0.2rem] ml-[0.8rem]'>
          <button onClick={handleBack}>
            <img src='../img\yellow_left.png' className='w-[1.1rem] h-[1.9rem]' alt="뒤로가기"/>
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className='h-[2rem] w-[19rem] ml-[1.2rem] border-rounded-[0.5rem] flex items-center'>
            <img src='../img\Bar.png' className='w-[23rem] h-[0.3rem] mt-[2rem] flex items-center' alt="바" />
            <input
              type="text"
              value={text}
              maxLength={100}
              onChange={handleTextChange}
              placeholder = '검색어를 입력해주세요'
              className='transparent outline-none border-0 focus:outline-none w-[22rem] ml-[-19.5rem] mt-[-0.25rem]'
              style={{ border: 'none', padding: '0 8px'}}
            />
          </div>
        </form>
        <div className='ml-[0.6rem] mb-[-0.8rem]'>
          <button onClick={handleSearch}>
            <img src="../img/Search2.png" alt='검색' width={'30px'} height={'30px'} />
          </button>
        </div>
      </div>

      <div className='flex justify-center'>
      <SearchSelectbox OPTIONS={OPTIONS1}  OPTIONNAME='카테고리' onChange={handleCategoryChange} />  
      <SearchSelectbox OPTIONS={OPTIONS2}  OPTIONNAME='구매형태' onChange={handleTranstypeChange} /> 
      <SearchSelectbox OPTIONS={OPTIONS3}  OPTIONNAME='품절유무' onChange={handleSoldChange}/>
      </div>

      <div className='flex flex-wrap justify-center items-center'>
        {loading ? (
          <div className='flex justify-center items-center h-[30rem]'>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
          </div>
        ) : data.length === 0 ? (
          <div className='flex justify-center items-center h-[30rem]'>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
          </div>
        ) : (
          data.map((item, index) => (
            <SearchDatail_Item
              key={index}
              title={item.title}
              price={item.price}
              createdDate={item.createdDate}
              thumbnailImg={item.thumbnailImg}
              documentId={item.documentId}
              transType={item.transType}
            />
          ))
        )}
      </div>

    
    </div>

          
    
    <Nav />
    </>
  );
}

SearchSelectbox.propTypes = {
  OPTIONS: PropTypes.arrayOf(PropTypes.shape({
    value: PropTypes.string,
    name: PropTypes.string,
  })).isRequired,
};

export default SearchDetail;
