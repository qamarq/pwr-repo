import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';
import { Request } from './types';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function calculateMovement(start: number, end: number): number {
  return Math.abs(start - end);
}

export function deepCopyRequests(requests: Request[]): Request[] {
  return requests.map((req) => ({ ...req }));
}

export function generateEdfBenchmark() {
  // 150 requestow z czego 50% to RT
  const requests = [];

  // Grupa 1: 50 żądań RT z deadline'ami 5-30 (krytyczne)
  for (let i = 0; i < 50; i++) {
    const cylinder = Math.floor(Math.random() * 300);
    // Krótkie deadline'y - wysokie priorytety
    const deadline = Math.floor(Math.random() * 25) + 5;
    requests.push(`${cylinder}:true:${deadline}`);
  }

  // Grupa 2: 25 żądań RT z deadline'ami 30-60 (średnie)
  for (let i = 0; i < 25; i++) {
    const cylinder = Math.floor(Math.random() * 300);
    const deadline = Math.floor(Math.random() * 30) + 30;
    requests.push(`${cylinder}:true:${deadline}`);
  }

  // Grupa 3: 75 zwykłych żądań
  for (let i = 0; i < 75; i++) {
    const cylinder = Math.floor(Math.random() * 300);
    requests.push(`${cylinder}:false`);
  }

  return shuffle(requests).join(', ');
}

// Funkcja generująca test dla FD-SCAN - skupiska RT w różnych częściach dysku
export function generateFdScanBenchmark() {
  const requests = [];

  // Grupa 1: Skupisko RT na początku dysku (cylindry 0-100)
  for (let i = 0; i < 30; i++) {
    const cylinder = Math.floor(Math.random() * 100);
    // Zróżnicowane deadline'y, niektóre mogą być niewykonalne
    const deadline = Math.floor(Math.random() * 450) + 110;
    requests.push(`${cylinder}:true:${deadline}`);
  }

  // Grupa 2: Skupisko RT w środku dysku (cylindry 100-200)
  for (let i = 0; i < 30; i++) {
    const cylinder = Math.floor(Math.random() * 100) + 100;
    const deadline = Math.floor(Math.random() * 560) + 220;
    requests.push(`${cylinder}:true:${deadline}`);
  }

  // Grupa 3: Skupisko RT na końcu dysku (cylindry 200-300)
  for (let i = 0; i < 30; i++) {
    const cylinder = Math.floor(Math.random() * 100) + 200;
    const deadline = Math.floor(Math.random() * 670) + 330;
    requests.push(`${cylinder}:true:${deadline}`);
  }

  // Grupa 4: Zwykłe żądania rozproszone po całym dysku
  for (let i = 0; i < 60; i++) {
    const cylinder = Math.floor(Math.random() * 300);
    requests.push(`${cylinder}:false`);
  }

  return shuffle(requests).join(', ');
}

export function generateEdgeRequests() {
  const requests = [];

  // 50 żądań na lewej krawędzi (cylindry 0-20)
  for (let i = 0; i < 50; i++) {
    const cylinder = Math.floor(Math.random() * 21); // 0-20
    const isRT = Math.random() < 0.3; // 30% żądań RT

    if (isRT) {
      const deadline = Math.floor(Math.random() * 550) + 250;
      requests.push(`${cylinder}:true:${deadline}`);
    } else {
      requests.push(`${cylinder}:false`);
    }
  }

  // 50 żądań na prawej krawędzi (cylindry 280-300)
  for (let i = 0; i < 50; i++) {
    const cylinder = Math.floor(Math.random() * 21) + 279; // 280-300
    const isRT = Math.random() < 0.3; // 30% żądań RT

    if (isRT) {
      const deadline = Math.floor(Math.random() * 550) + 250;
      requests.push(`${cylinder}:true:${deadline}`);
    } else {
      requests.push(`${cylinder}:false`);
    }
  }

  return shuffle(requests).join(', ');
}

export function generateCenterRequests() {
  const requests = [];

  // 100 żądań skupionych wokół środka (cylindry 140-160)
  for (let i = 0; i < 100; i++) {
    const cylinder = Math.floor(Math.random() * 21) + 140; // 140-160
    const isRT = Math.random() < 0.3; // 30% żądań RT

    if (isRT) {
      const deadline = Math.floor(Math.random() * 540) + 210;
      requests.push(`${cylinder}:true:${deadline}`);
    } else {
      requests.push(`${cylinder}:false`);
    }
  }

  return shuffle(requests).join(', ');
}

export function generateAfterHeadRequests() {
  const requests = [];

  // 100 żądań za pozycją głowicy (cylindry 51-300)
  for (let i = 0; i < 100; i++) {
    const cylinder = Math.floor(Math.random() * 250) + 51; // 51-300
    const isRT = Math.random() < 0.3; // 30% żądań RT

    if (isRT) {
      const deadline = Math.floor(Math.random() * 60) + 10;
      requests.push(`${cylinder}:true:${deadline}`);
    } else {
      requests.push(`${cylinder}:false`);
    }
  }

  return shuffle(requests).join(', ');
}

export function generateRealTimeAfterHeadRequests() {
  const requests = [];

  // Grupa 1: Żądania RT z krótkimi deadline'ami (cylindry 51-150)
  for (let i = 0; i < 40; i++) {
    const cylinder = Math.floor(Math.random() * 100) + 51; // 51-150
    const deadline = Math.floor(Math.random() * 20) + 5; // krótkie deadline'y 5-24
    requests.push(`${cylinder}:true:${deadline}`);
  }

  // Grupa 2: Żądania RT ze średnimi deadline'ami (cylindry 151-250)
  for (let i = 0; i < 30; i++) {
    const cylinder = Math.floor(Math.random() * 100) + 151; // 151-250
    const deadline = Math.floor(Math.random() * 30) + 25; // średnie deadline'y 25-54
    requests.push(`${cylinder}:true:${deadline}`);
  }

  // Grupa 3: Żądania RT z długimi deadline'ami (cylindry 251-300)
  for (let i = 0; i < 30; i++) {
    const cylinder = Math.floor(Math.random() * 50) + 251; // 251-300
    const deadline = Math.floor(Math.random() * 40) + 55; // długie deadline'y 55-94
    requests.push(`${cylinder}:true:${deadline}`);
  }

  return shuffle(requests).join(', ');
}

function shuffle(array: any[]) {
  const result = [...array];
  for (let i = result.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [result[i], result[j]] = [result[j], result[i]];
  }
  return result;
}
