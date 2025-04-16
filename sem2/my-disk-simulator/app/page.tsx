'use client';

import { useState } from 'react';
import { SimulationConfigForm } from '@/components/simulation-config-form';
import { SimulationResults } from '@/components/simulation-results';
import { algorithms } from '@/lib/algorithms';
import { SimulationParams, AlgorithmResult, Request } from '@/lib/types';
import { toast } from 'sonner';
import { ModeToggle } from '@/components/theme-switch';

export default function HomePage() {
  const [results, setResults] = useState<AlgorithmResult[]>([]);
  const [currentDiskSize, setCurrentDiskSize] = useState<number>(0);
  const [isSimulating, setIsSimulating] = useState<boolean>(false);

  const parseRequests = (
    requestsString: string,
    diskSize: number
  ): Request[] | null => {
    const parsedRequests: Request[] = [];
    const parts = requestsString
      .split(',')
      .map((s) => s.trim())
      .filter((s) => s !== '');
    let idCounter = 0;

    for (const part of parts) {
      const segments = part.split(':').map((s) => s.trim());
      const cylinderStr = segments[0];
      const isRealtimeStr =
        segments.length > 1 ? segments[1].toLowerCase() : 'false';
      const deadlineStr = segments.length > 2 ? segments[2] : undefined;

      const cylinder = parseInt(cylinderStr, 10);
      if (isNaN(cylinder) || cylinder < 0 || cylinder > diskSize) {
        toast.error('Błąd parsowania żądań', {
          description: `Nieprawidłowy numer cylindra: "${cylinderStr}". Musi być liczbą >= 0 i < ${diskSize}.`,
        });
        return null;
      }

      const isRealtime = isRealtimeStr === 'true';
      let deadline: number | undefined = undefined;
      if (isRealtime) {
        if (deadlineStr === undefined) {
          toast.error('Błąd parsowania żądań RT', {
            description: `Brak definicji deadline dla żądania RT: "${part}". Użyj formatu cylinder:true:deadline.`,
          });
          return null;
        }
        deadline = parseInt(deadlineStr, 10);
        if (isNaN(deadline) || deadline <= 0) {
          toast.error('Błąd parsowania żądań RT', {
            description: `Nieprawidłowy deadline dla żądania RT: "${deadlineStr}". Musi być dodatnią liczbą.`,
          });
          return null;
        }
      }

      parsedRequests.push({
        id: idCounter++,
        cylinder: cylinder,
        isRealtime: isRealtime,
        deadline: deadline,
        arrivalTime: Math.floor(Math.random() * 1000),
      });
    }
    if (parsedRequests.length === 0) {
      toast.error('Brak żądań', {
        description: 'Proszę podać co najmniej jedno żądanie.',
      });
      return null;
    }
    return parsedRequests;
  };

  const handleSimulationSubmit = (params: SimulationParams) => {
    setIsSimulating(true);
    setResults([]);
    setCurrentDiskSize(params.diskSize);

    setTimeout(() => {
      const parsedRequests = parseRequests(
        params.requestsString,
        params.diskSize
      );

      if (!parsedRequests) {
        setIsSimulating(false);
        return;
      }

      if (
        params.initialHeadPosition < 0 ||
        params.initialHeadPosition >= params.diskSize
      ) {
        toast.error('Błąd konfiguracji', {
          description: `Początkowa pozycja głowicy (${
            params.initialHeadPosition
          }) musi być w zakresie [0, ${params.diskSize - 1}].`,
        });
        setIsSimulating(false);
        return;
      }

      const simulationResults: AlgorithmResult[] = [];

      for (const name in algorithms) {
        try {
          const requestsCopy = parsedRequests.map((req) => ({ ...req }));

          const result = algorithms[name](
            params.initialHeadPosition,
            requestsCopy,
            params.diskSize
          );
          simulationResults.push({ name, ...result });
        } catch (error) {
          console.error(`Error running algorithm ${name}:`, error);
          simulationResults.push({
            name,
            totalMovement: Infinity,
            path: [{ step: 0, cylinder: params.initialHeadPosition }],
            servedRequestsOrder: [],
            hadRtFlag: false,
            starvedRequests: 0,
            error:
              error instanceof Error
                ? error.message
                : 'Nieznany błąd symulacji',
          });
        }
      }

      setResults(simulationResults);
      setIsSimulating(false);
      toast.success('Symulacja zakończona', {
        description: `Porównano ${simulationResults.length} algorytmów.`,
      });
    }, 100);
  };

  return (
    <main className="container mx-auto p-4 md:p-8">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold tracking-tight">
          Symulator Algorytmów Planowania Dostępu do Dysku
        </h1>
        <ModeToggle />
      </div>

      <SimulationConfigForm
        onSubmit={handleSimulationSubmit}
        isSimulating={isSimulating}
      />

      {results.length > 0 && (
        <SimulationResults results={results} diskSize={currentDiskSize} />
      )}
    </main>
  );
}
