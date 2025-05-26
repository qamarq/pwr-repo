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
import { Frame } from 'lucide-react'; // Example icon, can be replaced with a custom one
import { toast } from 'sonner';

export default function HomePage() {
  const [config, setConfig] = useState<SimulationConfig>(() =>
    simulationConfigSchema.parse({})
  );
  const [simulationResults, setSimulationResults] =
    useState<FullSimulationResults | null>(null);
  const [isSimulating, setIsSimulating] = useState(false);

  // Effect to load config from localStorage if available (optional)
  useEffect(() => {
    const savedConfig = localStorage.getItem('simulationConfig');
    if (savedConfig) {
      try {
        const parsedConfig = simulationConfigSchema.parse(
          JSON.parse(savedConfig)
        );
        setConfig(parsedConfig);
        // form.reset(parsedConfig); // Assuming form instance is accessible or pass down
      } catch (error) {
        console.warn('Failed to load saved config:', error);
        localStorage.removeItem('simulationConfig');
      }
    }
  }, []);

  const handleRunSimulation = async (values: SimulationConfig) => {
    setIsSimulating(true);
    setSimulationResults(null); // Clear previous results
    setConfig(values);
    localStorage.setItem('simulationConfig', JSON.stringify(values)); // Save config (optional)

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
    // This function would typically trigger the generation of new reference strings
    // and update some part of the UI or state if they are displayed independently.
    // For this app, new references are generated on each full simulation run.
    // So, this button primarily serves as a UX element or could reset parts of the form if needed.
    toast('Reference Generation', {
      description:
        'New page reference strings will be generated when you start the next simulation.',
    });
    // If you want to clear results when new references are "generated":
    // setSimulationResults(null);
  };

  // This is needed if form.reset is used in useEffect for localStorage
  // However, SimulationConfigForm manages its own form instance.
  // To pass default values to the form upon loading from localStorage,
  // the `defaultValues` prop of SimulationConfigForm can be used,
  // and `config` state would be the source of truth for these defaults.
  // For simplicity, removing direct form instance manipulation from HomePage.

  return (
    <div className="container mx-auto p-4 md:p-8 min-h-screen flex flex-col">
      <header className="mb-8 text-center">
        <div className="inline-flex items-center justify-center gap-3 mb-2">
          {/* Using a Lucide icon as a placeholder for a custom app icon */}
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
          defaultValues={config} // Pass current config as default values
        />
        <Separator className="my-8" />
        <SimulationResultsDisplay
          results={simulationResults}
          isLoading={isSimulating}
        />
      </main>

      <footer className="mt-12 py-6 text-center text-sm text-muted-foreground border-t">
        <p>
          &copy; {new Date().getFullYear()} OS Simulation Project. Built with
          Next.js & ShadCN UI.
        </p>
        <p className="font-mono text-xs mt-1">
          Task 4 - Frame Allocation Algorithm Research
        </p>
      </footer>
    </div>
  );
}
