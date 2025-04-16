import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from '@/components/ui/card';
import { AlgorithmResult } from '../lib/types';
import { HeadMovementChart } from './head-movement-chart';

interface Props {
  results: AlgorithmResult[];
  diskSize: number;
}

export function SimulationResults({ results, diskSize }: Props) {
  if (results.length === 0) {
    return null;
  }

  const sortedResults = [...results].sort(
    (a, b) => a.totalMovement - b.totalMovement
  );
  const bestResult = sortedResults[0]?.name;

  return (
    <div className="mt-8 space-y-6">
      <h2 className="text-2xl font-semibold tracking-tight">
        Wyniki Symulacji
      </h2>
      <div className="grid grid-cols-2 gap-6">
        {sortedResults.map((result) => {
          if (
            (result.name.includes('EDF') || result.name.includes('FD-SCAN')) &&
            !result.hadRtFlag
          ) {
            return null;
          } else if (
            (result.name.includes('FCFS') ||
              result.name.includes('SSTF') ||
              result.name == 'SCAN' ||
              result.name.includes('C-SCAN')) &&
            result.hadRtFlag
          ) {
            return null;
          }

          return (
            <Card
              key={result.name}
              className={result.name === bestResult ? 'border-primary' : ''}>
              <CardHeader>
                <CardTitle>
                  {result.name}
                  {result.name === bestResult && (
                    <span className="ml-2 text-xs font-normal text-primary">
                      (Najlepszy wynik)
                    </span>
                  )}
                </CardTitle>
                <CardDescription className="space-y-1">
                  <p>
                    Całkowity ruch głowicy: {result.totalMovement} cylindrów
                  </p>
                  <p>Zagłodzone żądania: {result.starvedRequests ?? 'N/A'}</p>
                  {/* <p className="text-sm text-muted-foreground">
                    Średni czas oczekiwania: {result.averageWaitTime ?? 'N/A'}
                  </p>
                  <p className="text-sm text-muted-foreground">
                    Największy czas oczekiwania: {result.maxWaitTime ?? 'N/A'}
                  </p> */}
                </CardDescription>
                {result.rejectedRequests &&
                  result.rejectedRequests.length > 0 && (
                    <p className="text-sm text-destructive">
                      Odrzucone żądania RT (ID):{' '}
                      {result.rejectedRequests.join(', ')}
                    </p>
                  )}
                {result.error && (
                  <p className="text-sm text-destructive">
                    Błąd: {result.error}
                  </p>
                )}
              </CardHeader>
              <CardContent>
                <h4 className="mb-2 text-sm font-medium">
                  Ścieżka ruchu głowicy:
                </h4>
                {/* Ogranicz liczbę punktów na wykresie jeśli jest ich bardzo dużo */}
                <HeadMovementChart
                  data={result.path}
                  diskSize={diskSize}
                  title={result.name}
                />
                <details className="mt-2 text-xs text-muted-foreground">
                  <summary>Kolejność obsługi żądań (ID)</summary>
                  <p className="mt-1 break-all">
                    {result.servedRequestsOrder.join(' -> ')}
                  </p>
                </details>
              </CardContent>
            </Card>
          );
        })}
      </div>
    </div>
  );
}
