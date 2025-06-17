'use server';

import type {
  UserSimulationParams,
  SimulationResults,
  Metrics,
  StrategyType,
} from '@/types';
import {
  applyStrategy1,
  applyStrategy2,
  applyStrategy3,
} from '@/lib/simulation';

const DEFAULT_NUM_TASKS_TO_SIMULATE = 10000;
const DEFAULT_MAX_TASK_DEMAND = 1000;

export async function runAllSimulationsAction(
  userParams: UserSimulationParams
): Promise<SimulationResults> {
  const params = {
    ...userParams,
    numTasksToSimulate: DEFAULT_NUM_TASKS_TO_SIMULATE,
    maxTaskDemand: DEFAULT_MAX_TASK_DEMAND,
  };

  const [strategy1Metrics, strategy2Metrics, strategy3Metrics] =
    await Promise.all([
      applyStrategy1(params),
      applyStrategy2(params),
      applyStrategy3(params),
    ]);

  return {
    strategy1: strategy1Metrics,
    strategy2: strategy2Metrics,
    strategy3: strategy3Metrics,
  };
}
