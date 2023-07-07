import { render, screen } from '@testing-library/react';
import App from '../app/page';
import React from 'react';

describe('App', () => {
  it('renders without crashing', () => {
    const { container } = render(<App />);
    expect(container.querySelector('div')).toBeInTheDocument();
    // render(<App />);
    // expect(screen.getByText('UNO')).toBeInTheDocument();
  });
});
