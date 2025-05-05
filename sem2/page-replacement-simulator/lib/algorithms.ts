// lib/algorithms.ts

export interface SimulationResult {
  pageFaults: number;
  finalFrames: (number | null)[]; // State of frames at the end
}

// --- FIFO ---
export function simulateFIFO(
  frames: number,
  references: number[]
): SimulationResult {
  const physicalMemory: number[] = [];
  let pageFaults = 0;

  for (const page of references) {
    if (!physicalMemory.includes(page)) {
      pageFaults++;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page);
      } else {
        physicalMemory.shift(); // Remove the oldest (first added)
        physicalMemory.push(page);
      }
    }
    // No action if page is already in memory
  }
  // Pad with null if memory not full
  while (physicalMemory.length < frames) physicalMemory.push(null);
  return { pageFaults, finalFrames: physicalMemory };
}

// --- OPT (Optimal) --- [cite: 20]
export function simulateOPT(
  frames: number,
  references: number[]
): SimulationResult {
  const physicalMemory: number[] = [];
  let pageFaults = 0;

  for (let i = 0; i < references.length; i++) {
    const page = references[i];
    if (!physicalMemory.includes(page)) {
      pageFaults++;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page);
      } else {
        // Find the page in memory that will be used furthest in the future
        let pageToReplace = -1;
        let furthestUseIndex = -1;

        physicalMemory.forEach((memPage) => {
          let nextUseIndex = references
            .slice(i + 1)
            .findIndex((ref) => ref === memPage);
          if (nextUseIndex === -1) {
            // This page is not used again
            pageToReplace = memPage;
            furthestUseIndex = Infinity; // Prioritize removing this
          } else if (
            nextUseIndex > furthestUseIndex &&
            furthestUseIndex !== Infinity
          ) {
            furthestUseIndex = nextUseIndex;
            pageToReplace = memPage;
          } else if (pageToReplace === -1) {
            // Initialize if first page checked
            pageToReplace = memPage;
            furthestUseIndex = nextUseIndex;
          }
        });

        const replaceIndex = physicalMemory.indexOf(pageToReplace);
        physicalMemory[replaceIndex] = page;
      }
    }
  }
  while (physicalMemory.length < frames) physicalMemory.push(null);
  return { pageFaults, finalFrames: physicalMemory };
}

// --- LRU (Least Recently Used) --- [cite: 25]
export function simulateLRU(
  frames: number,
  references: number[]
): SimulationResult {
  const physicalMemory: number[] = []; // Order matters: most recently used at the end
  let pageFaults = 0;

  for (const page of references) {
    const pageIndex = physicalMemory.indexOf(page);

    if (pageIndex === -1) {
      // Page fault
      pageFaults++;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page); // Add to end (most recent)
      } else {
        physicalMemory.shift(); // Remove the least recently used (at the beginning)
        physicalMemory.push(page); // Add new page to end
      }
    } else {
      // Page hit: Move page to the end (mark as most recently used)
      physicalMemory.splice(pageIndex, 1);
      physicalMemory.push(page);
    }
  }
  while (physicalMemory.length < frames) physicalMemory.push(null);
  return { pageFaults, finalFrames: physicalMemory };
}

// --- Approximated LRU (Second Chance) --- [cite: 32, 35]
interface SecondChancePage {
  pageNumber: number;
  referenceBit: 0 | 1;
}
export function simulateSecondChance(
  frames: number,
  references: number[]
): SimulationResult {
  const physicalMemory: SecondChancePage[] = [];
  let pointer = 0; // FIFO pointer
  let pageFaults = 0;

  for (const page of references) {
    const existingPageIndex = physicalMemory.findIndex(
      (p) => p.pageNumber === page
    );

    if (existingPageIndex !== -1) {
      // Page hit: set reference bit to 1
      physicalMemory[existingPageIndex].referenceBit = 1;
    } else {
      // Page fault
      pageFaults++;

      if (physicalMemory.length < frames) {
        // Still space, add to memory (like FIFO initially)
        physicalMemory.push({ pageNumber: page, referenceBit: 1 }); // Start with bit 1? Or 0? Doc implies add then set bit [cite: 35] - let's set to 1 on load
      } else {
        // Memory full, need replacement using Second Chance
        while (true) {
          const candidate = physicalMemory[pointer];
          if (candidate.referenceBit === 0) {
            // Replace this page
            physicalMemory[pointer] = { pageNumber: page, referenceBit: 1 }; // New page gets bit 1
            pointer = (pointer + 1) % frames; // Move pointer
            break; // Exit loop
          } else {
            // Give second chance
            candidate.referenceBit = 0; // Clear bit
            pointer = (pointer + 1) % frames; // Move pointer and check next
          }
        }
      }
    }
  }
  const finalFrames = physicalMemory.map((p) => p.pageNumber);
  while (finalFrames.length < frames) finalFrames.push(null);
  return { pageFaults, finalFrames };
}

// --- RAND (Random) --- [cite: 45]
export function simulateRAND(
  frames: number,
  references: number[]
): SimulationResult {
  const physicalMemory: number[] = [];
  let pageFaults = 0;

  for (const page of references) {
    if (!physicalMemory.includes(page)) {
      pageFaults++;
      if (physicalMemory.length < frames) {
        physicalMemory.push(page);
      } else {
        const randomIndex = Math.floor(Math.random() * frames);
        physicalMemory[randomIndex] = page; // Replace random page
      }
    }
  }
  while (physicalMemory.length < frames) physicalMemory.push(null);
  return { pageFaults, finalFrames: physicalMemory };
}
