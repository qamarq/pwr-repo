import type {
  PageReference,
  ProcessInfo,
  SimulationConfig,
} from '@/lib/simulation/types';

// Generates a page reference string for a single process
function generateProcessReferenceString(
  processId: number,
  basePage: number, // Starting page number for this process to ensure disjoint sets
  numPagesInProcess: number, // Max unique pages this process can reference
  refLength: number,
  localityFactor: number,
  localityWindowSize: number
): PageReference[] {
  const references: PageReference[] = [];
  let hotSet: number[] = [];
  let lastSwitch = 0;

  const switchHotSet = () => {
    hotSet = [];
    const startPage =
      basePage +
      Math.floor(Math.random() * (numPagesInProcess - localityWindowSize + 1));
    for (let i = 0; i < localityWindowSize; i++) {
      hotSet.push(startPage + i);
    }
  };

  switchHotSet(); // Initial hot set

  for (let i = 0; i < refLength; i++) {
    // Potentially switch hot set every N references or randomly
    if (
      i - lastSwitch > Math.max(10, localityWindowSize * 2) &&
      Math.random() < 0.1
    ) {
      switchHotSet();
      lastSwitch = i;
    }

    let page: number;
    if (Math.random() < localityFactor && hotSet.length > 0) {
      // Reference from hot set
      page = hotSet[Math.floor(Math.random() * hotSet.length)];
    } else {
      // Reference a random page from the process's allowed range
      page = basePage + Math.floor(Math.random() * numPagesInProcess);
    }
    references.push({ processId, page, timestamp: 0 }); // Timestamp will be set globally
  }
  return references;
}

// Generates page references for all processes and a global interleaved stream
export function generateSimulationData(config: SimulationConfig): {
  globalReferenceString: PageReference[];
  processInfos: ProcessInfo[];
} {
  const processInfos: ProcessInfo[] = [];
  let currentPageOffset = 0;
  const allProcessLocalReferences: PageReference[][] = [];

  for (let i = 0; i < config.numProcesses; i++) {
    const refLength =
      config.minPageRefLength +
      Math.floor(
        Math.random() * (config.maxPageRefLength - config.minPageRefLength + 1)
      );
    const processRefs = generateProcessReferenceString(
      i,
      currentPageOffset,
      config.maxPagesPerProcess,
      refLength,
      config.localityFactor,
      config.localityWindowSize
    );

    const uniquePages = new Set(processRefs.map((r) => r.page));

    processInfos.push({
      id: i,
      pageCount: uniquePages.size,
      referenceString: processRefs, // Store local string before timestamping
    });
    allProcessLocalReferences.push(processRefs);
    currentPageOffset += config.maxPagesPerProcess; // Ensure disjoint page numbers
  }

  // Interleave process references to create a global reference string
  const globalReferenceString: PageReference[] = [];
  const processCursors = new Array(config.numProcesses).fill(0);
  let globalTimestamp = 0;
  let activeProcesses = config.numProcesses;

  while (activeProcesses > 0) {
    for (let i = 0; i < config.numProcesses; i++) {
      if (processCursors[i] < allProcessLocalReferences[i].length) {
        // Simple round-robin: take one reference from each active process per global "tick"
        const localRef = allProcessLocalReferences[i][processCursors[i]];
        globalReferenceString.push({
          ...localRef,
          timestamp: globalTimestamp,
        });
        processCursors[i]++;
        if (processCursors[i] >= allProcessLocalReferences[i].length) {
          activeProcesses--;
        }
        globalTimestamp++; // Increment global time after each reference
      }
    }
    if (activeProcesses === 0) break; // All streams exhausted
  }

  // Update processInfos with their actual reference strings that now have global timestamps
  // This step might be redundant if localRef.timestamp was updated above directly and used.
  // For clarity, let's ensure ProcessInfo reflects the global timestamp.
  // However, the problem describes individual process strings and then a global one.
  // The `processInfos[i].referenceString` should ideally be the local sequence,
  // and `globalReferenceString` is the one used for simulation.
  // Let's keep processInfos.referenceString as the original local sequence without global timestamps for now.

  return { globalReferenceString, processInfos };
}
