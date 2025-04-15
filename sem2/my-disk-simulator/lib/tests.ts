import {
  generateAfterHeadRequests,
  generateCenterRequests,
  generateEdfBenchmark,
  generateEdgeRequests,
  generateFdScanBenchmark,
  generateRealTimeAfterHeadRequests,
} from '@/lib/utils';

export const testCases = [
  {
    name: 'SSTF Optymalny',
    requests: Array.from(
      { length: 100 },
      () =>
        // random number from 49 to 54
        Math.floor(Math.random() * 5) + 49
    ).join(','),
    initialHead: 53,
    diskSize: 200,
    description:
      'Żądania skupione wokół pozycji startowej - SSTF powinien mieć najmniejszy ruch',
  },
  {
    name: 'SCAN Wydajny',
    requests: '0, 199, 100, 50, 150, 25, 175',
    initialHead: 100,
    diskSize: 200,
    description:
      'Żądania rozłożone równomiernie - SCAN/C-SCAN będą najbardziej efektywne',
  },
  {
    name: 'FCFS Niekorzystny',
    requests: '199, 0, 198, 1, 197, 2',
    initialHead: 100,
    diskSize: 200,
    description:
      'Skrajne żądania w losowej kolejności - FCFS będzie miał najgorsze wyniki',
  },
  {
    name: 'Losowy 2k',
    requests: Array.from({ length: 2000 }, () =>
      Math.floor(Math.random() * 200)
    ).join(','),
    initialHead: Math.floor(Math.random() * 200),
    diskSize: 200,
    description: 'Losowe żądania - nieprzewidywalne wzorce',
  },
  {
    name: 'Losowy 100',
    requests: Array.from({ length: 100 }, () =>
      Math.floor(Math.random() * 300)
    ).join(','),
    initialHead: Math.floor(Math.random() * 300),
    diskSize: 300,
    description: 'Losowe żądania - nieprzewidywalne wzorce',
  },
  {
    name: 'EDF Wielkoskalowy',
    requests: generateEdfBenchmark(),
    initialHead: 150,
    diskSize: 300,
    description:
      "150 żądań z mieszanką RT i normalnych - optymalne dla EDF z różnymi deadline'ami",
  },
  {
    name: 'FD-SCAN Wielkoskalowy',
    requests: generateFdScanBenchmark(),
    initialHead: 150,
    diskSize: 300,
    description:
      '150 żądań ze skupiskami RT w różnych miejscach dysku - idealny dla FD-SCAN',
  },
  {
    name: 'Z listy',
    requests: '98,183,37,122,14,124,65,67',
    initialHead: 53,
    diskSize: 200,
    description:
      'Wzorzec z rzeczywistego świata - różne algorytmy będą miały różne wyniki',
  },
  {
    name: 'Krawędzie Dysku',
    requests: generateEdgeRequests(),
    initialHead: 150,
    diskSize: 300,
    description:
      '100 żądań rozłożonych tylko na dwóch krawędziach dysku (cylindry 0-20 i 280-300)',
  },
  {
    name: 'Tylko Środek',
    requests: generateCenterRequests(),
    initialHead: 50,
    diskSize: 300,
    description:
      '100 żądań skupionych wyłącznie wokół środka dysku (cylindry 140-160)',
  },
  {
    name: 'Za Głowicą',
    requests: generateAfterHeadRequests(),
    initialHead: 50,
    diskSize: 300,
    description:
      '100 żądań znajdujących się tylko za początkową pozycją głowicy (cylindry 51-300)',
  },
  {
    name: 'RT Za Głowicą',
    requests: generateRealTimeAfterHeadRequests(),
    initialHead: 50,
    diskSize: 300,
    description:
      "100 żądań RT z deadline'ami, wszystkie za początkową pozycją głowicy (cylindry 51-300)",
  },
];
