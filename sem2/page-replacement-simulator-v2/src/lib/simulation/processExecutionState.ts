import type { LocalPageReplacementAlgorithm } from '@/lib/simulation/types';
export interface PageAccessResult {
  fault: boolean;
  evictedPage?: number | null;
}
export class ProcessExecutionState {
  public physicalMemory: (number | null)[];
  private accessOrder: number[];
  private referenceBits?: Map<number, 0 | 1>;
  private scPointer?: number;
  public pageFaults = 0;
  public faultHistory: boolean[] = [];
  public isSuspended = false;
  public suspensionCount = 0;
  public workingSet = new Set<number>();
  public faultsInPFFWindow = 0;
  private pageAccessTimes = new Map<number, number>();
  private frameRange?: { start: number; end: number };
  constructor(
    public readonly id: number,
    public readonly config: { pageOffset: number },
    public allocatedFramesCount: number,
    private localAlgorithm: LocalPageReplacementAlgorithm,
    frameRange?: { start: number; end: number }
  ) {
    this.frameRange = frameRange;
    this.physicalMemory = Array(allocatedFramesCount).fill(null);
    this.accessOrder = [];
    if (localAlgorithm === 'SecondChance') {
      this.referenceBits = new Map();
      this.scPointer = 0;
    }
  }
  private get currentPagesInMem(): number[] {
    return this.physicalMemory.filter((p) => p !== null) as number[];
  }
  accessPage(page: number, timestamp: number = 0): PageAccessResult {
    if (this.isSuspended || this.allocatedFramesCount === 0) {
      this.faultHistory.push(false);
      return { fault: false };
    }
    let fault = false;
    let evictedPage: number | null = null;
    const inMem = this.physicalMemory.includes(page);

    this.workingSet.add(page);
    this.pageAccessTimes.set(page, timestamp);

    if (inMem) {
      if (this.localAlgorithm === 'LRU') {
        this.accessOrder = this.accessOrder.filter((p) => p !== page);
        this.accessOrder.push(page);
      } else if (this.localAlgorithm === 'SecondChance' && this.referenceBits) {
        this.referenceBits.set(page, 1);
      }
    } else {
      fault = true;
      this.pageFaults++;
      this.faultsInPFFWindow++;
      const curSize = this.currentPagesInMem.length;
      if (curSize < this.allocatedFramesCount) {
        const idx = this.physicalMemory.indexOf(null);
        this.physicalMemory[idx] = page;
        if (this.localAlgorithm === 'LRU' || this.localAlgorithm === 'FIFO') {
          this.accessOrder.push(page);
        }
        if (this.localAlgorithm === 'SecondChance' && this.referenceBits) {
          this.referenceBits.set(page, 1);
        }
      } else {
        let victimIdx = -1;
        let victimPage: number | null = null;
        switch (this.localAlgorithm) {
          case 'FIFO':
            if (this.accessOrder.length > 0) {
              victimPage = this.accessOrder.shift()!;
              victimIdx = this.physicalMemory.indexOf(victimPage);
            }
            break;
          case 'LRU':
            if (this.accessOrder.length > 0) {
              victimPage = this.accessOrder.shift()!;
              victimIdx = this.physicalMemory.indexOf(victimPage);
            }
            break;
          case 'RAND':
            victimIdx = Math.floor(Math.random() * this.allocatedFramesCount);
            victimPage = this.physicalMemory[victimIdx];
            break;
          case 'SecondChance':
            if (this.referenceBits && this.scPointer !== undefined) {
              const mem = this.physicalMemory;
              let attempts = 0;
              const maxAttempts = this.allocatedFramesCount * 2;
              while (attempts < maxAttempts) {
                const cand = mem[this.scPointer];
                if (cand === null) {
                  this.scPointer =
                    (this.scPointer + 1) % this.allocatedFramesCount;
                  attempts++;
                  continue;
                }
                if (this.referenceBits.get(cand) === 0) {
                  victimIdx = this.scPointer;
                  victimPage = cand;
                  this.referenceBits.delete(cand);
                  this.scPointer =
                    (this.scPointer + 1) % this.allocatedFramesCount;
                  break;
                } else {
                  this.referenceBits.set(cand, 0);
                  this.scPointer =
                    (this.scPointer + 1) % this.allocatedFramesCount;
                }
                attempts++;
              }
              if (victimIdx === -1) {
                victimIdx = this.scPointer;
                victimPage = this.physicalMemory[victimIdx];
                if (victimPage !== null) this.referenceBits.delete(victimPage);
                this.scPointer =
                  (this.scPointer + 1) % this.allocatedFramesCount;
              }
            }
            break;
        }
        if (victimIdx === -1 && this.allocatedFramesCount > 0) {
          victimIdx = 0;
          victimPage = this.physicalMemory[victimIdx];
          if (this.localAlgorithm === 'LRU' || this.localAlgorithm === 'FIFO') {
            if (this.accessOrder.length >= this.allocatedFramesCount) {
              this.accessOrder.shift();
            }
            this.accessOrder.push(page);
          }
          if (this.localAlgorithm === 'SecondChance' && this.referenceBits) {
            if (victimPage !== null) this.referenceBits.delete(victimPage);
            this.referenceBits.set(page, 1);
          }
        }
        if (victimIdx !== -1) {
          this.physicalMemory[victimIdx] = page;
          evictedPage = victimPage;
          if (this.localAlgorithm === 'LRU' || this.localAlgorithm === 'FIFO') {
            this.accessOrder.push(page);
          }
          if (this.localAlgorithm === 'SecondChance' && this.referenceBits) {
            this.referenceBits.set(page, 1);
          }
        }
      }
    }
    this.faultHistory.push(fault);
    return { fault, evictedPage };
  }
  adjustFrames(newCount: number): (number | null)[] {
    const evicted: (number | null)[] = [];
    if (newCount === this.allocatedFramesCount) return evicted;

    if (this.frameRange) {
      const maxFrames = this.frameRange.end - this.frameRange.start + 1;
      newCount = Math.min(newCount, maxFrames);
    }

    const curPages = [...this.currentPagesInMem];
    if (newCount < this.allocatedFramesCount) {
      let toEvict = this.allocatedFramesCount - newCount;
      let tempOrder = this.accessOrder.filter((p) => curPages.includes(p));
      for (let i = 0; i < toEvict && curPages.length > 0; i++) {
        const victim =
          tempOrder.length > 0 ? tempOrder.shift()! : curPages.pop()!;
        evicted.push(victim);
        const idx = curPages.indexOf(victim);
        if (idx > -1) curPages.splice(idx, 1);
        if (this.localAlgorithm === 'SecondChance' && this.referenceBits) {
          this.referenceBits.delete(victim);
        }
      }
      this.accessOrder = this.accessOrder.filter((p) => curPages.includes(p));
      this.physicalMemory = Array(newCount).fill(null);
      for (let i = 0; i < Math.min(newCount, curPages.length); i++) {
        this.physicalMemory[i] = curPages[i];
      }
    } else {
      while (this.physicalMemory.length < newCount) {
        this.physicalMemory.push(null);
      }
    }
    this.allocatedFramesCount = newCount;
    if (
      this.localAlgorithm === 'SecondChance' &&
      this.scPointer !== undefined
    ) {
      this.scPointer = this.scPointer % Math.max(1, newCount);
    }
    return evicted;
  }
  resetForPFFWindow() {
    this.faultsInPFFWindow = 0;
  }
  updateWorkingSet(currentTime: number, windowSize: number) {
    for (const [page, lastAccess] of this.pageAccessTimes.entries()) {
      if (currentTime - lastAccess > windowSize) {
        this.workingSet.delete(page);
        this.pageAccessTimes.delete(page);
      }
    }
  }
  clearWorkingSetAccumulator() {
    this.updateWorkingSet(Date.now(), 1000);
  }
}
export function computeThrashing(
  faults: boolean[],
  windowSize = 10,
  threshold = 5
): number {
  let incidents = 0;
  if (faults.length < windowSize || windowSize <= 0) return 0;

  const thrashingThreshold = Math.ceil(windowSize * 0.7);

  for (let start = 0; start + windowSize <= faults.length; start++) {
    let cnt = 0;
    for (let i = start; i < start + windowSize; i++) {
      if (faults[i]) cnt++;
    }
    if (cnt >= thrashingThreshold) incidents++;
  }
  return incidents / 4;
}
