// app/_components/head-movement-chart.tsx
'use client';

import {
  Line,
  LineChart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  Area,
  AreaChart,
} from 'recharts';
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
  ChartLegend,
  ChartLegendContent,
} from '@/components/ui/chart';
import { ChartData } from '../lib/types';

interface Props {
  data: ChartData;
  diskSize: number;
  title: string;
}

const chartConfig = {
  cylinder: {
    label: 'Cylinder',
    color: 'var(--chart-1)',
  },
} satisfies ChartConfig;

export function HeadMovementChart({ data, diskSize, title }: Props) {
  if (!data || data.length === 0) {
    return <p>Brak danych do wyświetlenia wykresu.</p>;
  }

  return (
    <ChartContainer config={chartConfig} className="min-h-[200px] w-full">
      <AreaChart
        accessibilityLayer
        data={data}
        margin={{
          top: 20,
          right: 20,
          left: -10,
          bottom: 20,
        }}>
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="step"
          tickLine={false}
          axisLine={false}
          tickMargin={8}
          name="Krok"
        />
        <YAxis
          // dataKey="cylinder" // Nie jest potrzebne jeśli jest tylko jedna linia
          domain={[0, diskSize > 0 ? diskSize - 1 : 100]} // Ustaw zakres osi Y
          tickLine={false}
          axisLine={false}
          tickMargin={8}
          name="Numer Cylindra"
        />
        <ChartTooltip
          cursor={false}
          content={<ChartTooltipContent indicator="line" />}
        />
        <defs>
          <linearGradient id="fillCylinder" x1="0" y1="0" x2="0" y2="1">
            <stop
              offset="5%"
              stopColor="var(--color-cylinder)"
              stopOpacity={0.8}
            />
            <stop
              offset="95%"
              stopColor="var(--color-cylinder)"
              stopOpacity={0.1}
            />
          </linearGradient>
        </defs>
        {/* <Line
          dataKey="cylinder"
          type="monotone" // lub "linear" dla prostych linii
          stroke="var(--color-cylinder)"
          strokeWidth={2}
          dot={true} // Pokaż kropki dla każdego kroku
        /> */}
        <Area
          dataKey="cylinder"
          type="natural"
          fill="url(#fillCylinder)"
          fillOpacity={0.4}
          strokeWidth={2}
          stroke="var(--color-cylinder)"
          stackId="a"
        />
        <ChartLegend content={<ChartLegendContent />} />
      </AreaChart>
    </ChartContainer>
  );
}
