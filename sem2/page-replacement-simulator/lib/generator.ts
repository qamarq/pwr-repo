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

  const getRandomPhasePages = (): number[] => {
    const pages = new Set<number>();
    const startPage = Math.floor(
      Math.random() * (totalVirtualPages - localityFactor + 1)
    );
    while (pages.size < localityFactor && pages.size < totalVirtualPages) {
      const potentialPage =
        startPage + Math.floor(Math.random() * localityFactor);
      if (potentialPage < totalVirtualPages) {
        pages.add(potentialPage);
      }
      if (pages.size < localityFactor) {
        pages.add(Math.floor(Math.random() * totalVirtualPages));
      }
    }
    return Array.from(pages);
  };

  currentPhasePages = getRandomPhasePages();

  for (let i = 0; i < referenceStringLength; i++) {
    if (i > 0 && i % phaseLength === 0) {
      currentPhasePages = getRandomPhasePages();
      phaseStartIndex = i;
    }

    const pageIndex = Math.floor(Math.random() * currentPhasePages.length);
    references.push(currentPhasePages[pageIndex]);
  }

  return references;
}
