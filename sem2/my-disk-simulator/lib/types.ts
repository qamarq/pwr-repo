// app/_lib/types.ts
export interface Request {
  id: number; // Unikalny identyfikator dla kluczy w React i śledzenia
  cylinder: number;
  isRealtime: boolean;
  deadline?: number; // W jednostkach czasu = liczba cylindrów do przemieszczenia
}

export interface SimulationParams {
  diskSize: number;
  initialHeadPosition: number;
  requestsString: string; // Surowy string z formularza
  realtimeProbability?: number; // Opcjonalne: do generowania RT
  maxDeadline?: number; // Opcjonalne: do generowania RT
}

export interface AlgorithmResult {
  name: string;
  totalMovement: number;
  // Ścieżka głowicy: sekwencja odwiedzonych cylindrów, zaczynając od pozycji startowej
  path: { step: number; cylinder: number }[];
  servedRequestsOrder: number[]; // Kolejność ID obsłużonych żądań
  rejectedRequests?: number[]; // ID odrzuconych żądań (dla FD-SCAN)
  error?: string; // W razie problemów
}

// Typ dla danych wejściowych do wykresu
export type ChartData = {
  step: number;
  cylinder: number;
}[];

// Typ dla funkcji algorytmu
export type AlgorithmFunction = (
  initialHeadPosition: number,
  requests: Request[],
  diskSize: number
) => Omit<AlgorithmResult, 'name'>; // Pomijamy nazwę, bo będzie dodana później

export interface AlgorithmResult {
  name: string;
  totalMovement: number;
  path: { step: number; cylinder: number }[];
  servedRequestsOrder: number[]; // Kolejność ID obsłużonych żądań
  rejectedRequests?: number[]; // ID odrzuconych żądań (dla FD-SCAN)
  // averageWaitingTime: number; // <<< NOWE POLE
  // starvedRequests?: number[]; // <<< NOWE POLE (ID potencjalnie zagłodzonych)
  averageWaitTime?: number; // Średni czas oczekiwania na obsługę (dla algorytmów RT)
  maxWaitTime?: number; // Największy czas oczekiwania na obsługę (dla algorytmów RT)
  error?: string; // W razie problemów
}
