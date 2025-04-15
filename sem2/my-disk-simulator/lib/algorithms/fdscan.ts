import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';
import { runSSTF } from './sstf';

// 6. FD-SCAN (Feasible Deadline SCAN)
export const runFDSCAN: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
  const hadRtFlag = requests.some((req) => req.isRealtime);
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const rejectedRequests: number[] = [];
  const remainingRequests = deepCopyRequests(requests);
  let step = 1;
  let currentTime = 0;

  while (remainingRequests.length > 0) {
    // Krok 1: Zidentyfikuj i odrzuć nieosiągalne RT
    for (let i = remainingRequests.length - 1; i >= 0; i--) {
      const req = remainingRequests[i];
      if (req.isRealtime && req.deadline !== undefined) {
        const timeToReach = calculateMovement(
          currentHeadPosition,
          req.cylinder
        );
        console.log(
          `Request ${req.id}: currentTime=${currentTime}, timeToReach=${timeToReach}, deadline=${req.deadline}`
        );
        if (timeToReach > req.deadline) {
          console.log(
            `FD-SCAN: Rejecting RT request ${req.id} (current time ${currentTime} + time to reach ${timeToReach} > deadline ${req.deadline})`
          );
          rejectedRequests.push(req.id);
          remainingRequests.splice(i, 1); // Odrzuć
        }
      }
    }

    if (remainingRequests.length === 0) break;

    // Krok 2: Znajdź wszystkie *osiągalne* RT i posortuj według deadline
    const feasibleRtRequests = remainingRequests
      .filter((req) => req.isRealtime && req.deadline !== undefined)
      .sort((a, b) => (a.deadline ?? Infinity) - (b.deadline ?? Infinity));

    let primaryTarget: Request | null = null;
    if (feasibleRtRequests.length > 0) {
      primaryTarget = feasibleRtRequests[0];
    }

    // Krok 3: Wykonaj ruch w stylu SCAN
    let requestsToServeOnPath: Request[] = [];
    let direction: number;

    if (primaryTarget) {
      // Jest cel RT - ruszaj w jego kierunku, zbierając wszystko po drodze
      direction = Math.sign(primaryTarget.cylinder - currentHeadPosition);
      if (direction === 0) direction = 1; // Jeśli jesteśmy na celu, wybierz kierunek

      // Znajdź wszystkie żądania (RT i normalne) pomiędzy obecną pozycją a celem (włącznie)
      requestsToServeOnPath = remainingRequests.filter((req) => {
        if (direction === 1) {
          return (
            req.cylinder >= currentHeadPosition &&
            req.cylinder <= primaryTarget!.cylinder
          );
        } else {
          // direction === -1
          return (
            req.cylinder <= currentHeadPosition &&
            req.cylinder >= primaryTarget!.cylinder
          );
        }
      });

      // Posortuj je zgodnie z kierunkiem ruchu
      requestsToServeOnPath.sort(
        (a, b) => direction * (a.cylinder - b.cylinder)
      );
    } else {
      // Brak aktywnych celów RT - działaj jak standardowy SCAN na pozostałych
      if (remainingRequests.length === 0) break;

      let closestReqDist = Infinity;
      remainingRequests.forEach((req) => {
        const dist = calculateMovement(currentHeadPosition, req.cylinder);
        if (dist < closestReqDist) {
          closestReqDist = dist;
        }
      });
      const closestRequests = remainingRequests.filter(
        (req) =>
          calculateMovement(currentHeadPosition, req.cylinder) ===
          closestReqDist
      );
      if (closestRequests.length === 0) break;

      const closestUp = closestRequests.find(
        (r) => r.cylinder > currentHeadPosition
      );
      const closestDown = closestRequests.find(
        (r) => r.cylinder < currentHeadPosition
      );

      if (closestUp) direction = 1;
      else if (closestDown) direction = -1;
      else direction = 1;

      if (direction === 1) {
        requestsToServeOnPath = remainingRequests.filter(
          (req) => req.cylinder >= currentHeadPosition
        );
        requestsToServeOnPath.sort((a, b) => a.cylinder - b.cylinder);
      } else {
        requestsToServeOnPath = remainingRequests.filter(
          (req) => req.cylinder <= currentHeadPosition
        );
        requestsToServeOnPath.sort((a, b) => b.cylinder - a.cylinder);
      }

      // Jeśli nie ma nic w tym kierunku, zmień go
      if (requestsToServeOnPath.length === 0) {
        direction *= -1;
        if (direction === 1) {
          requestsToServeOnPath = remainingRequests.filter(
            (req) => req.cylinder >= currentHeadPosition
          );
          requestsToServeOnPath.sort((a, b) => a.cylinder - b.cylinder);
        } else {
          requestsToServeOnPath = remainingRequests.filter(
            (req) => req.cylinder <= currentHeadPosition
          );
          requestsToServeOnPath.sort((a, b) => b.cylinder - a.cylinder);
        }
      }
    }

    // Krok 4: Obsłuż żądania znalezione na ścieżce
    if (requestsToServeOnPath.length === 0) {
      console.warn(
        'FD-SCAN: No requests to serve on path found, potentially stuck?'
      );
      const sstfFallback = runSSTF(
        currentHeadPosition,
        remainingRequests,
        diskSize
      );
      if (sstfFallback.path.length > 1) {
        const fallbackTarget = sstfFallback.path[1];
        const movement = calculateMovement(
          currentHeadPosition,
          fallbackTarget.cylinder
        );
        totalMovement += movement;
        currentTime += movement; // Aktualizuj czas
        currentHeadPosition = fallbackTarget.cylinder;
        path.push({ step: step++, cylinder: currentHeadPosition });
        const servedId = sstfFallback.servedRequestsOrder[0];
        servedRequestsOrder.push(servedId);
        const indexToRemove = remainingRequests.findIndex(
          (r) => r.id === servedId
        );
        if (indexToRemove > -1) remainingRequests.splice(indexToRemove, 1);
        continue;
      } else {
        break;
      }
    }

    // Priorytetyzuj obsługę żądań czasu rzeczywistego
    // Najpierw obsługujemy RT, następnie pozostałe
    const rtRequests = requestsToServeOnPath.filter((req) => req.isRealtime);
    const nonRtRequests = requestsToServeOnPath.filter(
      (req) => !req.isRealtime
    );

    // Sortowanie RT requestów według deadline
    rtRequests.sort(
      (a, b) => (a.deadline ?? Infinity) - (b.deadline ?? Infinity)
    );

    const sortedRequestsToServe = [...rtRequests, ...nonRtRequests];

    for (const reqToServe of sortedRequestsToServe) {
      const movement = calculateMovement(
        currentHeadPosition,
        reqToServe.cylinder
      );
      totalMovement += movement;
      currentTime += movement;
      currentHeadPosition = reqToServe.cylinder;
      path.push({ step: step++, cylinder: currentHeadPosition });
      servedRequestsOrder.push(reqToServe.id);

      const indexToRemove = remainingRequests.findIndex(
        (r) => r.id === reqToServe.id
      );
      if (indexToRemove > -1) {
        remainingRequests.splice(indexToRemove, 1);
      } else {
        console.error(
          `FD-SCAN Error: Tried to remove request ${reqToServe.id} which was not found.`
        );
      }
    }
  }

  return {
    totalMovement,
    path,
    servedRequestsOrder,
    rejectedRequests,
    hadRtFlag,
  };
};
