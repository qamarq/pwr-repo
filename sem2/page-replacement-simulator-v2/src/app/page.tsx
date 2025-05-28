'use client';

import { useState, useEffect } from 'react';
import {
  SimulationConfigForm,
  simulationConfigSchema,
} from '@/components/simulation-config-form';
import { SimulationResultsDisplay } from '@/components/simulation-results-display';
import type {
  SimulationConfig,
  FullSimulationResults,
} from '@/lib/simulation/types';
import { runFullSimulation } from '@/lib/simulation/orchestrator';
import { Separator } from '@/components/ui/separator';
import { Frame } from 'lucide-react';
import { toast } from 'sonner';

export default function HomePage() {
  const [config, setConfig] = useState<SimulationConfig>(() =>
    simulationConfigSchema.parse({
      localAlgorithm: 'FIFO',
      numProcesses: 10,
      totalFrames: 30,
      maxPagesPerProcess: 15,
      minPageRefLength: 500,
      maxPageRefLength: 1000,
      localityFactor: 0.8,
      localityWindowSize: 5,
      pffDeltaT: 8,
      pffLowerThresholdL: 0.1,
      pffUpperThresholdU: 0.4,
      pffSuspendThresholdH: 0.7,
      wsDeltaT: 15,
      wsCalculationIntervalC: 4,
      wsSuspensionStrategy: 'smallest_wss',
      thrashingWindowW: 10,
      thrashingThresholdEFactor: 0.1,
    })
  );
  const [simulationResults, setSimulationResults] =
    useState<FullSimulationResults | null>(null);
  const [isSimulating, setIsSimulating] = useState(false);

  useEffect(() => {
    const savedConfig = localStorage.getItem('simulationConfig');
    if (savedConfig) {
      try {
        const parsedConfig = simulationConfigSchema.parse(
          JSON.parse(savedConfig)
        );
        setConfig(parsedConfig);
      } catch (error) {
        console.warn('Failed to load saved config:', error);
        localStorage.removeItem('simulationConfig');
      }
    }
  }, []);

  const handleRunSimulation = async (values: SimulationConfig) => {
    setIsSimulating(true);
    setSimulationResults(null);
    setConfig(values);
    localStorage.setItem('simulationConfig', JSON.stringify(values));

    toast('Simulation Started', {
      description: 'Processing page references and allocating frames...',
    });

    try {
      const results = await runFullSimulation(values);
      setSimulationResults(results);
      toast('Simulation Complete', {
        description: 'Results are now displayed.',
      });
    } catch (error) {
      console.error('Simulation error:', error);
      toast.error('Simulation Failed', {
        description: (error as Error).message || 'An unknown error occurred.',
      });
    } finally {
      setIsSimulating(false);
    }
  };

  const handleGenerateReferences = () => {
    toast('Reference Generation', {
      description:
        'New page reference strings will be generated when you start the next simulation.',
    });
  };

  return (
    <div className="container mx-auto p-4 md:p-8 min-h-screen flex flex-col">
      <header className="mb-8 text-center">
        <div className="inline-flex items-center justify-center gap-3 mb-2">
          <Frame size={40} className="text-primary" />
          <h1 className="text-4xl font-bold tracking-tight text-primary">
            Frame Allocation Analyzer
          </h1>
        </div>
        <p className="text-lg text-muted-foreground">
          Simulate and compare operating system frame allocation algorithms.
        </p>
      </header>

      <main className="flex-grow">
        <SimulationConfigForm
          onSubmit={handleRunSimulation}
          onGenerateReferences={handleGenerateReferences}
          isSimulating={isSimulating}
          defaultValues={config}
        />
        <Separator className="my-8" />
        <SimulationResultsDisplay
          results={simulationResults}
          isLoading={isSimulating}
        />
      </main>

      <footer className="mt-12 py-6 text-center text-sm text-muted-foreground border-t">
        <p>
          &copy; {new Date().getFullYear()} OS Simulation Project - Kamil
          Marczak.
        </p>
        <p className="font-mono text-xs mt-1">
          Task 4 - Frame Allocation Algorithm
        </p>
      </footer>
    </div>
  );
}
