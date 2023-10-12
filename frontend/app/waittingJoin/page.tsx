'use client';

import { useEffect, useRef } from 'react';

export default function WaittingJoinHomepage() {

    const name = useRef<HTMLInputElement>(null).current?.value || '未命名的玩家';
    const number = 1;
    useEffect(() => {
        // 創建 WebSocket 連接
        const ws = new WebSocket("ws://localhost:9090/sse/02");
    
        ws.onopen = () => {
            console.log('register');
        };
    
        // 監聽事件
        ws.onmessage = (event) => {
            console.log('test2');
            const data = JSON.parse(event.data);
            console.log(data);
        };
        ws.onclose = () => {
            if (ws.readyState === WebSocket.CLOSED) {
                // 連接已關閉，執行其他操作
                console.log('連接已關閉');
            }
        };
        return () => {
            // 在組件卸載時關閉 WebSocket 連接
            ws.close();
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
