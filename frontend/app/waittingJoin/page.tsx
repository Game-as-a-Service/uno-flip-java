'use client';

import { useEffect, useRef, useState } from 'react';

export default function WaittingJoinHomepage() {

    const name = useRef<HTMLInputElement>(null).current?.value || '未命名的玩家';
    const playerId = useRef<HTMLInputElement>(null).current?.value || '01';
    const [number, setNumber] = useState(0);

    useEffect(() => {
        // 創建 EventSource 連接
        const eventSource = new EventSource("http://localhost:9090/sse/"+playerId);

        eventSource.onopen = () => {
            console.log('EventSource 連接已打開');
        };

        eventSource.onmessage = (event) => {
            console.log('收到事件：', event.data);
            const eventData = JSON.parse(event.data); 
            if(eventData.position){
            setNumber(eventData.position);}
        };

        eventSource.onerror = (error) => {
            console.error('EventSource 錯誤：', error);
        };

        return () => {
            eventSource.close();
        };

    }, []);
    return (
        <div className="flex flex-col justify-center items-center">
            <h1 className="mt-10">說明: 因為尚未決定玩家順序，不能表現其他玩家的相對位置</h1>
            <div className="border-gray-300 border-2 w-2/3 h-2/3 border-solid rounded-md p-10 m-10 flex flex-col justify-center items-center">
                <div className="border-sky-200 border-4 w-2/3 h-1/4 border-double rounded-md p-10 m-10 flex flex-col justify-center items-center">
                    <div className="text-center mb-auto">目前玩家人數: {number}</div>
                    <br />
                    <div className="text-center">等待其他玩家加入...</div>
                </div>

                <div className="border-2 border-red-300 border-solid rounded-md p-2 m-2 flex flex-col justify-center items-center" style={{ display: 'inline-block' }}>
                    <span className="text-center">{name}</span>
                </div>
            </div>

        </div>
    );
};
