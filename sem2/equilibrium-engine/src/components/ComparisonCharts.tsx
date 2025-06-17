'use client';

import type { SimulationResults, Metrics } from '@/types';
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from '@/components/ui/card';
import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
  type ChartConfig,
} from '@/components/ui/chart';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  LabelList,
} from 'recharts';
import { TrendingUp, AlertTriangle, Zap, Shuffle } from 'lucide-react';

interface ComparisonChartsProps {
  results: SimulationResults | null;
  isLoading: boolean;
}

// Chart Configurations
const averageLoadChartConfig = {
  averageLoad: { label: 'Avg. Load', color: 'var(--chart-1)' },
} satisfies ChartConfig;

const stdDevLoadChartConfig = {
  stdDevLoad: { label: 'Std. Dev.', color: 'var(--chart-2)' },
} satisfies ChartConfig;

const queriesChartConfig = {
  queries: { label: 'Queries', color: 'var(--chart-3)' },
} satisfies ChartConfig;

const migrationsChartConfig = {
  migrations: { label: 'Migrations', color: 'var(--chart-4)' },
} satisfies ChartConfig;

// Helper to prepare data for a specific metric
const prepareChartDataForMetric = (
  results: SimulationResults | null,
  metricKey: keyof Metrics,
  unitSuffix: string = ''
) => {
  if (!results) return [];
  const strategies = [
    { key: 'strategy1', name: 'Strategy 1' },
    { key: 'strategy2', name: 'Strategy 2' },
    { key: 'strategy3', name: 'Strategy 3' },
  ] as const;

  return strategies.map((s) => {
    const rawValue = results[s.key]?.[metricKey];
    // Format to 1 decimal place if unit is % and value is not integer, otherwise 0 decimal places for counts
    const decimalPlaces =
      unitSuffix === '%' &&
      rawValue !== undefined &&
      !Number.isInteger(rawValue)
        ? 1
        : 0;
    const value =
      rawValue !== undefined
        ? parseFloat(Number(rawValue).toFixed(decimalPlaces))
        : 0;
    return {
      name: s.name, // For XAxis
      [metricKey]: value, // For Bar dataKey and Tooltip
    };
  });
};

// Custom Label for Bar Charts
const renderLabelWithUnit = (props: any, unitSuffix: string = '') => {
  const { x, y, width, value } = props;

  if (value === undefined || value === null) return null;
  // Avoid showing "0%" for load/stdDev if value is truly 0 and it's a percentage.
  // For counts (queries, migrations), "0" is fine.
  if (
    value === 0 &&
    unitSuffix === '%' &&
    (props.name === 'averageLoad' || props.name === 'stdDevLoad')
  )
    return null;

  const displayValue = `${value}${unitSuffix}`;
  return (
    <text
      x={x + width / 2}
      y={y}
      dy={-6}
      textAnchor="middle"
      fill="var(--foreground)"
      fontSize={12}
      fontWeight="500">
      {displayValue}
    </text>
  );
};

