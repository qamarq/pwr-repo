// src/app/icon.tsx
import { ImageResponse } from 'next/og';

export const runtime = 'edge';
export const size = { width: 32, height: 32 };
export const contentType = 'image/png';

export default function Icon() {
  return new ImageResponse(
    (
      <div
        style={{
          background: 'hsl(207 90% 69%)', // Primary color
          width: '100%',
          height: '100%',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'space-around',
          borderRadius: '6px',
          padding: '5px',
          boxSizing: 'border-box',
        }}>
        <div
          style={{
            width: '70%',
            height: '20%',
            background: 'hsl(210 20% 96%)',
            borderRadius: '2px',
          }}
        />
        <div
          style={{
            width: '70%',
            height: '20%',
            background: 'hsl(210 20% 96%)',
            borderRadius: '2px',
          }}
        />
        <div
          style={{
            width: '70%',
            height: '20%',
            background: 'hsl(210 20% 96%)',
            borderRadius: '2px',
          }}
        />
      </div>
    ),
    size
  );
}
