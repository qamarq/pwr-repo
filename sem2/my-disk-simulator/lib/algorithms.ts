// app/_lib/algorithms.ts
import { Request, AlgorithmFunction } from './types';

// --- Funkcje Pomocnicze ---
function calculateMovement(start: number, end: number): number {
  return Math.abs(start - end);
}

// Głęboka kopia, aby algorytmy nie modyfikowały oryginalnej listy
function deepCopyRequests(requests: Request[]): Request[] {
  return requests.map((req) => ({ ...req }));
}

// --- Implementacje Algorytmów ---

// 1. FCFS (First Come First Serve)
export const runFCFS: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const remainingRequests = deepCopyRequests(requests); // Pracujemy na kopii

  let step = 1;
  while (remainingRequests.length > 0) {
    const nextRequest = remainingRequests.shift(); // Bierzemy pierwszy z brzegu
    if (!nextRequest) break;

    totalMovement += calculateMovement(
      currentHeadPosition,
      nextRequest.cylinder
    );
    currentHeadPosition = nextRequest.cylinder;
    path.push({ step: step++, cylinder: currentHeadPosition });
    servedRequestsOrder.push(nextRequest.id);
  }

  return { totalMovement, path, servedRequestsOrder };
};

// 2. SSTF (Shortest Seek Time First)
export const runSSTF: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
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
      // Można dodać tie-breaking (np. niższy cylinder przy tej samej odległości)
    });

    if (closestRequestIndex === -1) break; // Nie powinno się zdarzyć jeśli są żądania

    const nextRequest = remainingRequests.splice(closestRequestIndex, 1)[0];
    totalMovement += minDistance; // Używamy obliczonej minimalnej odległości
    currentHeadPosition = nextRequest.cylinder;
    path.push({ step: step++, cylinder: currentHeadPosition });
    servedRequestsOrder.push(nextRequest.id);
  }

  return { totalMovement, path, servedRequestsOrder };
};

// 3. SCAN (Elevator Algorithm)
export const runSCAN: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
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
      // direction === -1
      requestsInDirection.push(
        ...remainingRequests.filter(
          (req) => req.cylinder <= currentHeadPosition
        )
      );
      requestsInDirection.sort((a, b) => b.cylinder - a.cylinder); // Malejąco
    }

    if (requestsInDirection.length > 0) {
      // Obsłuż wszystkie żądania w tym kierunku
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
        // *Uwaga*: SCAN powinien iść do końca dysku przed zmianą kierunku,
        // nawet jeśli nie ma tam żądań. Poprawka logiki:
        // Zamiast szukać najbliższego, idź do końca.
        const endOfDisk = direction === 1 ? diskSize - 1 : 0;
        // Czy faktycznie ruszamy się do końca dysku? Tylko jeśli są jeszcze żądania
        // LUB jeśli standardowa implementacja tego wymaga.
        // Przyjmijmy, że idzie do końca *tylko* jeśli musi zmienić kierunek.

        // --> Poprawiona logika SCAN: zawsze idzie do końca przed zmianą kierunku <--
        const boundary = direction === 1 ? diskSize - 1 : 0;
        if (currentHeadPosition !== boundary) {
          // Jeśli nie dotarliśmy do końca, a nie ma więcej żądań po drodze
          totalMovement += calculateMovement(currentHeadPosition, boundary);
          currentHeadPosition = boundary;
          path.push({ step: step++, cylinder: currentHeadPosition });
        }
        direction *= -1; // Zmień kierunek
      } else if (remainingRequests.length === 0) {
        break; // Koniec, jeśli nie ma już żądań
      } else {
        // Nie ma żądań w nowym kierunku - to nie powinno się zdarzyć jeśli remainingRequests > 0
        console.error(
          'SCAN logic error: No requests found in either direction.'
        );
        break;
      }
    }
  }

  return { totalMovement, path, servedRequestsOrder };
};

// 4. C-SCAN (Circular SCAN)
export const runCSCAN: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
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

  return { totalMovement, path, servedRequestsOrder };
};

// --- Algorytmy Real-Time (wymagają pola isRealtime i deadline w Request) ---

// 5. EDF (Earliest Deadline First) - Uproszczona wersja
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

  return { totalMovement, path, servedRequestsOrder };
};

