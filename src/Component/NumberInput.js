import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Unstable_NumberInput as BaseNumberInput } from '@mui/base/Unstable_NumberInput';
import clsx from 'clsx';

export default function NumberInputBasic({ placeholder, value, onChange }){
    const [inputValue, setInputValue] = useState(value);
    const handleValueChange = (event, val) => {
        setInputValue(val); // 내부 상태 업데이트
        onChange(event, val); // 부모 컴포넌트에서 전달된 이벤트 핸들러 호출
      };
    return (
    <NumberInput
        value={inputValue} // 내부 상태 사용
        placeholder={placeholder}
        onChange={handleValueChange} // 내부 핸들러 사용
        min = {0}
    />
  );
}

NumberInputBasic.propTypes = {
    placeholder: PropTypes.string,
    value: PropTypes.number, // 예: 숫자 형태로 변경
    onChange: PropTypes.func, // 예: 함수 형태로 변경
  };
  
const resolveSlotProps = (fn, args) => (typeof fn === 'function' ? fn(args) : fn);

const NumberInput = React.forwardRef(function NumberInput(props, ref) {
  return (
    <BaseNumberInput
      {...props}
      ref={ref}
      slotProps={{
        root: (ownerState) => {
          const resolvedSlotProps = resolveSlotProps(
            props.slotProps?.root,
            ownerState,
          );
          return {
            ...resolvedSlotProps,
            className: clsx(
              'grid grid-cols-[1fr_19px] grid-rows-2 overflow-hidden font-sans rounded-lg text-slate-900 dark:text-slate-300 border border-solid  bg-white dark:bg-slate-900  hover:border-violet-400 dark:hover:border-violet-400 focus-visible:outline-0 ',
              ownerState.focused
                ? 'border-violet-400 dark:border-violet-400 shadow-lg shadow-outline-purple dark:shadow-outline-purple'
                : 'border-slate-300 dark:border-slate-600 shadow-md shadow-slate-100 dark:shadow-slate-900',
              resolvedSlotProps?.className,
            ),
          };
        },
        input: (ownerState) => {
          const resolvedSlotProps = resolveSlotProps(
            props.slotProps?.input,
            ownerState,
          );
          return {
            ...resolvedSlotProps,
            className: clsx(
              'col-start-1 col-end-2 row-start-1 row-end-3 text-sm font-sans leading-normal text-slate-900 bg-inherit border-0 rounded-[inherit] dark:text-slate-300 px-3 py-2 outline-0 focus-visible:outline-0 focus-visible:outline-none',
              resolvedSlotProps?.className,
            ),
          };
        },
        incrementButton: (ownerState) => {
          const resolvedSlotProps = resolveSlotProps(
            props.slotProps?.incrementButton,
            ownerState,
          );
          return {
            ...resolvedSlotProps,
            children: '▴',
            className: clsx(
              'font-[system-ui] flex flex-row flex-nowrap justify-center items-center appearance-none p-0 w-[19px] h-[19px] text-sm box-border leading-[1.2] border-0 bg-white dark:bg-slate-900 text-slate-900 dark:text-slate-300 transition-all duration-[120ms] hover:cursor-pointer hover:bg-slate-50 dark:hover:bg-slate-800 border-slate-300 dark:border-slate-600 col-start-2 col-end-3 row-start-1 row-end-2',
              resolvedSlotProps?.className,
            ),
          };
        },
        decrementButton: (ownerState) => {
          const resolvedSlotProps = resolveSlotProps(
            props.slotProps?.decrementButton,
            ownerState,
          );
          return {
            ...resolvedSlotProps,
            children: '▾',
            className: clsx(
              'font-[system-ui] flex flex-row flex-nowrap justify-center items-center appearance-none p-0 w-[19px] h-[19px] text-sm box-border leading-[1.2] border-0 bg-white dark:bg-slate-900 text-slate-900 dark:text-slate-300 transition-all duration-[120ms] hover:cursor-pointer hover:bg-slate-50 dark:hover:bg-slate-800 border-slate-300 dark:border-slate-600 col-start-2 col-end-3 row-start-2 row-end-3',
              resolvedSlotProps?.className,
            ),
          };
        },
      }}
    />
  );
});

NumberInput.propTypes = {
  /**
   * The props used for each slot inside the NumberInput.
   * @default {}
   */
  slotProps: PropTypes.shape({
    decrementButton: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    incrementButton: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    input: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
  }),
};