import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Item_width from '../Component/Item_width';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';

const TabView = () => {
  const [tabValue, setTabValue] = useState(0);
  const [postPreviewInfo, setPostPreviewInfo] = useState([]);
  const [loading, setLoading] = useState(true); // 데이터 로딩 상태를 관리
  const [pageNumber, setPageNumber] = useState(1);

  // 토큰 가져오기
  const token = localStorage.getItem('token');

  // fetchData 함수를 정의
  const fetchData = async (postType) => {
    try {
      const headers = {
        Authorization: `${token}`,
      };

      const response = await fetch(
        `/goody/contents/preview-info?transType=${postType}&page=0`,
        {
          method: 'GET',
          headers,
        }
      );

      if (!response.ok) {
        throw new Error('HTTP 오류 ' + response.status);
      }

      const data = await response.json();

      if (data.postPreviewInfo && data.postPreviewInfo.length > 0) {
        setPostPreviewInfo(data.postPreviewInfo);
        setLoading(false);
      } else {
        console.error('API에서 데이터를 가져오는 중 오류 발생: 데이터가 비어 있습니다.');
      }
    } catch (error) {
      console.error('API에서 데이터를 가져오는 중 오류 발생:', error);
      setLoading(false);
    }
  };

  useEffect(() => {
    // 인증된 사용자인지 확인하고, 토큰이 없거나 인증에 실패하면 리디렉션할 수 있음
    if (!token) {
      window.location.href = '/login'; // 로그인 페이지로 리디렉션
      return;
    }

    // 처음 마운트될 때 '판매해요' 탭의 데이터를 가져오도록 설정
    fetchData('판매해요');
  }, [token]); // useEffect의 의존성 배열에 token 추가

  const handleChange = (event, newValue) => {
    setTabValue(newValue);

    // 탭 변경 시 해당하는 데이터를 가져오도록 설정
    switch (newValue) {
      case 0:
        fetchData('판매해요');
        break;
      case 1:
        fetchData('교환해요');
        break;
      case 2:
        fetchData('나눔해요');
        break;
      case 3:
        fetchData('같이해요');
        break;
      default:
        break;
    }
  };

   // 스크롤 이벤트 핸들러
  const handleScroll = () => {
    const windowHeight = window.innerHeight;
    const documentHeight = document.documentElement.scrollHeight;
    const scrollPosition = window.scrollY;

    // 스크롤이 화면 맨 아래로 도달하면 페이지 번호를 변경
    if (scrollPosition + windowHeight === documentHeight) {
      console.log('스크롤이 화면 맨 아래로 도달함')
      setPageNumber(pageNumber + 1);
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [pageNumber]);

  return (
    <Box sx={{ width: '100%', bgcolor: 'background.paper', marginTop: '1rem' }}>
      <Tabs
        value={tabValue}
        onChange={handleChange}
        variant="scrollable"
        scrollButtons={false}
        textColor="secondary"
        indicatorColor="secondary"
        aria-label="scrollable prevent tabs example"
      >
        <Tab label={<Typography variant="body1" sx={{ minWidth: 80, fontWeight: 'bold' }}>판매해요</Typography>} />
        <Tab label={<Typography variant="body1" sx={{ minWidth: 80, fontWeight: 'bold' }}>교환해요</Typography>} />
        <Tab label={<Typography variant="body1" sx={{ minWidth: 80, fontWeight: 'bold' }}>나눔해요</Typography>} />
        <Tab label={<Typography variant="body1" sx={{ minWidth: 80, fontWeight: 'bold' }}>같이해요</Typography>} />
      </Tabs>

      <div style={{ overflowY: 'auto', height: '500px' }}>
      {/* 데이터 로딩 중 또는 데이터가 비어 있는 경우 처리 */}
      {loading && <div>Loading...</div>}

      {/* 각 탭에 따라 데이터 렌더링 */}
      {!loading && postPreviewInfo && postPreviewInfo.map((item, index) => (
        <div key={index} >
          <Link to={`/WriteDetail/${item.documentId}`}>
            <Item_width data={item} />
            {index === postPreviewInfo.length - 1 && <div style={{ marginBottom: '6rem' }}></div>}
          </Link>
        </div>
      ))}
      </div>
    </Box>
  );
};

export default TabView;
