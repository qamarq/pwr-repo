import { AlgorithmFunction } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 2. SSTF (Shortest Seek Time First)
export const runSSTF: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
  const hadRtFlag = requests.some((req) => req.isRealtime);
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const remainingRequests = deepCopyRequests(requests);

  let step = 1;
  while (remainingRequests.length > 0) {
    let closestRequestIndex = -1;
    let minDistance = Infinity;

    remainingRequests.forEach((req, index) => {
      const distance = calculateMovement(currentHeadPosition, req.cylinder);
      if (distance < minDistance) {
        minDistance = distance;
        closestRequestIndex = index;
      }
    });

    if (closestRequestIndex === -1) break;

    const nextRequest = remainingRequests.splice(closestRequestIndex, 1)[0];
    totalMovement += minDistance;
    currentHeadPosition = nextRequest.cylinder;
    path.push({ step: step++, cylinder: currentHeadPosition });
    servedRequestsOrder.push(nextRequest.id);
  }

  return { totalMovement, path, servedRequestsOrder, hadRtFlag };
};
