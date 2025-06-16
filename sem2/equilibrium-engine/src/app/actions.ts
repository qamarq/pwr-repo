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
const DEFAULT_MAX_TASK_DEMAND = 10;

export async function runAllSimulationsAction(
  userParams: UserSimulationParams
): Promise<SimulationResults> {
  const params = {
    ...userParams,
    numTasksToSimulate: DEFAULT_NUM_TASKS_TO_SIMULATE,
    maxTaskDemand: DEFAULT_MAX_TASK_DEMAND,
  };

  // Introduce a small delay to simulate computation and allow UI to show loading state.
  // await new Promise(resolve => setTimeout(resolve, 500));

  // These calls can be run in parallel if they don't share mutable state
  // or if the simulation functions are pure / handle their own state initialization.
  // The current simulation.ts structure initializes state per call, so parallel is fine.
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
