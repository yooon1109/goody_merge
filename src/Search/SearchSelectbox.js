import React from 'react';
import PropTypes from 'prop-types';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';



function SearchSelectbox({ OPTIONS , OPTIONNAME , onChange }) {
  const handleChange = (event) => {
    const selectedCategory = event.target.value;
    onChange(selectedCategory); // 선택한 값 전달
  };

  return (
<FormControl sx={{ marginY: 3, minWidth: 120, minHeight: 40, marginX:0.5 }}>
       <InputLabel id="demo-simple-select-label">{OPTIONNAME}</InputLabel>
  <Select
    labelId="demo-simple-select-label"
    id="demo-simple-select"
    label={OPTIONNAME}
    onChange={handleChange}
  >
        {OPTIONS.map((option) => (
          <MenuItem key={option.value} value={option.value}>
            {option.name}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}

SearchSelectbox.propTypes = {
  OPTIONS: PropTypes.arrayOf(
    PropTypes.shape({
      value: PropTypes.string,
      name: PropTypes.string,
    })
  ).isRequired,
  OPTIONNAME: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
};

export default SearchSelectbox;