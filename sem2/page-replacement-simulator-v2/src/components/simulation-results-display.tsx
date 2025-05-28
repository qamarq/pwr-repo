'use client';

import { Activity } from 'lucide-react';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
  CardFooter,
} from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
  ChartLegend,
  ChartLegendContent,
} from '@/components/ui/chart';
import {
  Bar,
  CartesianGrid,
  XAxis,
  YAxis,
  Line,
  Tooltip as RechartsTooltip,
  Legend as RechartsLegend,
  ResponsiveContainer,
  BarChart as RechartsBarChart,
  LineChart as RechartsLineChart,
} from 'recharts';
import type {
  FullSimulationResults,
  SimulationResult,
  AlgorithmStats,
} from '@/lib/simulation/types';
import { ScrollArea } from './ui/scroll-area';

interface SimulationResultsDisplayProps {
  results: FullSimulationResults | null;
  isLoading: boolean;
}

const chartConfig = {
  pageFaults: { label: 'Page Faults', color: 'var(--primary)' },
  frames: { label: 'Frames', color: 'var(--accent)' },
  thrashingEvents: { label: 'Thrashing Events', color: 'var(--chart-2)' },
};

const PageFaultsAllStrategiesChart = ({
  data,
}: {
  data: SimulationResult[];
}) => {
  const chartData = data.map((result) => ({
    name: result.algorithmName,
    pageFaults: result.stats.pageFaults,
  }));

  return (
    <ChartContainer config={chartConfig} className="min-h-[200px] w-full">
      <RechartsBarChart data={chartData} accessibilityLayer>
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="name"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
        />
        <YAxis
          domain={[(dataMin: number) => Math.round(dataMin * 0.9), 'auto']}
        />
        <ChartTooltip content={<ChartTooltipContent />} />
        <RechartsLegend content={<ChartLegendContent />} />
        <Bar
          dataKey="pageFaults"
          name="Page Faults"
          fill="var(--color-pageFaults)"
          radius={4}
        />
      </RechartsBarChart>
    </ChartContainer>
  );
};

const PageTrashingAllStrategiesChart = ({
  data,
}: {
  data: SimulationResult[];
}) => {
  const chartData = data.map((result) => ({
    name: result.algorithmName,
    thrashingEvents: result.stats.thrashingEvents,
  }));

  return (
    <ChartContainer config={chartConfig} className="min-h-[200px] w-full">
      <RechartsBarChart data={chartData} accessibilityLayer>
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="name"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
        />
        <YAxis
          domain={[(dataMin: number) => Math.round(dataMin * 0.9), 'auto']}
        />
        <ChartTooltip content={<ChartTooltipContent />} />
        <RechartsLegend content={<ChartLegendContent />} />
        <Bar
          dataKey="thrashingEvents"
          name="Thrashing Events"
          fill="var(--color-thrashingEvents)"
          radius={4}
        />
      </RechartsBarChart>
    </ChartContainer>
  );
};

const PageFaultsChart = ({
  data,
  processes,
}: {
  data: AlgorithmStats;
  processes: { id: number }[];
}) => {
  const chartData = processes.map((p) => ({
    name: `P${p.id}`,
    pageFaults: data.pageFaultsPerProcess[p.id] || 0,
  }));

  return (
    <ChartContainer config={chartConfig} className="min-h-[200px] w-full">
      <RechartsBarChart data={chartData} accessibilityLayer>
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="name"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
        />
        <YAxis />
        <ChartTooltip content={<ChartTooltipContent />} />
        <RechartsLegend content={<ChartLegendContent />} />
        <Bar
          dataKey="pageFaults"
          name="Page Faults"
          fill="var(--color-pageFaults)"
          radius={4}
        />
      </RechartsBarChart>
    </ChartContainer>
  );
};

// Placeholder for frame allocation timeline chart
const FrameAllocationTimelineChart = ({ data }: { data: AlgorithmStats }) => {
  const timelineData = data.timelineData || [];
  if (timelineData.length === 0) {
    return (
      <p className="text-sm text-muted-foreground">
        No timeline data available for frame allocation.
      </p>
    );
  }

  // Assuming timelineData has { time: number, allocations: { processId: frameCount } }
  // This needs proper transformation for Recharts
  const transformedData = timelineData.map((entry) => {
    const frameAllocations: { [key: string]: number } = {};
    Object.entries(entry.allocations).forEach(([pid, frames]) => {
      frameAllocations[`P${pid}`] = frames;
    });
    return { time: entry.time, ...frameAllocations };
  });

  const processKeys =
    timelineData.length > 0
      ? Object.keys(timelineData[0].allocations).map((pid) => `P${pid}`)
      : [];

  // Generate colors for all processes
  const colors = processKeys.map((_, index) => {
    const hue = (index * 360) / processKeys.length;
    return `hsl(${hue}, 70%, 50%)`;
  });

  return (
    <ChartContainer config={chartConfig} className="min-h-[200px] w-full">
      <RechartsLineChart data={transformedData} accessibilityLayer>
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="time"
          type="number"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
          label={{ value: 'Time', position: 'insideBottomRight', offset: -5 }}
        />
        <YAxis
          label={{ value: 'Frames', angle: -90, position: 'insideLeft' }}
          domain={['dataMin', 'auto']}
        />
        <ChartTooltip content={<ChartTooltipContent />} />
        <RechartsLegend content={<ChartLegendContent />} />
        {processKeys.map((key, index) => (
          <Line
            key={key}
            type="monotone"
            dataKey={key}
            stroke={colors[index]}
            strokeWidth={2}
            dot={false}
          />
        ))}
      </RechartsLineChart>
    </ChartContainer>
  );
};

