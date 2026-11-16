import React from 'react';
import patternImg from '../assets/triangle-pattern.png';

export default function TrianglePattern({ colorMode = 'white' }) {
  const isBlue = colorMode === 'blue';

  return (
    <div
      style={{
        position: 'fixed',
        bottom: 0,
        right: 0,
        width: '52vw',
        maxWidth: '750px',
        maxHeight: '75vh',
        pointerEvents: 'none',
        zIndex: 0,
        overflow: 'hidden',
        display: 'flex',
        alignItems: 'flex-end',
        justifyContent: 'flex-end',
      }}
    >
      <img
        src={patternImg}
        alt="Triangle Pattern"
        style={{
          width: '100%',
          height: 'auto',
          maxHeight: '100%',
          objectFit: 'contain',
          objectPosition: 'bottom right',
          opacity: isBlue ? 0.38 : 0.95,
          mixBlendMode: isBlue ? 'screen' : 'normal',
          filter: isBlue ? 'contrast(125%) brightness(108%)' : 'none',
          userSelect: 'none',
          pointerEvents: 'none',
        }}
      />
    </div>
  );
}
