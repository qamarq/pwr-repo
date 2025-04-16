import { STARVED_AFTER_TICKS } from '@/constants';
import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 4. C-SCAN (Circular SCAN)
export const runCSCAN: AlgorithmFunction = (
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
  let step = 1;

  // Kopiujemy i sortujemy żądania według czasu przybycia
  const allRequests = deepCopyRequests(requests);
  allRequests.sort((a, b) => a.arrivalTime - b.arrivalTime);

  // Aktywne żądania - te, które już przybyły i oczekują na obsługę
  let activeRequests: Request[] = [];

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

    // Jeśli nie ma więcej żądań do obsługi, zakończ algorytm
    if (activeRequests.length === 0) {
      break;
    }

    // Podziel aktywne żądania na dwie grupy: przed i za głowicą
    let requestsAhead = activeRequests.filter(
      (req) => req.cylinder >= currentHeadPosition
    );
    requestsAhead.sort((a, b) => a.cylinder - b.cylinder); // Rosnąco

    let requestsBehind = activeRequests.filter(
      (req) => req.cylinder < currentHeadPosition
    );
    requestsBehind.sort((a, b) => a.cylinder - b.cylinder); // Rosnąco

    // Faza 1: Obsłuż żądania "przed" głowicą (włącznie z aktualną pozycją, jeśli jest żądanie)
    for (const nextRequest of requestsAhead) {
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

      // Aktualizujemy listy żądań po dodaniu nowych
      const newRequestsAhead = activeRequests.filter(
        (req) =>
          req.cylinder >= currentHeadPosition && !requestsAhead.includes(req)
      );
      newRequestsAhead.sort((a, b) => a.cylinder - b.cylinder);

      const newRequestsBehind = activeRequests.filter(
        (req) =>
          req.cylinder < currentHeadPosition && !requestsBehind.includes(req)
      );
      newRequestsBehind.sort((a, b) => a.cylinder - b.cylinder);

      // Dodajemy nowe żądania do odpowiednich list
      requestsAhead = [...requestsAhead, ...newRequestsAhead];
      requestsBehind = [...requestsBehind, ...newRequestsBehind];

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
        // Usuwamy też z listy requestsAhead
        requestsAhead = requestsAhead.filter(
          (req) => req.id !== nextRequest.id
        );
      }
    }

    // Faza 2: Przeskok do początku (jeśli są jeszcze żądania "za" głowicą)
    if (requestsBehind.length > 0 || activeRequests.length > 0) {
      // Jeśli jeszcze nie jesteśmy na końcu dysku, przejdź tam
      if (currentHeadPosition != diskSize - 1) {
        const movementCost = calculateMovement(
          currentHeadPosition,
          diskSize - 1
        );
        totalMovement += movementCost;
        currentTime += movementCost;
        currentHeadPosition = diskSize - 1;
        path.push({ step: step++, cylinder: currentHeadPosition });

        // Dodajemy nowe żądania, które przybyły w międzyczasie
        while (
          allRequests.length > 0 &&
          allRequests[0].arrivalTime <= currentTime
        ) {
          activeRequests.push(allRequests.shift()!);
        }
      }

      // Skok do początku dysku (cylinder 0)
      const movementCost = calculateMovement(currentHeadPosition, 0);
      totalMovement += movementCost;
      currentTime += movementCost;
      currentHeadPosition = 0;
      path.push({ step: step++, cylinder: currentHeadPosition }); // Rejestruj pozycję 0 po skoku

      // Dodajemy nowe żądania, które przybyły w międzyczasie
      while (
        allRequests.length > 0 &&
        allRequests[0].arrivalTime <= currentTime
      ) {
        activeRequests.push(allRequests.shift()!);
      }

      // Aktualizuj listy żądań - po skoku pełna aktualizacja
      requestsAhead = activeRequests.filter(
        (req) => req.cylinder >= currentHeadPosition
      );
      requestsAhead.sort((a, b) => a.cylinder - b.cylinder);

      requestsBehind = activeRequests.filter(
        (req) => req.cylinder < currentHeadPosition
      );
      requestsBehind.sort((a, b) => a.cylinder - b.cylinder);

      // Faza 3: Obsłuż wszystkie pozostałe żądania od początku (rosnąco)
      for (const nextRequest of requestsAhead) {
        const movementCost = calculateMovement(
          currentHeadPosition,
          nextRequest.cylinder
        );
        totalMovement += movementCost;
        currentTime += movementCost;
        currentHeadPosition = nextRequest.cylinder;

        // Dodajemy nowe żądania, które przybyły w międzyczasie
        while (
          allRequests.length > 0 &&
          allRequests[0].arrivalTime <= currentTime
        ) {
          activeRequests.push(allRequests.shift()!);

          // Jeśli nowe żądanie jest w zakresie przed nami, dodaj do listy procesowania
          const newRequest = activeRequests[activeRequests.length - 1];
          if (
            newRequest.cylinder >= currentHeadPosition &&
            !requestsAhead.some((req) => req.id === newRequest.id)
          ) {
            requestsAhead.push(newRequest);
          }
        }

        // Sortuj ponownie żądania przed nami, aby obsługiwać je w odpowiedniej kolejności
        requestsAhead = requestsAhead.filter(
          (req) => req.cylinder >= currentHeadPosition
        );
        requestsAhead.sort((a, b) => a.cylinder - b.cylinder);

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
          // Usuwamy też z listy requestsAhead
          requestsAhead = requestsAhead.filter(
            (req) => req.id !== nextRequest.id
          );
        }
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
