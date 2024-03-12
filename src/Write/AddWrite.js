import React, { useState, useEffect } from 'react';
import { ActionBar } from '../Component/ActionBar';
import './AddWrite.css';
import { Nav } from '../Component/Nav';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Select from '@mui/material/Select';
import Chip from '@mui/material/Chip';
import Box from '@mui/material/Box';
import { NumericFormat } from 'react-number-format';
import InputAdornment from '@mui/material/InputAdornment';
import { TextField } from '@mui/material';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import ImageList from '@mui/material/ImageList';
import ImageListItem from '@mui/material/ImageListItem';
import { styled } from '@mui/system'
import NumberInput from '../Component/NumberInput'
import { useLocation } from 'react-router-dom';
import  { CollectionTag }  from '../Collection/CollectionTag';
import { message } from 'antd';

// 액션바 이름
const actionBarName = "글 작성";

const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5),
}));

/*셀렉트 박스 선택*/
const OPTIONS1 = [
  { value: "카테고리", name: "카테고리" },
  { value: "ENT", name: "연예인" },
  { value: "SPO", name: "스포츠" },
  { value: "MOV", name: "영화" },
  { value: "GAME", name: "게임" },
  { value: "ANI", name: "애니 / 만화" },
  { value: "CHA", name: "캐릭터" },
];

const OPTIONS2 = [
  { value: "거래종류", name: "거래종류" },
  { value: "판매해요", name: "판매해요" },
  { value: "교환해요", name: "교환해요" },
  { value: "나눔해요", name: "나눔해요" },
  { value: "같이해요", name: "같이해요" },
];

const OPTIONS3 = [
  { value: "상태등급", name: "상태등급" },
  { value: "A", name: "A" },
  { value: "B", name: "B" },
  { value: "C", name: "C" },
  { value: "D", name: "D" },
];

//인원수 옵션 1~10
const numOfPeopleOptions = [];
for (let i = 1; i <= 10; i++) {
  numOfPeopleOptions.push(
    <MenuItem key={i} value={i}>
      {i}
    </MenuItem>
  );
}


