'use client';

import * as z from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { Button } from '@/components/ui/button';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@/components/ui/accordion';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import { Cog, Play, RefreshCw } from 'lucide-react';
import type { SimulationConfig } from '@/lib/simulation/types';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from './ui/select';

export const simulationConfigSchema = z
  .object({
    numProcesses: z.coerce
      .number()
      .int()
      .min(1, 'Minimum 1 process')
      .max(10, 'Maximum 10 processes')
      .default(3),
    totalFrames: z.coerce
      .number()
      .int()
      .min(10, 'Minimum 10 frames')
      .max(1000, 'Maximum 1000 frames')
      .default(100),
    maxPagesPerProcess: z.coerce
      .number()
      .int()
      .min(5, 'Min 5 pages/process')
      .max(100, 'Max 100 pages/process')
      .default(20),
    minPageRefLength: z.coerce
      .number()
      .int()
      .min(20, 'Min 20 references')
      .max(500, 'Max 500 references')
      .default(50),
    maxPageRefLength: z.coerce
      .number()
      .int()
      .min(50, 'Min 50 references')
      .max(1000, 'Max 1000 references')
      .default(100),
    localityFactor: z.coerce
      .number()
      .min(0.1, 'Min 0.1')
      .max(1.0, 'Max 1.0')
      .default(0.8), // Probability of local reference
    localityWindowSize: z.coerce
      .number()
      .int()
      .min(3, 'Min 3')
      .max(20, 'Max 20')
      .default(5), // Size of "hot" page set

    pffDeltaT: z.coerce
      .number()
      .int()
      .min(5, 'Min 5 time units')
      .max(100, 'Max 100 time units')
      .default(20),
    pffLowerThresholdL: z.coerce
      .number()
      .min(0.01, 'Min 0.01')
      .max(0.5, 'Max 0.5')
      .default(0.1),
    pffUpperThresholdU: z.coerce
      .number()
      .min(0.1, 'Min 0.1')
      .max(0.9, 'Max 0.9')
      .default(0.4),
    pffSuspendThresholdH: z.coerce
      .number()
      .min(0.2, 'Min 0.2')
      .max(1.0, 'Max 1.0')
      .optional()
      .default(0.7),

    wsDeltaT: z.coerce
      .number()
      .int()
      .min(5, 'Min 5 time units')
      .max(100, 'Max 100 time units')
      .default(15),
    wsCalculationIntervalC: z.coerce
      .number()
      .int()
      .min(1, 'Min 1 time unit')
      .max(50, 'Max 50 time units')
      .default(7),
    wsSuspensionStrategy: z
      .enum([
        'smallest_wss',
        'largest_wss',
        'lowest_priority_mock',
        'random_mock',
      ])
      .default('smallest_wss'),

    thrashingWindowW: z.coerce
      .number()
      .int()
      .min(10, 'Min 10 time units')
      .max(100, 'Max 100 time units')
      .default(30),
    thrashingThresholdEFactor: z.coerce
      .number()
      .min(0.1, 'Min 0.1')
      .max(1.0, 'Max 1.0')
      .default(0.5), // e = w * factor
  })
  .refine((data) => data.minPageRefLength <= data.maxPageRefLength, {
    message: 'Min page reference length must be less than or equal to Max',
    path: ['minPageRefLength'],
  })
  .refine((data) => data.pffLowerThresholdL < data.pffUpperThresholdU, {
    message: 'PFF Lower threshold (l) must be less than Upper threshold (u)',
    path: ['pffLowerThresholdL'],
  })
  .refine(
    (data) =>
      !data.pffSuspendThresholdH ||
      data.pffUpperThresholdU < data.pffSuspendThresholdH,
    {
      message:
        'PFF Suspend threshold (h) must be greater than Upper threshold (u)',
      path: ['pffSuspendThresholdH'],
    }
  )
  .refine((data) => data.wsCalculationIntervalC < data.wsDeltaT, {
    message: 'WS Calculation interval (c) must be less than WS window (Δt)',
    path: ['wsCalculationIntervalC'],
  });

interface SimulationConfigFormProps {
  onSubmit: (values: SimulationConfig) => void;
  onGenerateReferences: () => void;
  isSimulating: boolean;
  defaultValues?: Partial<SimulationConfig>;
}

