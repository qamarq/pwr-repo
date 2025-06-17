'use client';

import React, { useState, useTransition } from 'react';
import Image from 'next/image';
import {
  ParameterForm,
  type SimulationFormValues,
} from '@/components/ParameterForm';
import { MetricsCard } from '@/components/MetricsCard';
import { ComparisonCharts } from '@/components/ComparisonCharts'; // Added import
import type { SimulationResults, UserSimulationParams } from '@/types';
import { runAllSimulationsAction } from './actions';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {
  AlertCircle,
  CheckCircle2,
  Settings2,
  BrainCircuit,
  BarChartBig,
  Info,
  PieChart,
} from 'lucide-react'; // Added PieChart
import { toast } from 'sonner';

export default function Home() {
  const [simulationResults, setSimulationResults] =
    useState<SimulationResults | null>(null);
  const [isPending, startTransition] = useTransition();

  const handleRunSimulation = async (values: SimulationFormValues) => {
    const params: UserSimulationParams = {
      N: values.N,
      p: values.p,
      r: values.r,
      z: values.z,
    };

    startTransition(async () => {
      try {
        setSimulationResults(null);
        toast('Simulation Started', {
          description: 'Processing tasks and strategies...',
        });
        const results = await runAllSimulationsAction(params);
        setSimulationResults(results);
        toast('Simulation Complete', {
          description: 'Results are now displayed.',
          action: <CheckCircle2 className="text-green-500" />,
        });
      } catch (error) {
        console.error('Simulation error:', error);
        toast.error('Simulation Error', {
          description:
            (error as Error).message ||
            'An unknown error occurred during simulation.',
          action: <AlertCircle className="text-white" />,
        });
        setSimulationResults(null);
      }
    });
  };

  const isLoading = isPending; // Simplified, as charts also check for results being null for initial state

  return (
    <div className="flex flex-col min-h-screen bg-gradient-to-br from-background to-secondary/30">
      <header className="py-6 px-4 md:px-8 bg-card shadow-md">
        <div className="container mx-auto flex items-center space-x-3">
          <BrainCircuit className="h-10 w-10 text-primary" />
          <div>
            <h1 className="text-3xl font-bold font-headline text-primary">
              Equilibrium Engine
            </h1>
            <p className="text-sm text-muted-foreground">
              Distributed Load Balancing Simulator
            </p>
          </div>
        </div>
      </header>

      <main className="flex-grow container mx-auto p-4 md:p-8 space-y-8">
        <section aria-labelledby="parameter-section-title">
          <h2 id="parameter-section-title" className="sr-only">
            Simulation Parameters
          </h2>
          <ParameterForm
            onSubmit={handleRunSimulation}
            isSimulating={isPending}
          />
        </section>

        {/* <section aria-labelledby="introduction-section-title">
          <Card className="shadow-lg">
            <CardHeader>
              <CardTitle className="flex items-center text-xl font-headline">
                <Info className="mr-2 h-6 w-6 text-primary" />
                About the Simulator
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3 text-sm text-foreground">
              <p>
                This application simulates three distinct distributed load
                balancing algorithms for N identical processors. New tasks
                arrive with varying computational demands.
              </p>
              <p>
                You can adjust parameters like the number of processors (N),
                load thresholds (p and r), and query attempts (z) to observe
                their impact on system performance.
              </p>
              <p className="font-semibold">Key Metrics:</p>
              <ul className="list-disc list-inside ml-4 space-y-1">
                <li>
                  <strong className="text-primary">Average Load:</strong> The
                  mean load across all processors.
                </li>
                <li>
                  <strong className="text-primary">Standard Deviation:</strong>{' '}
                  Variation in load from the average, indicating balance.
                </li>
                <li>
                  <strong className="text-primary">Queries:</strong> Total
                  requests made by processors for load information.
                </li>
                <li>
                  <strong className="text-primary">Migrations:</strong> Total
                  tasks moved between processors.
                </li>
              </ul>
              <p>
                Each simulation run processes 10,000 tasks, with individual task
                demands up to 10% of a processor's capacity.
              </p>
            </CardContent>
          </Card>
        </section> */}

        <section aria-labelledby="results-section-title">
          <div className="flex items-center mb-6">
            <BarChartBig className="h-8 w-8 text-primary mr-3" />
            <h2
              id="results-section-title"
              className="text-2xl font-bold font-headline text-foreground">
              Simulation Results (Per Strategy)
            </h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <MetricsCard
              strategyName="Strategy 1"
              metrics={simulationResults?.strategy1}
              isLoading={isPending && !simulationResults?.strategy1}
            />
            <MetricsCard
              strategyName="Strategy 2"
              metrics={simulationResults?.strategy2}
              isLoading={isPending && !simulationResults?.strategy2}
            />
            <MetricsCard
              strategyName="Strategy 3"
              metrics={simulationResults?.strategy3}
              isLoading={isPending && !simulationResults?.strategy3}
            />
          </div>
        </section>

        {/* New Chart Section - Conditionally render only if there are results or it's loading them */}
        {(simulationResults || isPending) && (
          <section
            aria-labelledby="comparison-chart-section-title"
            className="mt-12">
            {' '}
            {/* Added more top margin */}
            <div className="flex items-center mb-6">
              <PieChart className="h-8 w-8 text-primary mr-3" />
              <h2
                id="comparison-chart-section-title"
                className="text-2xl font-bold font-headline text-foreground">
                Comparative Analysis
              </h2>
            </div>
            <ComparisonCharts
              results={simulationResults}
              isLoading={isPending}
            />
          </section>
        )}
      </main>

      <footer className="py-6 text-center text-sm text-muted-foreground border-t border-border mt-auto">
        <div className="container mx-auto">
          &copy; {new Date().getFullYear()} Equilibrium Engine. All rights
          reserved.
        </div>
      </footer>
    </div>
  );
}