export function ComparisonCharts({
  results,
  isLoading,
}: ComparisonChartsProps) {
  if (isLoading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {[...Array(4)].map((_, i) => (
          <Card key={i} className="shadow-lg animate-pulse">
            <CardHeader>
              <div className="h-6 bg-muted rounded-md w-3/4 mb-2"></div>
              <div className="h-4 bg-muted rounded-md w-1/2"></div>
            </CardHeader>
            <CardContent>
              <div className="h-64 bg-muted rounded-md"></div>
            </CardContent>
          </Card>
        ))}
      </div>
    );
  }

  if (
    !results ||
    (!results.strategy1 && !results.strategy2 && !results.strategy3)
  ) {
    return (
      <Card className="shadow-lg col-span-1 md:col-span-2">
        <CardHeader>
          <CardTitle className="flex items-center text-xl font-headline">
            <TrendingUp className="mr-2 h-6 w-6 text-primary" />
            Comparative Analysis Charts
          </CardTitle>
          <CardDescription>
            Visual comparison of strategy performance metrics will appear here
            after simulation.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">
            Run a simulation to generate and view the comparative charts.
          </p>
        </CardContent>
      </Card>
    );
  }

  const averageLoadData = prepareChartDataForMetric(
    results,
    'averageLoad',
    '%'
  );
  const stdDevLoadData = prepareChartDataForMetric(results, 'stdDevLoad', '%');
  const queriesData = prepareChartDataForMetric(results, 'queries');
  const migrationsData = prepareChartDataForMetric(results, 'migrations');

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      {/* Average Processor Load Chart */}
      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle className="text-lg font-headline flex items-center">
            <TrendingUp className="mr-2 h-5 w-5 text-primary" />
            Average Processor Load
          </CardTitle>
          <CardDescription>
            Lower is generally better, indicating efficient use without
            overload.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <ChartContainer
            config={averageLoadChartConfig}
            className="min-h-[250px] w-full aspect-video">
            <BarChart
              accessibilityLayer
              data={averageLoadData}
              margin={{ top: 30, right: 10, left: -10, bottom: 5 }}>
              <CartesianGrid vertical={false} strokeDasharray="3 3" />
              <XAxis
                dataKey="name"
                tickLine={false}
                tickMargin={10}
                axisLine={false}
              />
              <YAxis
                tickFormatter={(value) => `${value}%`}
                domain={[0, 'dataMax + 5']}
                allowDataOverflow={true}
              />
              <ChartTooltip
                cursor={false}
                content={<ChartTooltipContent indicator="dot" />}
              />
              <Bar
                dataKey="averageLoad"
                fill="var(--color-averageLoad)"
                radius={4}>
                <LabelList
                  dataKey="averageLoad"
                  position="top"
                  content={(props) => renderLabelWithUnit(props, '%')}
                  name="averageLoad"
                />
              </Bar>
            </BarChart>
          </ChartContainer>
        </CardContent>
      </Card>

      {/* Load Standard Deviation Chart */}
      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle className="text-lg font-headline flex items-center">
            <AlertTriangle className="mr-2 h-5 w-5 text-[var(--chart-2)]" />
            Load Standard Deviation
          </CardTitle>
          <CardDescription>
            Lower indicates better load balance across processors.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <ChartContainer
            config={stdDevLoadChartConfig}
            className="min-h-[250px] w-full aspect-video">
            <BarChart
              accessibilityLayer
              data={stdDevLoadData}
              margin={{ top: 30, right: 10, left: -10, bottom: 5 }}>
              <CartesianGrid vertical={false} strokeDasharray="3 3" />
              <XAxis
                dataKey="name"
                tickLine={false}
                tickMargin={10}
                axisLine={false}
              />
              <YAxis
                tickFormatter={(value) => `${value}%`}
                domain={[0, 'dataMax + 2']}
                allowDataOverflow={true}
              />
              <ChartTooltip
                cursor={false}
                content={<ChartTooltipContent indicator="dot" />}
              />
              <Bar
                dataKey="stdDevLoad"
                fill="var(--color-stdDevLoad)"
                radius={4}>
                <LabelList
                  dataKey="stdDevLoad"
                  position="top"
                  content={(props) => renderLabelWithUnit(props, '%')}
                  name="stdDevLoad"
                />
              </Bar>
            </BarChart>
          </ChartContainer>
        </CardContent>
      </Card>

      {/* Number of Load Queries Chart */}
      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle className="text-lg font-headline flex items-center">
            <Zap className="mr-2 h-5 w-5 text-[var(--chart-3)]" />
            Number of Load Queries
          </CardTitle>
          <CardDescription>
            Lower suggests less overhead in finding suitable processors.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <ChartContainer
            config={queriesChartConfig}
            className="min-h-[250px] w-full aspect-video">
            <BarChart
              accessibilityLayer
              data={queriesData}
              margin={{ top: 30, right: 10, left: -10, bottom: 5 }}>
              <CartesianGrid vertical={false} strokeDasharray="3 3" />
              <XAxis
                dataKey="name"
                tickLine={false}
                tickMargin={10}
                axisLine={false}
              />
              <YAxis
                tickFormatter={(value) => value.toLocaleString()}
                domain={[0, 'dataMax + 500']}
                allowDataOverflow={true}
              />
              <ChartTooltip
                cursor={false}
                content={<ChartTooltipContent indicator="dot" />}
              />
              <Bar dataKey="queries" fill="var(--color-queries)" radius={4}>
                <LabelList
                  dataKey="queries"
                  position="top"
                  content={(props) => renderLabelWithUnit(props)}
                  name="queries"
                />
              </Bar>
            </BarChart>
          </ChartContainer>
        </CardContent>
      </Card>

      {/* Number of Process Migrations Chart */}
      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle className="text-lg font-headline flex items-center">
            <Shuffle className="mr-2 h-5 w-5 text-[var(--chart-4)]" />
            Number of Process Migrations
          </CardTitle>
          <CardDescription>
            Lower indicates less task movement and associated costs.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <ChartContainer
            config={migrationsChartConfig}
            className="min-h-[250px] w-full aspect-video">
            <BarChart
              accessibilityLayer
              data={migrationsData}
              margin={{ top: 30, right: 10, left: -10, bottom: 5 }}>
              <CartesianGrid vertical={false} strokeDasharray="3 3" />
              <XAxis
                dataKey="name"
                tickLine={false}
                tickMargin={10}
                axisLine={false}
              />
              <YAxis
                tickFormatter={(value) => value.toLocaleString()}
                domain={[0, 'dataMax + 500']}
                allowDataOverflow={true}
              />
              <ChartTooltip
                cursor={false}
                content={<ChartTooltipContent indicator="dot" />}
              />
              <Bar
                dataKey="migrations"
                fill="var(--color-migrations)"
                radius={4}>
                <LabelList
                  dataKey="migrations"
                  position="top"
                  content={(props) => renderLabelWithUnit(props)}
                  name="migrations"
                />
              </Bar>
            </BarChart>
          </ChartContainer>
        </CardContent>
      </Card>
    </div>
  );
}
