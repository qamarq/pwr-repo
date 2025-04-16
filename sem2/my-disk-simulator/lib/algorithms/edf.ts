import { STARVED_AFTER_TICKS } from '@/constants';
import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 5. EDF (Earliest Deadline First)
export const runEDF: AlgorithmFunction = (initialHeadPosition, requests) => {
  let starvedRequests = 0;
  const hadRtFlag = requests.some((req) => req.isRealtime);
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  let currentTime = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const rejectedRequests: number[] = [];

  // Kopiujemy i sortujemy żądania według czasu przybycia
  const allRequests = deepCopyRequests(requests);
  allRequests.sort((a, b) => a.arrivalTime - b.arrivalTime);

  // Aktywne żądania - te, które już przybyły i oczekują na obsługę
  let activeRequests: Request[] = [];

  let step = 1;

  while (allRequests.length > 0 || activeRequests.length > 0) {
    // Dodaj nowe żądania, które przybyły w obecnym czasie lub wcześniej
    while (
      allRequests.length > 0 &&
      allRequests[0].arrivalTime <= currentTime
    ) {
      activeRequests.push(allRequests.shift()!);
    }

    // Jeśli nie ma aktywnych żądań, przesuń czas do przybycia następnego żądania
    if (activeRequests.length === 0 && allRequests.length > 0) {
      currentTime = allRequests[0].arrivalTime;
      continue; // Wróć na początek pętli, żeby dodać nowe żądania
    }

    // Jeśli nie ma więcej żądań do obsługi, zakończ algorytm
    if (activeRequests.length === 0) {
      break;
    }

    // Znajdź żądania RT, które mają deadline i są osiągalne (wykonalne)
    const feasibleRtRequests = activeRequests
      .filter((req) => req.isRealtime && req.deadline !== undefined)
      .filter((req) => {
        const timeToReach = calculateMovement(
          currentHeadPosition,
          req.cylinder
        );
        return req.deadline! >= timeToReach; // Sprawdź czy zdążymy
      })
      .sort((a, b) => (a.deadline ?? Infinity) - (b.deadline ?? Infinity)); // Sortuj wg deadline rosnąco

    // Sprawdź i oznacz nieosiągalne żądania RT
    const unreachableRtRequests = activeRequests
      .filter((req) => req.isRealtime && req.deadline !== undefined)
      .filter((req) => {
        const timeToReach = calculateMovement(
          currentHeadPosition,
          req.cylinder
        );
        return req.deadline! < timeToReach; // Żądania, których nie zdążymy obsłużyć
      });

    // Dodaj nieosiągalne żądania do odrzuconych i usuń je z aktywnych
    for (const rejectedReq of unreachableRtRequests) {
      rejectedRequests.push(rejectedReq.id);
      const indexToRemove = activeRequests.findIndex(
        (r) => r.id === rejectedReq.id
      );
      if (indexToRemove !== -1) {
        activeRequests.splice(indexToRemove, 1);
      }
    }

    let nextRequest: Request | undefined = undefined;
    let requestIndexToRemove = -1;

    if (feasibleRtRequests.length > 0) {
      // EDF - wybieramy żądanie RT z najwcześniejszym deadline
      nextRequest = feasibleRtRequests[0];
      requestIndexToRemove = activeRequests.findIndex(
        (r) => r.id === nextRequest!.id
      );
    } else {
      // Jeśli nie ma osiągalnych żądań RT, wybierz najbliższe (SSTF)
      let closestRequestIndex = -1;
      let minDistance = Infinity;

      activeRequests.forEach((req, index) => {
        const distance = calculateMovement(currentHeadPosition, req.cylinder);
        if (distance < minDistance) {
          minDistance = distance;
          closestRequestIndex = index;
        }
      });

      if (closestRequestIndex !== -1) {
        requestIndexToRemove = closestRequestIndex;
        nextRequest = activeRequests[requestIndexToRemove];
      }
    }

    if (nextRequest && requestIndexToRemove !== -1) {
      const movement = calculateMovement(
        currentHeadPosition,
        nextRequest.cylinder
      );
      totalMovement += movement;
      currentTime += movement; // Czas rośnie proporcjonalnie do ruchu głowicy
      currentHeadPosition = nextRequest.cylinder;

      // Dodajemy nowe żądania, które przybyły w międzyczasie
      while (
        allRequests.length > 0 &&
        allRequests[0].arrivalTime <= currentTime
      ) {
        activeRequests.push(allRequests.shift()!);
      }

      // Sprawdź czy żądanie nie zostało zagłodzone
      if (currentTime - nextRequest.arrivalTime > STARVED_AFTER_TICKS) {
        starvedRequests++;
      }

      path.push({ step: step++, cylinder: currentHeadPosition });
      servedRequestsOrder.push(nextRequest.id);
      activeRequests.splice(requestIndexToRemove, 1);
    } else {
      // Jeśli nie znaleziono żadnego żądania do obsługi, ale są jeszcze żądania w kolejce
      if (allRequests.length > 0) {
        currentTime = allRequests[0].arrivalTime;
        continue;
      }
      break;
    }
  }

  return {
    totalMovement,
    path,
    servedRequestsOrder,
    rejectedRequests,
    hadRtFlag,
    starvedRequests,
  };
};
