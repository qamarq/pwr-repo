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
  ChartConfig,
  ChartLegend,
  ChartLegendContent,
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
  const [frames, setFrames] = useState<number>(10);
  const [virtualPages, setVirtualPages] = useState<number>(20);
  const [refLength, setRefLength] = useState<number>(1000);
  const [localityFactor, setLocalityFactor] = useState<number>(5);
  const [phaseLength, setPhaseLength] = useState<number>(10);

  const [windowSize, setWindowSize] = useState<number>(10);
  const [threshold, setThreshold] = useState<number>(7);

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
        'Physical frames are more than virtual pages - less interesting case'
      );
    }

    setIsLoading(true);
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
      color: 'var(--chart-1)',
    },
    thrashing: {
      label: 'Thrashing',
      color: 'var(--chart-2)',
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
            Configure the simulation environment.
          </CardDescription>
        </CardHeader>
        <CardContent className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="flex flex-col gap-1">
            <Label htmlFor="frames">Physical Frames</Label>
            <Input
              id="frames"
              type="number"
              value={frames}
              onChange={(e) => setFrames(parseInt(e.target.value) || 1)}
            />
          </div>
          <div className="flex flex-col gap-1">
            <Label htmlFor="virtualPages">Total Virtual Pages</Label>
            <Input
              id="virtualPages"
              type="number"
              value={virtualPages}
              onChange={(e) => setVirtualPages(parseInt(e.target.value) || 1)}
            />
          </div>
          <div className="flex flex-col gap-1">
            <Label htmlFor="refLength">Reference String Length</Label>
            <Input
              id="refLength"
              type="number"
              value={refLength}
              onChange={(e) => setRefLength(parseInt(e.target.value) || 1)}
            />
          </div>
          <div />
          <div className="flex flex-col gap-1">
            <Label htmlFor="localityFactor">
              Locality Factor (Approx pages per phase)
            </Label>
            <Input
              id="localityFactor"
              type="number"
              value={localityFactor}
              onChange={(e) => setLocalityFactor(parseInt(e.target.value) || 1)}
            />
          </div>
          <div className="flex flex-col gap-1">
            <Label htmlFor="phaseLength">
              Phase Length (Approx refs per phase)
            </Label>
            <Input
              id="phaseLength"
              type="number"
              value={phaseLength}
              onChange={(e) => setPhaseLength(parseInt(e.target.value) || 1)}
            />
          </div>
          <div className="flex flex-col gap-1">
            <Label htmlFor="windowSize">Window Size (for Thrashing)</Label>
            <Input
              id="windowSize"
              type="number"
              value={windowSize}
              onChange={(e) => setWindowSize(parseInt(e.target.value) || 1)}
            />
          </div>
          <div className="flex flex-col gap-1">
            <Label htmlFor="threshold">Threshold (for Thrashing)</Label>
            <Input
              id="threshold"
              type="number"
              value={threshold}
              onChange={(e) => setThreshold(parseInt(e.target.value) || 1)}
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
          <CardHeader className="flex justify-between">
            <div className="flex flex-col gap-1">
              <CardTitle>Simulation Results</CardTitle>
              <CardDescription>
                Number of Page Faults per Algorithm
              </CardDescription>
            </div>
            <div className="flex items-center gap-2">
              <Button variant="outline" onClick={() => setResults([])}>
                Clear Results
              </Button>
              <Button
                onClick={() => {
                  handleGenerateString();
                  handleRunSimulation();
                }}
                disabled={isLoading || referenceString.length === 0}>
                {isLoading ? 'Simulating...' : 'Run Simulation Again'}
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <ChartContainer config={chartConfig}>
              <BarChart accessibilityLayer data={results}>
                <CartesianGrid vertical={false} />
                <XAxis
                  dataKey="name"
                  tickLine={false}
                  tickMargin={10}
                  axisLine={false}
                />
                <ChartTooltip content={<ChartTooltipContent hideLabel />} />
                <ChartLegend content={<ChartLegendContent />} />
                <Bar
                  dataKey="thrashing"
                  stackId="a"
                  fill="var(--color-thrashing)"
                  radius={[0, 0, 4, 4]}
                />
                <Bar
                  dataKey="pageFaults"
                  stackId="a"
                  fill="var(--color-pageFaults)"
                  radius={[4, 4, 0, 0]}
                />
              </BarChart>
            </ChartContainer>

            {/* <div className="mt-4 space-y-2">
              <h3 className="font-semibold">Final Frame States:</h3>
              {results.map((res) => (
                <p key={res.name} className="text-sm">
                  <strong>{res.name}:</strong> [
                  {res.finalFrames.map((f) => f ?? '-').join(', ')}]
                </p>
              ))}
            </div> */}
          </CardContent>
        </Card>
      )}
    </div>
  );
}
