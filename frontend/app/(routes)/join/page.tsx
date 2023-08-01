'use client';
import React, { useRef, useState } from 'react';

const Join = () => {
  const nameRef = useRef<HTMLInputElement>(null);
  const [isNameValid, setIsNameValid] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const inputChangeHandler = () => {
    const name = nameRef.current?.value || '';
    const valid = name.length > 0;
    setIsNameValid(valid);
  };

  const buttonClickHandler = () => {
    setIsLoading(true);
    console.log('btn clicked', nameRef.current?.value);

    const timeout = setTimeout(() => {
      setIsLoading(false);
    }, 1000);
    return () => {
      clearTimeout(timeout);
    };
  };

  return (
    <div className="flex flex-col justify-center items-center">
      <h1 className="mt-10">說明: 玩家需輸入暱稱，才能進入遊戲</h1>
      <div className="border-gray-300 border-2 w-2/3 h-96 border-solid rounded-md p-10 m-10 flex flex-col justify-center items-center">
        <div className="flex justify-center items-center space-x-3">
          <div className="">暱稱: </div>
          <input
            ref={nameRef}
            onChange={inputChangeHandler}
            className="rounded-md focus:outline-none text-black px-2 py-1"
          ></input>
        </div>
        <div className="flex justify-center">
          <button
            onClick={buttonClickHandler}
            disabled={!isNameValid}
            className="text-xl w-40 bg-teal-700 px-5 py-2 mt-10 rounded-md hover:opacity-80 hover:scale-105 duration-500 disabled:bg-gray-500 disabled:scale-100 disabled:opacity-100"
          >
            {isLoading ? (
              <div role="status">
                <svg
                  aria-hidden="true"
                  className="inline w-8 h-8 mr-2 text-gray-200 animate-spin dark:text-gray-600 fill-gray-600 dark:fill-gray-300"
                  viewBox="0 0 100 101"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                    fill="currentColor"
                  />
                  <path
                    d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                    fill="currentFill"
                  />
                </svg>
                <span className="sr-only">Loading...</span>
              </div>
            ) : (
              `進入遊戲`
            )}
          </button>
          <div className=""></div>
        </div>
      </div>
    </div>
  );
};

export default Join;
