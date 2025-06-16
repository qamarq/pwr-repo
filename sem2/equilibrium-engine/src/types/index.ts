export interface Task {
  id: number;
  demand: number; // CPU % required, e.g., 3 for 3%
}

export interface Processor {
  id: number;
  tasks: Task[];
  currentLoad: number; // Sum of demands of its tasks
}

// Parameters configurable by the user
export interface UserSimulationParams {
  N: number; // Number of processors
  p: number; // Load threshold for strategies 1, 2, 3
  r: number; // Min load threshold for strategy 3
  z: number; // Max query attempts for strategy 1
}

// All parameters for simulation, including fixed ones
export interface SimulationParams extends UserSimulationParams {
  numTasksToSimulate: number; // Total tasks to simulate
  maxTaskDemand: number; // Max demand for a single task (e.g., 10 for 10%)
}

export interface Metrics {
  averageLoad: number;
  stdDevLoad: number;
  queries: number;
  migrations: number;
}

export type StrategyType = 'strategy1' | 'strategy2' | 'strategy3';

export interface SimulationResults {
  strategy1?: Metrics;
  strategy2?: Metrics;
  strategy3?: Metrics;
}