// 6. FD-SCAN (Feasible Deadline SCAN)
export const runFDSCAN: AlgorithmFunction = (
  initialHeadPosition,
  requests,
  diskSize
) => {
  let currentHeadPosition = initialHeadPosition;
  let totalMovement = 0;
  const path = [{ step: 0, cylinder: initialHeadPosition }];
  const servedRequestsOrder: number[] = [];
  const rejectedRequests: number[] = [];
  const remainingRequests = deepCopyRequests(requests);
  let step = 1;

  while (remainingRequests.length > 0) {
    // Krok 1: Zidentyfikuj i odrzuć nieosiągalne RT
    const nowRejected: number[] = [];
    for (let i = remainingRequests.length - 1; i >= 0; i--) {
      const req = remainingRequests[i];
      if (req.isRealtime && req.deadline !== undefined) {
        const timeToReach = calculateMovement(
          currentHeadPosition,
          req.cylinder
        );
        if (req.deadline < timeToReach) {
          console.log(
            `FD-SCAN: Rejecting RT request ${req.id} (deadline ${req.deadline} < time ${timeToReach})`
          );
          rejectedRequests.push(req.id);
          nowRejected.push(req.id);
          remainingRequests.splice(i, 1); // Odrzuć
        }
      }
    }

    if (remainingRequests.length === 0) break; // Koniec jeśli nic nie zostało

    // Krok 2: Znajdź *osiągalne* RT z najwcześniejszym deadline
    const feasibleRtRequests = remainingRequests
      .filter((req) => req.isRealtime && req.deadline !== undefined) // Sprawdzenie deadline jest już niepotrzebne po kroku 1
      .sort((a, b) => (a.deadline ?? Infinity) - (b.deadline ?? Infinity));

    let primaryTarget: Request | null = null;
    if (feasibleRtRequests.length > 0) {
      primaryTarget = feasibleRtRequests[0]; // Cel główny: RT z najkrótszym możliwym deadline
    }

    // Krok 3: Wykonaj ruch w stylu SCAN
    let requestsToServeOnPath: Request[] = [];
    let nextPosition: number;
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
      nextPosition = primaryTarget.cylinder; // Ostateczny cel tego "skanu"
    } else {
      // Brak aktywnych celów RT - działaj jak standardowy SCAN na pozostałych
      // (Użyjmy logiki SCAN - idź w jednym kierunku, obsłuż wszystko, potem zawróć)
      // Wybierz kierunek (np. w stronę najbliższego żądania lub domyślnie w górę/dół)
      if (remainingRequests.length === 0) break; // Powtórne sprawdzenie

      let closestReqDist = Infinity;
      remainingRequests.forEach((req) => {
        closestReqDist = Math.min(
          closestReqDist,
          calculateMovement(currentHeadPosition, req.cylinder)
        );
      });
      const closestRequests = remainingRequests.filter(
        (req) =>
          calculateMovement(currentHeadPosition, req.cylinder) ===
          closestReqDist
      );
      if (closestRequests.length === 0) break; // Nie powinno się zdarzyć

      // Preferuj ruch w górę jeśli równie blisko, lub jeśli jedyna opcja
      const closestUp = closestRequests.find(
        (r) => r.cylinder > currentHeadPosition
      );
      const closestDown = closestRequests.find(
        (r) => r.cylinder < currentHeadPosition
      );

      if (closestUp) direction = 1;
      else if (closestDown) direction = -1;
      else direction = 1; // Jesteśmy na którymś żądaniu, ruszaj w górę

      // Zbierz wszystkie żądania w wybranym kierunku
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
      // Gdzie kończy się ten ruch? Na ostatnim żądaniu w tym kierunku.
      nextPosition =
        requestsToServeOnPath.length > 0
          ? requestsToServeOnPath[requestsToServeOnPath.length - 1].cylinder
          : currentHeadPosition;
    }

    // Krok 4: Obsłuż żądania znalezione na ścieżce
    if (requestsToServeOnPath.length === 0 && !primaryTarget) {
      // Sytuacja, gdy nie ma celu RT i SCAN też nic nie znalazł - powinno być obsłużone przez while loop break
      console.warn(
        'FD-SCAN: No requests to serve on path found, potentially stuck?'
      );
      // Spróbuj ruszyć do najbliższego, jeśli jeszcze coś zostało
      const sstfFallback = runSSTF(
        currentHeadPosition,
        remainingRequests,
        diskSize
      );
      if (sstfFallback.path.length > 1) {
        const fallbackTarget = sstfFallback.path[1];
        totalMovement += calculateMovement(
          currentHeadPosition,
          fallbackTarget.cylinder
        );
        currentHeadPosition = fallbackTarget.cylinder;
        path.push({ step: step++, cylinder: currentHeadPosition });
        const servedId = sstfFallback.servedRequestsOrder[0];
        servedRequestsOrder.push(servedId);
        const indexToRemove = remainingRequests.findIndex(
          (r) => r.id === servedId
        );
        if (indexToRemove > -1) remainingRequests.splice(indexToRemove, 1);
        continue; // Przejdź do następnej iteracji pętli while
      } else {
        break; // Nic więcej do zrobienia
      }
    }

    for (const reqToServe of requestsToServeOnPath) {
      totalMovement += calculateMovement(
        currentHeadPosition,
        reqToServe.cylinder
      );
      currentHeadPosition = reqToServe.cylinder;
      path.push({ step: step++, cylinder: currentHeadPosition });
      servedRequestsOrder.push(reqToServe.id);

      // Usuń obsłużone żądanie z głównej listy
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
    // Po obsłużeniu ścieżki, pętla while zacznie od nowa (sprawdzi RT, itd.)
  }

  return { totalMovement, path, servedRequestsOrder, rejectedRequests };
};

// --- Eksport wszystkich algorytmów ---
export const algorithms: { [key: string]: AlgorithmFunction } = {
  FCFS: runFCFS,
  SSTF: runSSTF,
  SCAN: runSCAN,
  'C-SCAN': runCSCAN,
  EDF: runEDF,
  'FD-SCAN': runFDSCAN,
};
