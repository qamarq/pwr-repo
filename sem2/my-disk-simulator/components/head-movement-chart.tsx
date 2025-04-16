'use client';

import { Line, LineChart, CartesianGrid, XAxis, YAxis } from 'recharts';
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
      <LineChart
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
          domain={[0, diskSize > 0 ? diskSize - 1 : 100]}
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
        <Line
          dataKey="cylinder"
          type="linear"
          strokeWidth={2}
          stroke="var(--color-cylinder)"
          dot={false}
        />
        <ChartLegend content={<ChartLegendContent />} />
      </LineChart>
    </ChartContainer>
  );
}
