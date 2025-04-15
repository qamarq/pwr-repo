import { AlgorithmFunction, Request } from '../types';
import { deepCopyRequests, calculateMovement } from '../utils';

// 3. SCAN (Elevator Algorithm)
export const runSCAN: AlgorithmFunction = (
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

  // Kierunek: 1 = w górę (do diskSize - 1), -1 = w dół (do 0)
  // Zaczynamy ruch w kierunku najbliższego końca LUB jeśli żądania są tylko po jednej stronie
  let direction = 1; // Domyślnie w górę, można to ustawić inteligentniej

  // Prosta logika startowego kierunku: jeśli są żądania "w dół", zacznij w dół
  if (remainingRequests.some((req) => req.cylinder < currentHeadPosition)) {
    // Jeśli *wszystkie* są w dół LUB start jest bliżej górnej krawędzi, zacznij w dół
    if (
      remainingRequests.every((req) => req.cylinder < currentHeadPosition) ||
      calculateMovement(currentHeadPosition, 0) <
        calculateMovement(currentHeadPosition, diskSize - 1)
    ) {
      direction = -1;
    }
  }

  while (remainingRequests.length > 0) {
    const requestsInDirection: Request[] = [];
    if (direction === 1) {
      requestsInDirection.push(
        ...remainingRequests.filter(
          (req) => req.cylinder >= currentHeadPosition
        )
      );
      requestsInDirection.sort((a, b) => a.cylinder - b.cylinder); // Rosnąco
    } else {
      requestsInDirection.push(
        ...remainingRequests.filter(
          (req) => req.cylinder <= currentHeadPosition
        )
      );
      requestsInDirection.sort((a, b) => b.cylinder - a.cylinder); // Malejąco
    }

    if (requestsInDirection.length > 0) {
      for (const nextRequest of requestsInDirection) {
        totalMovement += calculateMovement(
          currentHeadPosition,
          nextRequest.cylinder
        );
        currentHeadPosition = nextRequest.cylinder;
        path.push({ step: step++, cylinder: currentHeadPosition });
        servedRequestsOrder.push(nextRequest.id);
        // Usuń obsłużone żądanie z głównej listy
        const indexToRemove = remainingRequests.findIndex(
          (r) => r.id === nextRequest.id
        );
        if (indexToRemove > -1) {
          remainingRequests.splice(indexToRemove, 1);
        }
      }
      // Po obsłużeniu wszystkich w danym kierunku, zmień kierunek
      // Nie idziemy do końca dysku, jeśli nie ma tam już żądań
      direction *= -1;
    } else {
      // Jeśli nie ma żądań w bieżącym kierunku, po prostu zmień kierunek
      // Jeśli i tak nic nie ma, pętla while się zakończy
      direction *= -1;
      // Dodatkowa logika: Jeśli zmieniliśmy kierunek i *nadal* nic nie ma,
      // a są żądania, oznacza to, że musimy przejść "pusty" fragment
      const nextPotentialRequests =
        direction === 1
          ? remainingRequests.filter(
              (req) => req.cylinder >= currentHeadPosition
            )
          : remainingRequests.filter(
              (req) => req.cylinder <= currentHeadPosition
            );

      if (nextPotentialRequests.length > 0 && remainingRequests.length > 0) {
        // Znajdź najbliższe żądanie w nowym kierunku
        let targetCylinder: number;
        if (direction === 1) {
          targetCylinder = Math.min(
            ...nextPotentialRequests.map((r) => r.cylinder)
          );
        } else {
          targetCylinder = Math.max(
            ...nextPotentialRequests.map((r) => r.cylinder)
          );
        }

        const boundary = direction === 1 ? diskSize - 1 : 0;
        if (currentHeadPosition !== boundary) {
          totalMovement += calculateMovement(currentHeadPosition, boundary);
          currentHeadPosition = boundary;
          path.push({ step: step++, cylinder: currentHeadPosition });
        }
        direction *= -1; // Zmień kierunek
      } else if (remainingRequests.length === 0) {
        break;
      } else {
        console.error(
          'SCAN logic error: No requests found in either direction.'
        );
        break;
      }
    }
  }

  return { totalMovement, path, servedRequestsOrder, hadRtFlag };
};
