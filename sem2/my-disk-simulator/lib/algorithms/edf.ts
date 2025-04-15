import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 5. EDF (Earliest Deadline First)
export const runEDF: AlgorithmFunction = (initialHeadPosition, requests) => {
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const remainingRequests = deepCopyRequests(requests);
  let currentTime = 0;

  let step = 1;
  while (remainingRequests.length > 0) {
    const feasibleRtRequests = remainingRequests
      .filter((req) => req.isRealtime && req.deadline !== undefined)
      .filter((req) => {
        const timeToReach = calculateMovement(
          currentHeadPosition,
          req.cylinder
        );
        return req.deadline! >= timeToReach; // Sprawdź czy zdążymy
      })
      .sort((a, b) => (a.deadline ?? Infinity) - (b.deadline ?? Infinity)); // Sortuj wg deadline rosnąco

    let nextRequest: Request | undefined = undefined;
    let requestIndexToRemove = -1;

    if (feasibleRtRequests.length > 0) {
      nextRequest = feasibleRtRequests[0];
      requestIndexToRemove = remainingRequests.findIndex(
        (r) => r.id === nextRequest!.id
      );
    } else {
      let closestRequestIndex = -1;
      let minDistance = Infinity;
      remainingRequests.forEach((req, index) => {
        // if (req.isRealtime && req.deadline !== undefined) {
        //      const timeToReach = calculateMovement(currentHeadPosition, req.cylinder);
        //      if(req.deadline < timeToReach) return; // Pomiń nieosiągalne RT
        // }

        const distance = calculateMovement(currentHeadPosition, req.cylinder);
        if (distance < minDistance) {
          minDistance = distance;
          closestRequestIndex = index;
        }
      });

      if (closestRequestIndex !== -1) {
        requestIndexToRemove = closestRequestIndex;
        nextRequest = remainingRequests[requestIndexToRemove];
      }
    }

    if (nextRequest && requestIndexToRemove !== -1) {
      const movement = calculateMovement(
        currentHeadPosition,
        nextRequest.cylinder
      );
      totalMovement += movement;
      currentHeadPosition = nextRequest.cylinder;
      currentTime += movement;

      path.push({ step: step++, cylinder: currentHeadPosition });
      servedRequestsOrder.push(nextRequest.id);
      remainingRequests.splice(requestIndexToRemove, 1);
    } else {
      break;
    }
  }

  const hadRtFlag = requests.some((req) => req.isRealtime);
  return { totalMovement, path, servedRequestsOrder, hadRtFlag };
};
