import type {
  PFFParams,
  AllocationStrategyResult,
} from '@/lib/simulation/types';
import {
  ProcessExecutionState,
  computeThrashing,
} from '../processExecutionState';
export function simulatePFFControl(
  params: PFFParams
): AllocationStrategyResult {
  const {
    totalSystemFrames,
    globalReferences,
    processConfigs,
    localAlgorithm,
    thrashingWindowSize,
    thrashingThreshold,
    pffDeltaT,
    pffLowerBound,
    pffUpperBound,
    pffHighSuspendThreshold,
  } = params;
  const n = processConfigs.length;
  if (!n) {
    return {
      strategyName: 'PFF Control',
      name: 'PFF Control',
      totalPageFaults: 0,
      totalThrashingEvents: 0,
      totalSuspensions: 0,
      processMetrics: [],
      pageFaults: 0,
      thrashing: 0,
    };
  }
  const totalSi = processConfigs.reduce((a, p) => a + p.numVirtualPages, 0);
  const allocs: {
    id: number;
    pageOffset: number;
    numVirtualPages: number;
    allocatedFrames: number;
    faultQueue: number[];
    frameRange?: { start: number; end: number };
  }[] = processConfigs.map((p) => {
    const maxFrames = p.frameRange
      ? p.frameRange.end - p.frameRange.start + 1
      : totalSystemFrames;
    const proportionalFrames =
      totalSystemFrames > 0 && totalSi > 0
        ? Math.max(
            1,
            Math.floor((p.numVirtualPages / totalSi) * totalSystemFrames)
          )
        : 1;
    return {
      ...p,
      allocatedFrames: Math.min(proportionalFrames, maxFrames),
      faultQueue: [],
    };
  });
  let usedFrames = allocs.reduce((s, x) => s + x.allocatedFrames, 0);
  const freeFrames = () => totalSystemFrames - usedFrames;
  const states = allocs.map(
    (a) =>
      new ProcessExecutionState(
        a.id,
        { pageOffset: a.pageOffset },
        a.allocatedFrames,
        localAlgorithm,
        a.frameRange
      )
  );
  globalReferences.forEach((ref) => {
    const idx = allocs.findIndex((a) => a.id === ref.processId);
    const st = states[idx];
    if (!st.isSuspended) {
      const res = st.accessPage(ref.page);
      if (res.fault) {
        allocs[idx].faultQueue.push(ref.timestamp);
      }
      const fq = allocs[idx].faultQueue;
      while (fq.length && fq[0] <= ref.timestamp - pffDeltaT) {
        fq.shift();
      }
      const ppf = fq.length / pffDeltaT;
      if (ppf > pffUpperBound) {
        if (freeFrames() > 0) {
          st.adjustFrames(st.allocatedFramesCount + 1);
          usedFrames++;
        } else if (
          pffHighSuspendThreshold !== undefined &&
          ppf > pffHighSuspendThreshold
        ) {
          st.isSuspended = true;
          st.suspensionCount++;
        }
      } else if (ppf < pffLowerBound && st.allocatedFramesCount > 1) {
        st.adjustFrames(st.allocatedFramesCount - 1);
        usedFrames--;
      }
    } else {
      st.faultHistory.push(false);
    }
  });
  let totalPF = 0;
  let totalThr = 0;
  const pm = states.map((s) => {
    const thr = computeThrashing(
      s.faultHistory,
      thrashingWindowSize,
      thrashingThreshold
    );
    totalPF += s.pageFaults;
    totalThr += thr;
    return {
      processId: s.id,
      pageFaults: s.pageFaults,
      thrashingEvents: thr,
      suspensions: s.suspensionCount,
    };
  });
  return {
    strategyName: 'PFF Control',
    name: 'PFF Control',
    totalPageFaults: totalPF,
    totalThrashingEvents: totalThr,
    totalSuspensions: states.reduce((a, s) => a + s.suspensionCount, 0),
    processMetrics: pm,
    pageFaults: totalPF,
    thrashing: totalThr,
  };
}
