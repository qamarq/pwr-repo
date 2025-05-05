// lib/generator.ts
export interface GenerationParams {
  totalVirtualPages: number;
  referenceStringLength: number;
  localityFactor: number; // How many pages roughly in a phase
  phaseLength: number; // How long a phase lasts
}

export function generateReferenceString(params: GenerationParams): number[] {
  const {
    totalVirtualPages,
    referenceStringLength,
    localityFactor,
    phaseLength,
  } = params;
  const references: number[] = [];
  let currentPhasePages: number[] = [];
  let phaseStartIndex = 0;

  // Helper to get a random subset of pages for a phase
  const getRandomPhasePages = (): number[] => {
    const pages = new Set<number>();
    const startPage = Math.floor(
      Math.random() * (totalVirtualPages - localityFactor + 1)
    );
    while (pages.size < localityFactor && pages.size < totalVirtualPages) {
      // Simple approach: pages close to startPage, can be refined
      const potentialPage =
        startPage + Math.floor(Math.random() * localityFactor);
      if (potentialPage < totalVirtualPages) {
        pages.add(potentialPage);
      }
      // Fallback if localityFactor is large or near the end
      if (pages.size < localityFactor) {
        pages.add(Math.floor(Math.random() * totalVirtualPages));
      }
    }
    return Array.from(pages);
  };

  currentPhasePages = getRandomPhasePages();

  for (let i = 0; i < referenceStringLength; i++) {
    // Check if phase needs to change
    if (i > 0 && i % phaseLength === 0) {
      currentPhasePages = getRandomPhasePages();
      phaseStartIndex = i;
    }

    // Select a page from the current phase set
    const pageIndex = Math.floor(Math.random() * currentPhasePages.length);
    references.push(currentPhasePages[pageIndex]);
  }

  return references;
}
