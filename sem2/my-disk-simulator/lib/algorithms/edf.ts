// 5. EDF (Earliest Deadline First) - Uproszczona wersja

import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// Zakłada przełączanie na SSTF dla zwykłych żądań, gdy nie ma RT.
export const runEDF: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const remainingRequests = deepCopyRequests(requests);
  let currentTime = 0; // Czas symulacji (opcjonalne, ale deadline go implikuje)

  let step = 1;
  while (remainingRequests.length > 0) {
    // Znajdź wszystkie *real-time* żądania, których deadline jest jeszcze osiągalny
    const feasibleRtRequests = remainingRequests
      .filter((req) => req.isRealtime && req.deadline !== undefined)
      .filter((req) => {
        const timeToReach = calculateMovement(
          currentHeadPosition,
          req.cylinder
        );
        // Deadline jest w *kwantach czasu* od *teraz*
        // Czas = przemieszczenie
        return req.deadline! >= timeToReach; // Sprawdź czy zdążymy
      })
      .sort((a, b) => (a.deadline ?? Infinity) - (b.deadline ?? Infinity)); // Sortuj wg deadline rosnąco

    let nextRequest: Request | undefined = undefined;
    let requestIndexToRemove = -1;

    if (feasibleRtRequests.length > 0) {
      // Wybierz RT z najwcześniejszym deadline
      nextRequest = feasibleRtRequests[0];
      requestIndexToRemove = remainingRequests.findIndex(
        (r) => r.id === nextRequest!.id
      );
    } else {
      // Brak pilnych RT, użyj algorytmu standardowego (np. SSTF) dla *wszystkich* pozostałych
      let closestRequestIndex = -1;
      let minDistance = Infinity;
      remainingRequests.forEach((req, index) => {
        // Ignoruj RT, których nie da się już obsłużyć (choć EDF nie powinien ich odrzucać jawnie)
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
      // Aktualizacja czasu (jeśli śledzimy)
      currentTime += movement; // Zakładamy czas = ruch

      path.push({ step: step++, cylinder: currentHeadPosition });
      servedRequestsOrder.push(nextRequest.id);
      remainingRequests.splice(requestIndexToRemove, 1); // Usuń obsłużone żądanie
    } else {
      // Nie znaleziono żadnego żądania (ani RT, ani normalnego) - koniec
      break;
    }
  }

  // Uwaga: Ta implementacja EDF jest uproszczona. Dokładniejsza wymagałaby śledzenia czasu
  // i dynamicznego sprawdzania deadline'ów. Tutaj deadline jest sprawdzany tylko
  // względem *bezpośredniego* ruchu do celu.
  const hadRtFlag = requests.some((req) => req.isRealtime);
  return { totalMovement, path, servedRequestsOrder, hadRtFlag };
};
