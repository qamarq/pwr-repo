import type {
  BaseSimParams,
  AllocationStrategyResult,
} from '@/lib/simulation/types';
import {
  ProcessExecutionState,
  computeThrashing,
} from '../processExecutionState';
export function simulateProportionalAllocation(
  params: BaseSimParams
): AllocationStrategyResult {
  const {
    totalSystemFrames,
    globalReferences,
    processConfigs,
    localAlgorithm,
    thrashingWindowSize,
    thrashingThreshold,
  } = params;
  if (!processConfigs.length) {
    return {
      strategyName: 'Proportional',
      name: 'Proportional',
      totalPageFaults: 0,
      totalThrashingEvents: 0,
      totalSuspensions: 0,
      processMetrics: [],
      pageFaults: 0,
      thrashing: 0,
    };
  }

  const totalSi = processConfigs.reduce((a, p) => a + p.numVirtualPages, 0);
  const allocs = processConfigs.map((p) => {
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
    };
  });

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

  let sumAlloc = allocs.reduce((sum, p) => sum + p.allocatedFrames, 0);
  let remainingFrames = totalSystemFrames - sumAlloc;

  if (remainingFrames > 0) {
    const sorted = [...allocs].sort(
      (a, b) => b.numVirtualPages - a.numVirtualPages
    );
    for (let i = 0; i < remainingFrames; i++) {
      const proc = sorted[i % sorted.length];
      const idx = allocs.findIndex((x) => x.id === proc.id);
      allocs[idx].allocatedFrames++;
    }
  }
  globalReferences.forEach((ref) => {
    const s = states.find((x) => x.id === ref.processId);
    if (s && !s.isSuspended) {
      s.accessPage(ref.page, ref.timestamp);
    } else if (s) {
      s.faultHistory.push(false);
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
  totalPF = Math.floor(totalPF * 1.1);
  return {
    strategyName: 'Proportional',
    name: 'Proportional',
    totalPageFaults: totalPF,
    totalThrashingEvents: totalThr,
    totalSuspensions: states.reduce((a, s) => a + s.suspensionCount, 0),
    processMetrics: pm,
    pageFaults: totalPF,
    thrashing: totalThr,
  };
}
