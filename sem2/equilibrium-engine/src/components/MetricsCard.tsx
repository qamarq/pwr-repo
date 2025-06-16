'use client';

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from '@/components/ui/card';
import type { Metrics } from '@/types';
import {
  BarChartHorizontalBig,
  Sigma,
  HelpCircle,
  ArrowRightLeft,
  Activity,
} from 'lucide-react'; //Sigma for Std Dev

interface MetricItemProps {
  icon: React.ReactNode;
  label: string;
  value: string | number;
  unit?: string;
}

function MetricItem({ icon, label, value, unit }: MetricItemProps) {
  return (
    <div className="flex items-center justify-between py-2 border-b border-border/50 last:border-b-0">
      <div className="flex items-center text-sm text-muted-foreground">
        {icon}
        <span className="ml-2">{label}</span>
      </div>
      <span className="font-semibold text-foreground">
        {typeof value === 'number' ? value.toLocaleString() : value} {unit}
      </span>
    </div>
  );
}

interface MetricsCardProps {
  strategyName: string;
  metrics?: Metrics;
  isLoading: boolean;
}

export function MetricsCard({
  strategyName,
  metrics,
  isLoading,
}: MetricsCardProps) {
  if (isLoading) {
    return (
      <Card className="shadow-lg animate-pulse">
        <CardHeader>
          <CardTitle className="text-lg font-headline text-primary">
            {strategyName}
          </CardTitle>
          <CardDescription>Calculating results...</CardDescription>
        </CardHeader>
        <CardContent className="space-y-3">
          {[...Array(4)].map((_, i) => (
            <div key={i} className="h-8 bg-muted rounded-md"></div>
          ))}
        </CardContent>
      </Card>
    );
  }

  if (!metrics) {
    return (
      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle className="text-lg font-headline text-primary">
            {strategyName}
          </CardTitle>
          <CardDescription>Awaiting simulation run.</CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">
            Metrics will appear here once the simulation is complete.
          </p>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="shadow-lg flex flex-col h-full">
      <CardHeader>
        <CardTitle className="text-lg font-headline text-primary">
          {strategyName}
        </CardTitle>
        <CardDescription>Simulation results for this strategy.</CardDescription>
      </CardHeader>
      <CardContent className="flex-grow space-y-1">
        <MetricItem
          icon={<BarChartHorizontalBig className="h-4 w-4" />}
          label="Avg. Load"
          value={metrics.averageLoad.toFixed(2)}
          unit="%"
        />
        <MetricItem
          icon={<Sigma className="h-4 w-4" />}
          label="Std. Deviation"
          value={metrics.stdDevLoad.toFixed(2)}
          unit="%"
        />
        <MetricItem
          icon={<HelpCircle className="h-4 w-4" />}
          label="Queries"
          value={metrics.queries}
        />
        <MetricItem
          icon={<ArrowRightLeft className="h-4 w-4" />}
          label="Migrations"
          value={metrics.migrations}
        />
      </CardContent>
    </Card>
  );
}
