import { STARVED_AFTER_TICKS } from '@/constants';
import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 2. SSTF (Shortest Seek Time First)
export const runSSTF: AlgorithmFunction = (initialHeadPosition, requests) => {
  let starvedRequests = 0;
  const hadRtFlag = requests.some((req) => req.isRealtime);
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  let currentTime = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];

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

    // Znajdź żądanie z najmniejszym czasem przejścia (najkrótszy seek)
    let closestRequestIndex = -1;
    let minDistance = Infinity;

    activeRequests.forEach((req, index) => {
      const distance = calculateMovement(currentHeadPosition, req.cylinder);
      if (distance < minDistance) {
        minDistance = distance;
        closestRequestIndex = index;
      }
    });

    if (closestRequestIndex === -1) break;

    const nextRequest = activeRequests.splice(closestRequestIndex, 1)[0];
    totalMovement += minDistance;
    currentTime += minDistance; // Czas rośnie proporcjonalnie do ruchu głowicy
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
  }

  return {
    totalMovement,
    path,
    servedRequestsOrder,
    hadRtFlag,
    starvedRequests,
  };
};
