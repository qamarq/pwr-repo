import { STARVED_AFTER_TICKS } from '@/constants';
import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 3. SCAN (Elevator Algorithm)
export const runSCAN: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
  let starvedRequests = 0;
  const hadRtFlag = requests.some((req) => req.isRealtime);
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  let currentTime = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const allRequests = deepCopyRequests(requests);

  // Sortujemy wszystkie żądania według czasu przybycia
  allRequests.sort((a, b) => a.arrivalTime - b.arrivalTime);

  // Aktywne żądania to te, które już przybyły i oczekują na obsługę
  let activeRequests: Request[] = [];
  let step = 1;

  // Kierunek: 1 = w górę (do diskSize - 1), -1 = w dół (do 0)
  let direction = 1; // Domyślnie w górę

  // Główna pętla algorytmu działa dopóki są żądania do obsłużenia
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

    // Ustal początkowy kierunek po dodaniu pierwszych aktywnych żądań
    if (totalMovement === 0 && activeRequests.length > 0) {
      // Sprawdź czy są żądania poniżej obecnej pozycji
      if (activeRequests.some((req) => req.cylinder < currentHeadPosition)) {
        // Jeśli wszystkie są w dół LUB start jest bliżej końca dysku, zacznij w dół
        if (
          activeRequests.every((req) => req.cylinder < currentHeadPosition) ||
          calculateMovement(currentHeadPosition, 0) <
            calculateMovement(currentHeadPosition, diskSize - 1)
        ) {
          direction = -1;
        }
      }
    }

    // Jeśli nadal nie ma żądań, zakończ algorytm
    if (activeRequests.length === 0) {
      break;
    }

    // Znajdź żądania do obsługi w bieżącym kierunku
    const requestsInDirection: Request[] = [];
    if (direction === 1) {
      requestsInDirection.push(
        ...activeRequests.filter((req) => req.cylinder >= currentHeadPosition)
      );
      requestsInDirection.sort((a, b) => a.cylinder - b.cylinder); // Rosnąco
    } else {
      requestsInDirection.push(
        ...activeRequests.filter((req) => req.cylinder <= currentHeadPosition)
      );
      requestsInDirection.sort((a, b) => b.cylinder - a.cylinder); // Malejąco
    }

    if (requestsInDirection.length > 0) {
      // Obsługujemy żądania w bieżącym kierunku
      for (const nextRequest of requestsInDirection) {
        const movementCost = calculateMovement(
          currentHeadPosition,
          nextRequest.cylinder
        );
        totalMovement += movementCost;
        currentTime += movementCost; // Czas rośnie proporcjonalnie do ruchu głowicy
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

        // Usuń obsłużone żądanie z listy aktywnych
        const indexToRemove = activeRequests.findIndex(
          (r) => r.id === nextRequest.id
        );
        if (indexToRemove > -1) {
          activeRequests.splice(indexToRemove, 1);
        }
      }
      // Po obsłużeniu wszystkich żądań w danym kierunku, zmień kierunek
      direction *= -1;
    } else {
      // Jeśli nie ma żądań w bieżącym kierunku, zmień kierunek
      direction *= -1;

      // Sprawdź czy są żądania w nowym kierunku
      const nextPotentialRequests =
        direction === 1
          ? activeRequests.filter((req) => req.cylinder >= currentHeadPosition)
          : activeRequests.filter((req) => req.cylinder <= currentHeadPosition);

      if (nextPotentialRequests.length === 0 && activeRequests.length > 0) {
        // Jeśli nie ma żadnych żądań w nowym kierunku, ale są inne aktywne żądania,
        // musimy przejść do granicy dysku i zmienić kierunek
        const boundary = direction === 1 ? diskSize - 1 : 0;
        if (currentHeadPosition !== boundary) {
          const movementCost = calculateMovement(currentHeadPosition, boundary);
          totalMovement += movementCost;
          currentTime += movementCost;
          currentHeadPosition = boundary;
          path.push({ step: step++, cylinder: currentHeadPosition });

          // Dodajemy nowe żądania, które przybyły w międzyczasie
          while (
            allRequests.length > 0 &&
            allRequests[0].arrivalTime <= currentTime
          ) {
            activeRequests.push(allRequests.shift()!);
          }
        }
        direction *= -1; // Zmień kierunek ponownie
      }
    }
  }

  return {
    totalMovement,
    path,
    servedRequestsOrder,
    hadRtFlag,
    starvedRequests,
  };
};
