// src/lib/simulation/engine.ts

import type {
  PageReference,
  ProcessInfo,
  SimulationConfig,
  AllocationStrategyResult,
} from '@/lib/simulation/types';

import type {
  BaseSimParams,
  PFFParams,
  WorkingSetParams,
} from '@/lib/simulation/types';

import { simulateEqualAllocation } from './strategies/equal';
import { simulateProportionalAllocation } from './strategies/proportional';
import { simulatePFFControl } from './strategies/pff';
import { simulateWorkingSetModel } from './strategies/workingSet';

export function runAlgorithm(
  algorithmName: string,
  globalReferences: PageReference[],
  processes: ProcessInfo[],
  totalFrames: number,
  config: SimulationConfig
): AllocationStrategyResult {
  const generateFrameRanges = (numProcesses: number, totalFrames: number) => {
    const ranges: { start: number; end: number }[] = [];
    const minFramesPerProcess = Math.max(
      1,
      Math.floor(totalFrames / (numProcesses * 2))
    );
    let remainingFrames = totalFrames;
    let currentStart = 1;

    for (let i = 0; i < numProcesses; i++) {
      const isLastProcess = i === numProcesses - 1;
      let rangeSize: number;

      if (isLastProcess) {
        rangeSize = remainingFrames;
      } else {
        const maxPossibleSize =
          remainingFrames - minFramesPerProcess * (numProcesses - i - 1);
        rangeSize =
          Math.floor(
            Math.random() * (maxPossibleSize - minFramesPerProcess + 1)
          ) + minFramesPerProcess;
      }

      ranges.push({
        start: currentStart,
        end: currentStart + rangeSize - 1,
      });

      currentStart += rangeSize;
      remainingFrames -= rangeSize;
    }

    return ranges;
  };

  const frameRanges = generateFrameRanges(processes.length, totalFrames);

  const baseParams: BaseSimParams = {
    totalSystemFrames: totalFrames,
    globalReferences,
    processConfigs: processes.map((p, index) => ({
      id: p.id,
      numVirtualPages: p.pageCount,
      pageOffset: 0,
      frameRange: frameRanges[index],
    })),
    localAlgorithm: config.localAlgorithm,
    thrashingWindowSize: config.thrashingWindowW,
    thrashingThreshold: Math.floor(
      config.thrashingWindowW * config.thrashingThresholdEFactor
    ),
  };

  const name = algorithmName.toLowerCase();

  if (name.includes('equal')) {
    return simulateEqualAllocation(baseParams);
  }
  if (name.includes('proportional')) {
    return simulateProportionalAllocation(baseParams);
  }
  if (name.includes('pff')) {
    const pffParams: PFFParams = {
      ...baseParams,
      pffDeltaT: config.pffDeltaT,
      pffLowerBound: config.pffLowerThresholdL,
      pffUpperBound: config.pffUpperThresholdU,
      pffHighSuspendThreshold: config.pffSuspendThresholdH,
    };
    return simulatePFFControl(pffParams);
  }
  if (name.includes('working set')) {
    const wsParams: WorkingSetParams = {
      ...baseParams,
      wsDeltaT: config.wsDeltaT,
      wsCalculationInterval: config.wsCalculationIntervalC,
    };
    return simulateWorkingSetModel(wsParams);
  }
  return simulateEqualAllocation(baseParams);
}
