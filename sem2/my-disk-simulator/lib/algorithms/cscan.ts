import { AlgorithmFunction } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 4. C-SCAN (Circular SCAN)
export const runCSCAN: AlgorithmFunction = (
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

  // Zawsze skanuje w jednym kierunku, np. w górę
  const direction = 1;

  // Posortuj wszystkie żądania dla łatwiejszego przetwarzania
  remainingRequests.sort((a, b) => a.cylinder - b.cylinder);

  // Znajdź żądania >= aktualnej pozycji
  let requestsAhead = remainingRequests.filter(
    (req) => req.cylinder >= currentHeadPosition
  );
  requestsAhead.sort((a, b) => a.cylinder - b.cylinder); // Upewnij się, że są posortowane

  // Znajdź żądania < aktualnej pozycji (do obsłużenia po zawinięciu)
  let requestsBehind = remainingRequests.filter(
    (req) => req.cylinder < currentHeadPosition
  );
  requestsBehind.sort((a, b) => a.cylinder - b.cylinder); // Też posortuj

  // Faza 1: Obsłuż żądania "przed" głowicą (włącznie z aktualną pozycją, jeśli jest żądanie)
  for (const nextRequest of requestsAhead) {
    totalMovement += calculateMovement(
      currentHeadPosition,
      nextRequest.cylinder
    );
    currentHeadPosition = nextRequest.cylinder;
    path.push({ step: step++, cylinder: currentHeadPosition });
    servedRequestsOrder.push(nextRequest.id);
    // Usuwamy z *oryginalnej* listy kopii, żeby uniknąć problemów z indeksami
    const indexToRemove = remainingRequests.findIndex(
      (r) => r.id === nextRequest.id
    );
    if (indexToRemove > -1) remainingRequests.splice(indexToRemove, 1); // Usuń z głównej listy
  }

  // Faza 2: Przeskok do początku (jeśli są jeszcze żądania "za" głowicą)
  if (requestsBehind.length > 0) {
    // Czy C-SCAN idzie do końca dysku (diskSize-1) przed skokiem? Tak.
    if (currentHeadPosition != diskSize - 1) {
      totalMovement += calculateMovement(currentHeadPosition, diskSize - 1);
      currentHeadPosition = diskSize - 1;
      path.push({ step: step++, cylinder: currentHeadPosition });
    }
    // Skok do początku dysku (cylinder 0)
    totalMovement += calculateMovement(currentHeadPosition, 0); // Ruch z końca do początku
    currentHeadPosition = 0;
    path.push({ step: step++, cylinder: currentHeadPosition }); // Rejestruj pozycję 0 po skoku

    // Faza 3: Obsłuż żądania od początku do miejsca, gdzie zaczęliśmy (lub do ostatniego żądania "za")
    for (const nextRequest of requestsBehind) {
      // Obsługujemy już od pozycji 0
      totalMovement += calculateMovement(
        currentHeadPosition,
        nextRequest.cylinder
      );
      currentHeadPosition = nextRequest.cylinder;
      path.push({ step: step++, cylinder: currentHeadPosition });
      servedRequestsOrder.push(nextRequest.id);
      // Usuwamy z *oryginalnej* listy kopii
      const indexToRemove = remainingRequests.findIndex(
        (r) => r.id === nextRequest.id
      );
      if (indexToRemove > -1) remainingRequests.splice(indexToRemove, 1); // Usuń z głównej listy
    }
  }
  // Upewnij się, że remainingRequests jest puste
  if (remainingRequests.length > 0) {
    console.error('C-SCAN Error: Not all requests served.', remainingRequests);
  }

  return { totalMovement, path, servedRequestsOrder, hadRtFlag };
};