const AddWrite = () => {
  const location = useLocation();
  const { datatitle, dataexplain } = location.state || {}; // null인 경우에 빈 객체를 할당

  const [selectedImage, setSelectedImage] = useState([]);//첨부한 이미지
  const [selectedOption1, setSelectedOption1] = useState(OPTIONS1[0]);
  const [selectedOption2, setSelectedOption2] = useState(OPTIONS2[0]);
  const [selectedOption3, setSelectedOption3] = useState(OPTIONS3[0]);
  const [title, setTitle] = useState(dataexplain ? datatitle : '');
  const [price, setPrice] = useState('');// 가격
  const [isFreeChecked, setIsFreeChecked] = useState(false);//나눔여부
  const [showTogetherTypeCheckboxes, setShowTogetherTypeCheckboxes] = useState(false);
  const [selectedTogetherType, setSelectedTogetherType] = useState('numOfPeople'); //인원수 or 품목
  const [selectedNumOfPeople, setSelectedNumOfPeople] = useState(null);
  const [inputPeopleField, setInputPeopleField] = useState('');
  const [chipPeople, setChipPeople] = useState([]);
  const [explainText, setExplainText] = useState(dataexplain ? dataexplain : '');
  const [hashTags, setHashTags] = useState([]);
  const [loggedIn, setLoggedIn] = useState(false);
  
  const [messageApi, contextHolder] = message.useMessage();

  const warning = (content) => {
    messageApi.open({
      type: 'warning',
      content: content,
      duration: 2,
      style: {
        marginTop: '72vh',
      },
    });
  };
  
  //토큰 저장 후 로그인 상태 설정
  useEffect(() => {
    // 사용자 로그인 상태를 확인하는 로직을 구현
    const token = localStorage.getItem('token'); // 또는 세션에서 토큰을 가져올 수 있음
    if (token) {
        setLoggedIn(true); // 토큰이 있다면 로그인 상태로 설정
      }
  }, []);

  const handleUpload = async () => {//글작성 버튼 클릭시
    try {
      //사용자 인증 여부 확인
      if (!loggedIn) {
          // 로그인되지 않은 경우 처리
          warning('로그인 후에 글을 작성할 수 있습니다.');
          return;
      }

      const formData = new FormData();// 해당 필드에 원하는 값을 설정
      if (selectedOption2.value === "거래종류") {
        warning('거래종류를 선택해주세요');
        return;
      } else {
        formData.append('transType', selectedOption2.value); //거래종류
      }

      if (selectedImage && selectedImage.length > 0) {
        selectedImage.forEach((image, index) => {
            formData.append(`imgPath[${index}]`, image);
        });
        }
      if(price !== ''){
        const stringWithoutCommas = price.replace(/,/g, '');
        const numberAsInt = parseInt(stringWithoutCommas, 10);//콤마제외하고 String->int 변환
        formData.append('price', numberAsInt);//가격
      }else{
        formData.append('price',0);
      }
      
      if (selectedNumOfPeople !== null) {
        const num = parseInt(selectedNumOfPeople, 10);
        formData.append('numOfPeople', num);//인원수
      }
      formData.append('free', isFreeChecked);//나눔여부
      if (chipPeople.length > 0) {
        const peopleArray = chipPeople.map(item => item.label);
        formData.append('people', peopleArray);//나눌품목
      }
      formData.append('explain', explainText);//설명
      if (selectedOption3.value === "상태등급") {
        warning('상태등급을 선택해주세요');
        return;
      } else {
        formData.append('grade', selectedOption3.value);//등급
      }
      formData.append('title', title);//제목
      if (selectedOption1.value === "카테고리") {
        warning('카테고리를 선택해주세요');
        return;
      } else {
        formData.append('category', selectedOption1.value);//카테고리
      }
      if(hashTags != ''){
        formData.append('hashTags',hashTags);
      }else{
        warning('해시태그를 입력해주세요');
        return;
      }
      console.log([...formData.entries()]);
      
      if(formData.get('category')!=null&&formData.get('grade')!=null&&formData.get('transType')!=null&&formData.get('hashTags')!=null){
        console.log('전송');
        const response = await fetch('/goody/contents/', {
            method: 'POST',
            body: formData, // 멀티파트(form-data) 형식으로 데이터를 보냅니다.
            headers: {
                // 토큰을 사용하여 사용자 인증
                Authorization: `${localStorage.getItem('token')}`,
            },
        });
  
        if (response.ok) {
            // 데이터가 성공적으로 API로 전송되었습니다.
            console.log('데이터가 성공적으로 전송되었습니다.');
            const data = await response.text();
            window.location.href =`/WriteDetail/${data}`; 
        } else {
            // 여기서 오류를 처리합니다.
            console.error('API로 데이터를 전송하는 중 오류가 발생했습니다.');
        }
      }
    } catch (error) {
      console.error('오류가 발생했습니다:', error);
    }
  }

  const handleFileChange = (event) => {//파일첨부시 선택된 파일 이미지 데이터에 추가
    const files = event.target.files;
    const newImages = Array.from(files);
    setSelectedImage([...selectedImage, ...newImages]);

    if (setSelectedImage.length >= 5) {
      alert("이미지초과");
    }
    console.log(files);
  };

  const handleDeleteImage = (index) => {
    const updatedImages = [...selectedImage];
    updatedImages.splice(index, 1);
    setSelectedImage(updatedImages);
  };

  const handleSelectCategoryChange = (event) => {
    const selectedValue = event.target.value;
    const selectedOption = OPTIONS1.find(option => option.value === selectedValue);
    setSelectedOption1(selectedOption);

  };

  const handleSelectTransTypeChange = (event) => {
    setSelectedNumOfPeople(null);
    setChipPeople([]);
    setSelectedTogetherType('');
    setIsFreeChecked(false);
    const selectedValue = event.target.value;
    const selectedOption = OPTIONS2.find(option => option.value === selectedValue);
    setSelectedOption2(selectedOption);
    // If "같이해요" is selected, show the radio checkboxes.
    setShowTogetherTypeCheckboxes(selectedValue === "같이해요");
    if(selectedValue==='같이해요'){
      setSelectedTogetherType('numOfPeople');
    }
    if (selectedValue === '나눔해요') {
      setIsFreeChecked(true);
      setPrice('');
    }

  };

  const handleSelectGradeChange = (event) => {
    const selectedValue = event.target.value;
    const selectedOption = OPTIONS3.find(option => option.value === selectedValue);
    setSelectedOption3(selectedOption);
  };

  const handleTogetherCheckBoxChange = (event) => {//모집인원 or 품목
    setSelectedNumOfPeople(null);
    setChipPeople([]);
    setSelectedTogetherType(event.target.value);
  }

  const handleSelectNumOfPeopleChange = (event) => {//선택한 모집인원
    const value = parseInt(event.target.value, 10);
    if (value >= 0) {
      setSelectedNumOfPeople(value);
    } else if(value<0) {
      // 이 부분에서 0 미만의 값에 대한 처리를 수행하거나 무시합니다.
      setSelectedNumOfPeople(0);
    }
  }

  const handleChipDelete = (chipToDelete) => () => {
    setChipPeople((chips) => chips.filter((chip) => chip.key !== chipToDelete.key));
  };

  const handleAddBtnClick = () => {//인원추가 버튼 클릭시
    if (inputPeopleField.trim() !== '') {
      // 입력 값이 공백이 아닌 경우에만 추가
      const newPerson = { key: chipPeople.length, label: inputPeopleField };
      setChipPeople([...chipPeople, newPerson]);
      setInputPeopleField(''); // 입력 필드 비우기
    }
  };

  const handleTagsChange = (newTags) => {
    setHashTags(newTags); // 필드 데이터 업데이트
  };

  return (
    <div className="App w-full h-full mt-18">      
      <ActionBar actionBarName={actionBarName} className='fixed top-0 w-full' />

      {/*return 구문에서 PHOTO 컴포넌트 화면에 렌더링*/}
      <div id="addwirte" className='mt-5 pt-10'>

        <div id="photo" className='p-3'>
          
          <div className='flex space-x-4 p-5'>
            
            <div className='p-6 border border-gray-200 rounded-xl shadow-[0_1px_8px_rgba(180,180,180,0.7)] flex'>
            <input type="file" multiple id="fileInput" className="hidden" onChange={handleFileChange} />
            <label id="fileInput" htmlFor="fileInput">
              <img className='w-16' src='img\Icon_Camera.png'></img>
              <p className='text-center font-bold text-[#B4B4B4]'>{selectedImage.length}/5</p>
            </label>
          </div>
          
          {/* 카메라 아이콘 옆 이미지 리스트 */}
            <ImageList sx={{ width: 1000 }} cols={5} rowHeight={130} gap={20}>
              {selectedImage.map((image, index) => (
                <ImageListItem key={index} style={{ width: '100px', height: '100px'}}>
                  <div style={{ position: 'relative', display: 'inline-block' }}>
                    <img
                      srcSet={URL.createObjectURL(image)}
                      src={URL.createObjectURL(image)}
                      alt="image"
                      loading="lazy"
                      className='rounded-xl object-cover w-[100px] h-[100px]'
                    />
                    <button onClick={() => handleDeleteImage(index)}
                      style={{
                        position: 'absolute',
                        top: '0',
                        right: '0',
                        zIndex: '1',
                        cursor: 'pointer',
                      }}>
                      <HighlightOffIcon />
                    </button>
                  </div>
                </ImageListItem>
              ))}
            </ImageList>

          </div>
        </div>
        <hr />

        {/* 카테고리 선택박스 */}
        <FormControl sx={{ m: 1, minWidth: 390 }}>
          <Select className="w-11/12 sm:flex-row h-10 sm:h-16 p-2  ml-2" value={selectedOption1.value} onChange={handleSelectCategoryChange}
            sx={{ boxShadow: 'none', '.MuiOutlinedInput-notchedOutline': { border: 0 } }}>
            {OPTIONS1.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <hr />

        {/* 거래종류 선택박스 */}
        <FormControl sx={{ m: 1, minWidth: 390 }}>
          <Select className="w-11/12 sm:flex-row h-10 sm:h-16 p-2  ml-2" value={selectedOption2.value} onChange={handleSelectTransTypeChange}
            sx={{ boxShadow: 'none', '.MuiOutlinedInput-notchedOutline': { border: 0 } }}>
            {OPTIONS2.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <hr />

        {/*같이해요 선택시 나타나는 박스*/}
        <div style={{ margin: '10px', padding: '10px', display: showTogetherTypeCheckboxes ? 'block' : 'none' }}>
          <div className='mb-4 flex items-center justify-center'>
            <FormControl>
              <RadioGroup row defaultValue="numOfPeople" name="radio-buttons-group" value={selectedTogetherType} onChange={handleTogetherCheckBoxChange}>
                <FormControlLabel value="numOfPeople" control={<Radio color="default" size='small' />} label="인원수" style={{ marginRight: '140px' }} />
                <FormControlLabel value="people" control={<Radio color="default" size='small' />} label="품목" />
              </RadioGroup>
            </FormControl>
          </div>
          <div className='flex items-center justify-center'>
            {selectedTogetherType === "numOfPeople" ? (
              <FormControl>
                <NumberInput placeholder="모집인원" value={selectedNumOfPeople} onChange={handleSelectNumOfPeopleChange}/>
              </FormControl>
            ) : (
              <div>
                <div className='flex items-center justify-center'>
                  <TextField size='small' label="추가 품목" variant="outlined" value={inputPeopleField} onChange={(e) => setInputPeopleField(e.target.value)} />
                  <img className='m-3' src='img\personAdd.png' width={30} alt='추가버튼' onClick={handleAddBtnClick} />
                </div>
                <Box sx={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap', listStyle: 'none', p: 0.5, m: 0, }} component="ul">
                  {chipPeople.map((data) => {
                    let icon;
                    return (
                      <ListItem key={data.key}>
                        <Chip
                          icon={icon}
                          label={data.label}
                          onDelete={data.label === 'React' ? undefined : handleChipDelete(data)}
                        />
                      </ListItem>
                    );
                  })}
                </Box>
              </div>
            )}
          </div>
        </div>
        <hr />


        {/* 등급 선택박스 */}
        <FormControl sx={{ m: 1, minWidth: 390 }}>
          <Select className="w-11/12 sm:flex-row h-10 sm:h-16 p-2  ml-2" value={selectedOption3.value} onChange={handleSelectGradeChange}
            sx={{ boxShadow: 'none', '.MuiOutlinedInput-notchedOutline': { border: 0 } }}>
            {OPTIONS3.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <hr />


        {/* 글 제목 */}
        <div className='flex p-2 h-10 m-2 mr-4 ml-4 items-center'>
          <TextField fullWidth
            sx={{ "& .MuiOutlinedInput-root": { "& > fieldset": { border: "none" } } }}//테두리제거
            value= {title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder='글 제목' />
        </div>
        <hr />

        {/* 가격 */}
        <div className='flex h-10 m-4 items-center'>
          <FormControl>
            <NumericFormat
              value={price}
              thousandSeparator
              customInput={TextField}
              disabled={isFreeChecked}
              sx={{ "& .MuiOutlinedInput-root": { "& > fieldset": { border: "none" } } }}//테두리제거
              onChange={(e) => setPrice(e.target.value)}
              placeholder='가격'
              InputProps={{ startAdornment: (<InputAdornment position='start'>￦</InputAdornment>), style: { border: 'none' , marginRight: '45px'} }}
            />
          </FormControl>

          {/* 나눔버튼 */}
          <FormGroup>
            <FormControlLabel control={
              <Checkbox checked={isFreeChecked}
                onChange={ (e) => {
                    setIsFreeChecked(e.target.checked);
                    if (e.target.checked) {
                      setPrice(''); // Set price to an empty string when the checkbox is checked
                    }
                  }}
                inputProps={{ 'aria-label': 'controlled' }} color="default" />} label="나눔" />
          </FormGroup>
        </div>
        <hr />

        {/* 내용 */}
        <div className='flex mr-4 ml-4 items-center'>
          <TextField fullWidth
            sx={{
              "& .MuiOutlinedInput-root": { "& > fieldset": { border: "none" } }, "& .MuiInputBase-input": {
                overflowY: 'auto', // Allow scrolling when content exceeds the specified rows
              },
            }}//테두리제거
            value={explainText}
            onChange={(e) => setExplainText(e.target.value)}
            multiline
            rows={8}
            placeholder='내용' />
        </div>

        <CollectionTag onTagsChange={handleTagsChange} /> 
        
        {/* 등록 버튼 */}
        <div style={{ display: 'flex', alignItems: 'flex-end', marginBottom: '80px', marginLeft: '20px', marginRight: '20px' }}>
          <button onClick={handleUpload}>
            <img src='img\registerBtn.png'></img>
          </button>
        </div>
      </div>
      
      <Nav />
      {/* 알림창 */}
      <div >
        {contextHolder}
      </div>
    </div>
  );
}

export default AddWrite;