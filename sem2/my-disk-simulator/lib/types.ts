export interface Request {
  id: number;
  cylinder: number;
  isRealtime: boolean;
  deadline?: number;
  arrivalTime: number;
}

export interface SimulationParams {
  diskSize: number;
  initialHeadPosition: number;
  requestsString: string;
  realtimeProbability?: number;
  maxDeadline?: number;
}

export interface AlgorithmResult {
  name: string;
  totalMovement: number;
  path: { step: number; cylinder: number }[];
  servedRequestsOrder: number[];
  rejectedRequests?: number[];
  error?: string;
  hadRtFlag: boolean;
}

export type ChartData = {
  step: number;
  cylinder: number;
}[];

export type AlgorithmFunction = (
  initialHeadPosition: number,
  requests: Request[],
  diskSize: number
) => Omit<AlgorithmResult, 'name'>;

export interface AlgorithmResult {
  name: string;
  totalMovement: number;
  path: { step: number; cylinder: number }[];
  servedRequestsOrder: number[];
  rejectedRequests?: number[];
  averageWaitTime?: number;
  maxWaitTime?: number;
  error?: string;
  starvedRequests: number;
}
