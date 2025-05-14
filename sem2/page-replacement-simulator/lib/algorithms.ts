export interface SimulationResult {
  pageFaults: number;
  thrashing: number;
  finalFrames: (number | null)[];
}

function computeThrashing(
  faults: boolean[],
  windowSize: number = 5,
  threshold: number = 4
): number {
  let thrashing = 0;
  const step = Math.floor(windowSize / 2);
  for (let start = 0; start + windowSize <= faults.length; start += step) {
    let count = 0;
    for (let i = start; i < start + windowSize; i++) {
      if (faults[i]) count++;
    }
    if (count >= threshold) thrashing++;
  }
  return thrashing;
}

export function simulateFIFO(
  frames: number,
  references: number[],
  windowSize: number = 5,
  threshold: number = 4
): SimulationResult {
  const physicalMemory: number[] = [];
  const faults: boolean[] = [];
  let pageFaults = 0;
  for (let i = 0; i < references.length; i++) {
    const page = references[i];
    if (!physicalMemory.includes(page)) {
      pageFaults++;
      faults[i] = true;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page);
      } else {
        physicalMemory.shift();
        physicalMemory.push(page);
      }
    } else {
      faults[i] = false;
    }
  }
  while (physicalMemory.length < frames) physicalMemory.push(null);
  const thrashing = computeThrashing(faults, windowSize, threshold);
  return { pageFaults, thrashing, finalFrames: physicalMemory };
}

export function simulateOPT(
  frames: number,
  references: number[],
  windowSize: number = 5,
  threshold: number = 4
): SimulationResult {
  const physicalMemory: number[] = [];
  const faults: boolean[] = [];
  let pageFaults = 0;
  for (let i = 0; i < references.length; i++) {
    const page = references[i];
    if (!physicalMemory.includes(page)) {
      pageFaults++;
      faults[i] = true;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page);
      } else {
        let pageToReplace = -1;
        let furthest = -1;
        physicalMemory.forEach((memPage) => {
          const nextUse = references
            .slice(i + 1)
            .findIndex((ref) => ref === memPage);
          if (nextUse === -1) {
            pageToReplace = memPage;
            furthest = Infinity;
          } else if (furthest !== Infinity && nextUse > furthest) {
            furthest = nextUse;
            pageToReplace = memPage;
          } else if (pageToReplace === -1) {
            pageToReplace = memPage;
            furthest = nextUse;
          }
        });
        const idx = physicalMemory.indexOf(pageToReplace);
        physicalMemory[idx] = page;
      }
    } else {
      faults[i] = false;
    }
  }
  while (physicalMemory.length < frames) physicalMemory.push(null);
  const thrashing = computeThrashing(faults, windowSize, threshold);
  return { pageFaults, thrashing, finalFrames: physicalMemory };
}

export function simulateLRU(
  frames: number,
  references: number[],
  windowSize: number = 5,
  threshold: number = 4
): SimulationResult {
  const physicalMemory: number[] = [];
  const faults: boolean[] = [];
  let pageFaults = 0;
  for (let i = 0; i < references.length; i++) {
    const page = references[i];
    const idx = physicalMemory.indexOf(page);
    if (idx === -1) {
      pageFaults++;
      faults[i] = true;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page);
      } else {
        physicalMemory.shift();
        physicalMemory.push(page);
      }
    } else {
      faults[i] = false;
      physicalMemory.splice(idx, 1);
      physicalMemory.push(page);
    }
  }
  while (physicalMemory.length < frames) physicalMemory.push(null);
  const thrashing = computeThrashing(faults, windowSize, threshold);
  return { pageFaults, thrashing, finalFrames: physicalMemory };
}

interface SecondChancePage {
  pageNumber: number;
  referenceBit: 0 | 1;
}
export function simulateSecondChance(
  frames: number,
  references: number[],
  windowSize: number = 5,
  threshold: number = 4
): SimulationResult {
  const physicalMemory: SecondChancePage[] = [];
  const faults: boolean[] = [];
  let pointer = 0;
  let pageFaults = 0;
  for (let i = 0; i < references.length; i++) {
    const page = references[i];
    const idx = physicalMemory.findIndex((p) => p.pageNumber === page);
    if (idx !== -1) {
      faults[i] = false;
      physicalMemory[idx].referenceBit = 1;
    } else {
      pageFaults++;
      faults[i] = true;
      if (physicalMemory.length < frames) {
        physicalMemory.push({ pageNumber: page, referenceBit: 1 });
      } else {
        while (true) {
          const cand = physicalMemory[pointer];
          if (cand.referenceBit === 0) {
            physicalMemory[pointer] = {
              pageNumber: page,
              referenceBit: 1,
            };
            pointer = (pointer + 1) % frames;
            break;
          } else {
            cand.referenceBit = 0;
            pointer = (pointer + 1) % frames;
          }
        }
      }
    }
  }
  const final = physicalMemory.map((p) => p.pageNumber);
  while (final.length < frames) final.push(null);
  const thrashing = computeThrashing(faults, windowSize, threshold);
  return { pageFaults, thrashing, finalFrames: final };
}

export function simulateRAND(
  frames: number,
  references: number[],
  windowSize: number = 5,
  threshold: number = 4
): SimulationResult {
  const physicalMemory: number[] = [];
  const faults: boolean[] = [];
  let pageFaults = 0;
  for (let i = 0; i < references.length; i++) {
    const page = references[i];
    if (!physicalMemory.includes(page)) {
      pageFaults++;
      faults[i] = true;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page);
      } else {
        const ri = Math.floor(Math.random() * frames);
        physicalMemory[ri] = page;
      }
    } else {
      faults[i] = false;
    }
  }
  while (physicalMemory.length < frames) physicalMemory.push(null);
  const thrashing = computeThrashing(faults, windowSize, threshold);
  return { pageFaults, thrashing, finalFrames: physicalMemory };
}
