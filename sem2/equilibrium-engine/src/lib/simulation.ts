import type { Processor, Task, SimulationParams, Metrics } from '@/types';

const DEFAULT_NUM_TASKS_TO_SIMULATE = 10000;
const DEFAULT_MAX_TASK_DEMAND = 10;

function initializeProcessors(N: number): Processor[] {
  return Array.from({ length: N }, (_, id) => ({
    id,
    tasks: [],
    currentLoad: 0,
  }));
}

function calculateFinalLoadMetrics(processors: Processor[]): {
  averageLoad: number;
  stdDevLoad: number;
} {
  if (processors.length === 0) return { averageLoad: 0, stdDevLoad: 0 };

  const totalLoad = processors.reduce((sum, p) => sum + p.currentLoad, 0);
  const averageLoad = totalLoad / processors.length;

  const sumOfSquaredDifferences = processors.reduce((sum, p) => {
    return sum + Math.pow(p.currentLoad - averageLoad, 2);
  }, 0);
  const stdDevLoad = Math.sqrt(sumOfSquaredDifferences / processors.length);

  return {
    averageLoad: parseFloat(averageLoad.toFixed(2)),
    stdDevLoad: parseFloat(stdDevLoad.toFixed(2)),
  };
}

function getRandomProcessorIndex(N: number, excludeIndex: number = -1): number {
  if (N <= 1 && excludeIndex !== -1) {
    throw new Error(
      'Cannot select another processor if N <= 1 and an exclusion is requested.'
    );
  }
  let randomIndex: number;
  do {
    randomIndex = Math.floor(Math.random() * N);
  } while (randomIndex === excludeIndex);
  return randomIndex;
}

function strategy1Core(
  processors: Processor[],
  newTask: Task,
  originatingProcessorIndex: number,
  params: SimulationParams,
  metrics: { queries: number; migrations: number }
): void {
  let attempts = 0;
  let migrated = false;
  const { N, p, z } = params;

  if (N <= 1) {
    processors[originatingProcessorIndex].tasks.push(newTask);
    processors[originatingProcessorIndex].currentLoad += newTask.demand;
    return;
  }

  while (attempts < z && !migrated) {
    const targetProcessorIndex = getRandomProcessorIndex(
      N,
      originatingProcessorIndex
    );
    metrics.queries++;
    if (processors[targetProcessorIndex].currentLoad < p) {
      processors[targetProcessorIndex].tasks.push(newTask);
      processors[targetProcessorIndex].currentLoad += newTask.demand;
      metrics.migrations++;
      migrated = true;
    }
    attempts++;
  }

  if (!migrated) {
    processors[originatingProcessorIndex].tasks.push(newTask);
    processors[originatingProcessorIndex].currentLoad += newTask.demand;
  }
}

function strategy2Core(
  processors: Processor[],
  newTask: Task,
  originatingProcessorIndex: number,
  params: SimulationParams,
  metrics: { queries: number; migrations: number }
): void {
  const { N, p } = params;

  if (processors[originatingProcessorIndex].currentLoad > p && N > 1) {
    const hasCandidate = processors.some(
      (proc, idx) => idx !== originatingProcessorIndex && proc.currentLoad < p
    );
    if (!hasCandidate) {
      processors[originatingProcessorIndex].tasks.push(newTask);
      processors[originatingProcessorIndex].currentLoad += newTask.demand;
    } else {
      let targetProcessorIndex: number;
      do {
        targetProcessorIndex = getRandomProcessorIndex(
          N,
          originatingProcessorIndex
        );
        metrics.queries++;
      } while (processors[targetProcessorIndex].currentLoad >= p);
      processors[targetProcessorIndex].tasks.push(newTask);
      processors[targetProcessorIndex].currentLoad += newTask.demand;
      metrics.migrations++;
    }
  } else {
    processors[originatingProcessorIndex].tasks.push(newTask);
    processors[originatingProcessorIndex].currentLoad += newTask.demand;
  }
}

function strategy3ProactiveBalancing(
  processors: Processor[],
  params: SimulationParams,
  metrics: { queries: number; migrations: number }
): void {
  const { N, p, r } = params;
  if (N <= 1) return;

  for (let u_idx = 0; u_idx < N; u_idx++) {
    if (processors[u_idx].currentLoad < r) {
      const v_idx = getRandomProcessorIndex(N, u_idx);
      metrics.queries++;
      if (
        processors[v_idx].currentLoad > p &&
        processors[v_idx].tasks.length > 0 &&
        processors[v_idx].currentLoad - processors[u_idx].currentLoad > p * 0.2
      ) {
        const taskIndexToSteal = Math.floor(
          Math.random() * processors[v_idx].tasks.length
        );
        const stolenTask = processors[v_idx].tasks.splice(
          taskIndexToSteal,
          1
        )[0];

        if (stolenTask) {
          processors[v_idx].currentLoad -= stolenTask.demand;
          processors[u_idx].tasks.push(stolenTask);
          processors[u_idx].currentLoad += stolenTask.demand;
          metrics.migrations++;
        }
      }
    }
  }
}

function runSingleStrategySimulation(
  coreLogicFn: (
    processors: Processor[],
    newTask: Task,
    originatingProcessorIndex: number,
    params: SimulationParams,
    metrics: { queries: number; migrations: number }
  ) => void,
  proactiveBalancingFn:
    | ((
        processors: Processor[],
        params: SimulationParams,
        metrics: { queries: number; migrations: number }
      ) => void)
    | null,
  params: SimulationParams
): Metrics {
  let processors = initializeProcessors(params.N);
  const currentMetrics = { queries: 0, migrations: 0 };
  const fullParams = {
    ...params,
    numTasksToSimulate:
      params.numTasksToSimulate || DEFAULT_NUM_TASKS_TO_SIMULATE,
    maxTaskDemand: params.maxTaskDemand || DEFAULT_MAX_TASK_DEMAND,
  };

  for (let i = 0; i < fullParams.numTasksToSimulate; i++) {
    const newTask: Task = {
      id: i,
      demand: Math.floor(Math.random() * fullParams.maxTaskDemand) + 1,
    };
    const originatingProcessorIndex = Math.floor(Math.random() * params.N);

    coreLogicFn(
      processors,
      newTask,
      originatingProcessorIndex,
      fullParams,
      currentMetrics
    );

    if (proactiveBalancingFn) {
      proactiveBalancingFn(processors, fullParams, currentMetrics);
    }
  }

  const { averageLoad: rawAvgLoad, stdDevLoad: rawStdDevLoad } =
    calculateFinalLoadMetrics(processors);
  const maxAvgLoad =
    (fullParams.numTasksToSimulate * fullParams.maxTaskDemand) / fullParams.N;
  const averageLoad = parseFloat(((rawAvgLoad / maxAvgLoad) * 100).toFixed(2));
  const stdDevLoad = parseFloat(
    ((rawStdDevLoad / rawAvgLoad) * 100).toFixed(2)
  );
  return { ...currentMetrics, averageLoad, stdDevLoad };
}

export function applyStrategy1(params: SimulationParams): Metrics {
  return runSingleStrategySimulation(strategy1Core, null, params);
}

export function applyStrategy2(params: SimulationParams): Metrics {
  return runSingleStrategySimulation(strategy2Core, null, params);
}

export function applyStrategy3(params: SimulationParams): Metrics {
  return runSingleStrategySimulation(
    strategy2Core,
    strategy3ProactiveBalancing,
    params
  );
}