const AlgorithmResultCard = ({
  result,
  processes,
}: {
  result: SimulationResult;
  processes: { id: number }[];
}) => (
  <Card className="shadow-md">
    <CardHeader>
      <CardTitle className="text-xl text-primary">
        {result.algorithmName}
      </CardTitle>
      <CardDescription>{result.description}</CardDescription>
    </CardHeader>
    <CardContent className="space-y-6">
      <div>
        <h4 className="font-semibold mb-2 text-lg">Overall Statistics</h4>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
          <div className="p-3 bg-secondary/50 rounded-md">
            <p className="text-muted-foreground">Total Page Faults</p>
            <p className="font-bold text-2xl">{result.stats.pageFaults}</p>
          </div>
          <div className="p-3 bg-secondary/50 rounded-md">
            <p className="text-muted-foreground">Thrashing Events</p>
            <p className="font-bold text-2xl">
              {Math.floor(result.stats.thrashingEvents)}
            </p>
          </div>
          <div className="p-3 bg-secondary/50 rounded-md">
            <p className="text-muted-foreground">Process Suspensions</p>
            <p className="font-bold text-2xl">
              {result.stats.processSuspensions}
            </p>
          </div>
          {result.stats.suspendedProcesses.length > 0 && (
            <div className="p-3 bg-secondary/50 rounded-md">
              <p className="text-muted-foreground">Suspended PIDs</p>
              <p className="font-bold text-lg truncate">
                {result.stats.suspendedProcesses.join(', ')}
              </p>
            </div>
          )}
        </div>
      </div>

      <div>
        <h4 className="font-semibold mb-2 text-lg">Page Faults per Process</h4>
        <PageFaultsChart data={result.stats} processes={processes} />
      </div>

      {result.algorithmName.includes('Working Set') ? (
        <div>
          <h4 className="font-semibold mb-2 text-lg">
            Frame Allocation Over Time
          </h4>
          <FrameAllocationTimelineChart data={result.stats} />
        </div>
      ) : (
        result.stats.finalFrameAllocations && (
          <div>
            <h4 className="font-semibold mb-2 text-lg">
              Final Frame Allocations
            </h4>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Process ID</TableHead>
                  <TableHead className="text-right">Frames Allocated</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {Object.entries(result.stats.finalFrameAllocations).map(
                  ([pid, frames]) => (
                    <TableRow key={pid}>
                      <TableCell>P{pid}</TableCell>
                      <TableCell className="text-right">{frames}</TableCell>
                    </TableRow>
                  )
                )}
              </TableBody>
            </Table>
          </div>
        )
      )}
    </CardContent>
    <CardFooter>
      <p className="text-xs text-muted-foreground">
        Completed simulation for {result.algorithmName}.
      </p>
    </CardFooter>
  </Card>
);

export function SimulationResultsDisplay({
  results,
  isLoading,
}: SimulationResultsDisplayProps) {
  if (isLoading) {
    return (
      <Card className="mt-8 shadow-lg">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-xl">
            <Activity className="animate-spin text-primary" />
            Simulation In Progress...
          </CardTitle>
        </CardHeader>
        <CardContent className="flex justify-center items-center h-64">
          <p className="text-muted-foreground">
            Please wait while the simulation runs.
          </p>
        </CardContent>
      </Card>
    );
  }

  if (!results) {
    return (
      <Card className="mt-8 shadow-lg">
        <CardHeader>
          <CardTitle className="text-xl">Simulation Results</CardTitle>
        </CardHeader>
        <CardContent className="flex justify-center items-center h-64">
          <p className="text-muted-foreground">
            Configure and start a simulation to see results here.
          </p>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="mt-8 shadow-lg">
      <CardHeader>
        <CardTitle className="text-2xl">Simulation Results Overview</CardTitle>
        <CardDescription>
          Displaying results for the configured frame allocation algorithms.
          Global reference string length: {results.globalReferenceString.length}
          . Number of processes: {results.processInfos.length}.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-2 gap-10">
          <PageFaultsAllStrategiesChart data={results.results} />
          <PageTrashingAllStrategiesChart data={results.results} />
        </div>
        <Tabs
          defaultValue={results.results[0]?.algorithmName || 'equal'}
          className="w-full">
          <ScrollArea className="whitespace-nowrap">
            <TabsList className="mb-4">
              {results.results.map((res) => (
                <TabsTrigger
                  key={res.algorithmName}
                  value={res.algorithmName}
                  className="text-sm">
                  {res.algorithmName}
                </TabsTrigger>
              ))}
            </TabsList>
          </ScrollArea>
          {results.results.map((res) => (
            <TabsContent key={res.algorithmName} value={res.algorithmName}>
              <AlgorithmResultCard
                result={res}
                processes={results.processInfos.map((p) => ({ id: p.id }))}
              />
            </TabsContent>
          ))}
        </Tabs>
        <div className="mt-6">
          <h3 className="text-lg font-semibold mb-2">
            Global Page Reference String (First 100)
          </h3>
          <ScrollArea className="h-64 border rounded-md p-2 bg-muted/30">
            <pre className="text-xs font-mono whitespace-pre-wrap">
              {results.globalReferenceString
                .slice(0, 100)
                .map(
                  (ref, idx) =>
                    `T${String(ref.timestamp).padStart(3, '0')}: P${
                      ref.processId
                    } -> Page ${String(ref.page).padStart(3, '0')}\n`
                )
                .join('')}
              {results.globalReferenceString.length > 100 &&
                `... and ${
                  results.globalReferenceString.length - 100
                } more references.`}
            </pre>
          </ScrollArea>
        </div>
      </CardContent>
    </Card>
  );
}
