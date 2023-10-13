import { render, screen } from '@testing-library/react';
import React from 'react';
import WaittingJoinHomepage from '@/app/waittingJoin/page';
import fetchMock from 'jest-fetch-mock';



beforeEach(() => {
  fetchMock.resetMocks();
});

describe('WaittingJoinHomepage', () => {
  it('should update player count when a message is received', async () => {
    const mockData = { position: 3 };
    
    fetchMock.mockResponse(JSON.stringify(mockData));
    
    render(<WaittingJoinHomepage />);
    
    // 模拟 EventSource 的事件回调
    const playerCount = await screen.findByText('目前玩家人數: 3');
    expect(playerCount).toBeInTheDocument();
  });
});