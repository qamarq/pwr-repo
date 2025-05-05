// app/page.tsx (Simplified Example)
'use client';

import React, { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from '@/components/ui/card';
import {
  Bar,
  BarChart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import {
  ChartContainer,
  ChartTooltipContent,
  ChartTooltip,
} from '@/components/ui/chart'; // shadcn chart components

import { generateReferenceString, GenerationParams } from '@/lib/generator';
import {
  simulateFIFO,
  simulateOPT,
  simulateLRU,
  simulateSecondChance,
  simulateRAND,
  SimulationResult,
} from '@/lib/algorithms';

interface AlgoResult extends SimulationResult {
  name: string;
}

export default function SimulatorPage() {
  const [frames, setFrames] = useState<number>(4);
  const [virtualPages, setVirtualPages] = useState<number>(20);
  const [refLength, setRefLength] = useState<number>(50);
  const [localityFactor, setLocalityFactor] = useState<number>(5);
  const [phaseLength, setPhaseLength] = useState<number>(10);

  const [referenceString, setReferenceString] = useState<number[]>([]);
  const [results, setResults] = useState<AlgoResult[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleGenerateString = () => {
    const params: GenerationParams = {
      totalVirtualPages: virtualPages,
      referenceStringLength: refLength,
      localityFactor: localityFactor,
      phaseLength: phaseLength,
    };
    setReferenceString(generateReferenceString(params));
    setResults([]); // Clear previous results
  };

  const handleRunSimulation = () => {
    if (referenceString.length === 0) {
      alert('Please generate a reference string first.');
      return;
    }
    if (frames <= 0) {
      alert('Number of frames must be positive.');
      return;
    }
    if (frames > virtualPages) {
      console.warn(
        'Physical frames are more than virtual pages - less interesting case [cite: 9]'
      );
    }

    setIsLoading(true);
    // Ensure same string is used for all [cite: 11]
    const fifoResult = simulateFIFO(frames, referenceString);
    const optResult = simulateOPT(frames, referenceString);
    const lruResult = simulateLRU(frames, referenceString);
    const approxLruResult = simulateSecondChance(frames, referenceString);
    const randResult = simulateRAND(frames, referenceString);

    setResults([
      { name: 'FIFO', ...fifoResult },
      { name: 'OPT', ...optResult },
      { name: 'LRU', ...lruResult },
      { name: 'Approx LRU', ...approxLruResult },
      { name: 'RAND', ...randResult },
    ]);
    setIsLoading(false);
  };

  const chartConfig = {
    pageFaults: {
      label: 'Page Faults',
      color: 'hsl(var(--chart-1))',
    },
  };

  return (
    <div className="container mx-auto p-4 space-y-6">
      <h1 className="text-2xl font-bold">
        Page Replacement Algorithm Simulator
      </h1>

      <Card>
        <CardHeader>
          <CardTitle>Simulation Parameters</CardTitle>
          <CardDescription>
            Configure the simulation environment[cite: 1].
          </CardDescription>
        </CardHeader>
        <CardContent className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div>
            <Label htmlFor="frames">Physical Frames [cite: 2]</Label>
            <Input
              id="frames"
              type="number"
              value={frames}
              onChange={(e) => setFrames(parseInt(e.target.value) || 1)}
              min="1"
            />
          </div>
          <div>
            <Label htmlFor="virtualPages">Total Virtual Pages [cite: 3]</Label>
            <Input
              id="virtualPages"
              type="number"
              value={virtualPages}
              onChange={(e) => setVirtualPages(parseInt(e.target.value) || 1)}
              min="1"
            />
          </div>
          <div>
            <Label htmlFor="refLength">Reference String Length</Label>
            <Input
              id="refLength"
              type="number"
              value={refLength}
              onChange={(e) => setRefLength(parseInt(e.target.value) || 1)}
              min="1"
            />
          </div>
          <div>
            <Label htmlFor="localityFactor">
              Locality Factor (Approx pages per phase) [cite: 5]
            </Label>
            <Input
              id="localityFactor"
              type="number"
              value={localityFactor}
              onChange={(e) => setLocalityFactor(parseInt(e.target.value) || 1)}
              min="1"
            />
          </div>
          <div>
            <Label htmlFor="phaseLength">
              Phase Length (Approx refs per phase) [cite: 5]
            </Label>
            <Input
              id="phaseLength"
              type="number"
              value={phaseLength}
              onChange={(e) => setPhaseLength(parseInt(e.target.value) || 1)}
              min="1"
            />
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Reference String</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <Button onClick={handleGenerateString}>
            Generate New Reference String
          </Button>
          {referenceString.length > 0 && (
            <p className="font-mono break-all text-sm p-2 bg-muted rounded">
              {referenceString.join(', ')}
            </p>
          )}
        </CardContent>
      </Card>

      <Button
        onClick={handleRunSimulation}
        disabled={isLoading || referenceString.length === 0}>
        {isLoading ? 'Simulating...' : 'Run Simulation'}
      </Button>

      {results.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Simulation Results</CardTitle>
            <CardDescription>
              Number of Page Faults per Algorithm [cite: 6]
            </CardDescription>
          </CardHeader>
          <CardContent>
            <ChartContainer
              config={chartConfig}
              className="min-h-[200px] w-full">
              <ResponsiveContainer width="100%" height={300}>
                <BarChart
                  data={results}
                  margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis allowDecimals={false} />
                  <ChartTooltip
                    cursor={false}
                    content={<ChartTooltipContent hideLabel />}
                  />
                  <Legend />
                  <Bar
                    dataKey="pageFaults"
                    fill="hsl(var(--chart-1))"
                    name="Page Faults"
                  />
                </BarChart>
              </ResponsiveContainer>
            </ChartContainer>
            {/* Optional: Display final frame states */}
            {/* <div className="mt-4 space-y-2">
              <h3 className="font-semibold">Final Frame States:</h3>
              {results.map(res => (
                <p key={res.name} className="text-sm">
                  <strong>{res.name}:</strong> [{res.finalFrames.map(f => f ?? '-').join(', ')}]
                </p>
              ))}
            </div> */}
          </CardContent>
        </Card>
      )}
    </div>
  );
}