export function SimulationConfigForm({
  onSubmit,
  onGenerateReferences,
  isSimulating,
  defaultValues,
}: SimulationConfigFormProps) {
  const form = useForm<z.infer<typeof simulationConfigSchema>>({
    resolver: zodResolver(simulationConfigSchema),
    defaultValues: defaultValues ?? simulationConfigSchema.parse({}), // Use default values from schema
  });

  return (
    <Card className="shadow-lg">
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-xl">
          <Cog className="text-primary" />
          Simulation Configuration
        </CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            <Accordion
              type="multiple"
              defaultValue={['general', 'page-ref', 'pff', 'ws', 'thrashing']}
              className="w-full">
              <AccordionItem value="general">
                <AccordionTrigger className="text-lg font-semibold">
                  General Parameters
                </AccordionTrigger>
                <AccordionContent className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-4">
                  <FormField
                    control={form.control}
                    name="numProcesses"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Number of Processes (N)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 3"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="totalFrames"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Total Available Frames (F)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 100"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </AccordionContent>
              </AccordionItem>

              <AccordionItem value="page-ref">
                <AccordionTrigger className="text-lg font-semibold">
                  Page Reference Generation
                </AccordionTrigger>
                <AccordionContent className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-4">
                  <FormField
                    control={form.control}
                    name="maxPagesPerProcess"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Max Pages per Process</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 20"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="minPageRefLength"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Min Page References per Process</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 50"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="maxPageRefLength"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Max Page References per Process</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 100"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="localityFactor"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Locality Factor (0.1 - 1.0)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            step="0.1"
                            placeholder="e.g., 0.8"
                            {...field}
                          />
                        </FormControl>
                        <FormDescription>
                          Probability of local page reference.
                        </FormDescription>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="localityWindowSize"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Locality Window Size</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 5"
                            {...field}
                          />
                        </FormControl>
                        <FormDescription>
                          Number of 'hot' pages in a local set.
                        </FormDescription>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </AccordionContent>
              </AccordionItem>

              <AccordionItem value="pff">
                <AccordionTrigger className="text-lg font-semibold">
                  PFF Control Parameters
                </AccordionTrigger>
                <AccordionContent className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-4">
                  <FormField
                    control={form.control}
                    name="pffDeltaT"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>PFF Time Window (Δt)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 20"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="pffLowerThresholdL"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>PFF Lower Threshold (l)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            step="0.01"
                            placeholder="e.g., 0.1"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="pffUpperThresholdU"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>PFF Upper Threshold (u)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            step="0.01"
                            placeholder="e.g., 0.4"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="pffSuspendThresholdH"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>
                          PFF Suspend Threshold (h, optional)
                        </FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            step="0.01"
                            placeholder="e.g., 0.7"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </AccordionContent>
              </AccordionItem>

              <AccordionItem value="ws">
                <AccordionTrigger className="text-lg font-semibold">
                  Working Set Model Parameters
                </AccordionTrigger>
                <AccordionContent className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-4">
                  <FormField
                    control={form.control}
                    name="wsDeltaT"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>WS Time Window (Δt)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 15"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="wsCalculationIntervalC"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>WS Calculation Interval (c)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 7"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="wsSuspensionStrategy"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>WS Suspension Strategy</FormLabel>
                        <Select
                          onValueChange={field.onChange}
                          defaultValue={field.value}>
                          <FormControl>
                            <SelectTrigger>
                              <SelectValue placeholder="Select a strategy" />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent>
                            <SelectItem value="smallest_wss">
                              Smallest WSS
                            </SelectItem>
                            <SelectItem value="largest_wss">
                              Largest WSS
                            </SelectItem>
                            <SelectItem value="lowest_priority_mock">
                              Lowest Priority (Mock)
                            </SelectItem>
                            <SelectItem value="random_mock">
                              Random (Mock)
                            </SelectItem>
                          </SelectContent>
                        </Select>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </AccordionContent>
              </AccordionItem>

              <AccordionItem value="thrashing">
                <AccordionTrigger className="text-lg font-semibold">
                  Thrashing Detection Parameters
                </AccordionTrigger>
                <AccordionContent className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-4">
                  <FormField
                    control={form.control}
                    name="thrashingWindowW"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Thrashing Detection Window (w)</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            placeholder="e.g., 30"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="thrashingThresholdEFactor"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>
                          Thrashing Fault Factor (for e = w * factor)
                        </FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            step="0.01"
                            placeholder="e.g., 0.5"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </AccordionContent>
              </AccordionItem>
            </Accordion>

            <Separator />

            <div className="flex flex-col sm:flex-row gap-4 justify-end pt-4">
              <Button
                type="button"
                variant="outline"
                onClick={onGenerateReferences}
                disabled={isSimulating}>
                <RefreshCw className="mr-2 h-4 w-4" />
                Generate New References
              </Button>
              <Button
                type="submit"
                disabled={isSimulating}
                className="bg-primary hover:bg-primary/90">
                <Play className="mr-2 h-4 w-4" />
                {isSimulating ? 'Simulating...' : 'Start Simulation'}
              </Button>
            </div>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
}
