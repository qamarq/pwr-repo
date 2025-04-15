import { AlgorithmFunction } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 1. FCFS (First Come First Serve)
export const runFCFS: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
  const hadRtFlag = requests.some((req) => req.isRealtime);
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const remainingRequests = deepCopyRequests(requests); // Pracujemy na kopii

  let step = 1;
  while (remainingRequests.length > 0) {
    const nextRequest = remainingRequests.shift(); // Bierzemy pierwszy z brzegu
    if (!nextRequest) break;

    totalMovement += calculateMovement(
      currentHeadPosition,
      nextRequest.cylinder
    );
    currentHeadPosition = nextRequest.cylinder;
    path.push({ step: step++, cylinder: currentHeadPosition });
    servedRequestsOrder.push(nextRequest.id);
  }

  return { totalMovement, path, servedRequestsOrder, hadRtFlag };
};
