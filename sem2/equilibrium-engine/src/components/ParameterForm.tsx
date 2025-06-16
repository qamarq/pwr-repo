'use client';

import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
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
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import type { UserSimulationParams } from '@/types';
import { Server, Gauge, Repeat, PlayCircle } from 'lucide-react';

const formSchema = z
  .object({
    N: z.coerce
      .number()
      .int()
      .min(2, 'N must be at least 2')
      .max(200, 'N can be at most 200'),
    p: z.coerce.number().min(0, 'p must be >= 0').max(100, 'p must be <= 100'),
    r: z.coerce.number().min(0, 'r must be >= 0').max(100, 'r must be <= 100'),
    z: z.coerce.number().int().min(1, 'z must be at least 1'),
  })
  .refine((data) => data.r < data.p, {
    message: 'r (min. threshold) must be less than p (overload threshold)',
    path: ['r'],
  })
  .refine((data) => data.z <= data.N, {
    message: 'z (max queries) cannot exceed N (processors)',
    path: ['z'],
  });

export type SimulationFormValues = z.infer<typeof formSchema>;

interface ParameterFormProps {
  onSubmit: (values: SimulationFormValues) => void;
  isSimulating: boolean;
  defaultValues?: Partial<SimulationFormValues>;
}

const defaultFormValues: SimulationFormValues = {
  N: 50,
  p: 70,
  r: 30,
  z: 5,
};

export function ParameterForm({
  onSubmit,
  isSimulating,
  defaultValues = defaultFormValues,
}: ParameterFormProps) {
  const form = useForm<SimulationFormValues>({
    resolver: zodResolver(formSchema),
    defaultValues,
  });

  return (
    <Card className="shadow-lg">
      <CardHeader>
        <CardTitle className="flex items-center text-xl font-headline">
          <Gauge className="mr-2 h-6 w-6 text-primary" />
          Simulation Parameters
        </CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <FormField
                control={form.control}
                name="N"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="flex items-center">
                      <Server className="mr-2 h-4 w-4" /> Number of Processors
                      (N)
                    </FormLabel>
                    <FormControl>
                      <Input type="number" placeholder="e.g., 50" {...field} />
                    </FormControl>
                    <FormDescription>Range: 2-200</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="p"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="flex items-center">
                      <Gauge className="mr-2 h-4 w-4" /> Overload Threshold (%)
                      (p)
                    </FormLabel>
                    <FormControl>
                      <Input type="number" placeholder="e.g., 70" {...field} />
                    </FormControl>
                    <FormDescription>
                      Load % above which processor is considered overloaded.
                      Range: 0-100.
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="r"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="flex items-center">
                      <Gauge className="mr-2 h-4 w-4" /> Min. Load Threshold (%)
                      (r)
                    </FormLabel>
                    <FormControl>
                      <Input type="number" placeholder="e.g., 20" {...field} />
                    </FormControl>
                    <FormDescription>
                      Load % below which processor seeks tasks (Strategy 3).
                      Range: 0-100, r &lt; p.
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="z"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="flex items-center">
                      <Repeat className="mr-2 h-4 w-4" /> Max Query Attempts (z)
                    </FormLabel>
                    <FormControl>
                      <Input type="number" placeholder="e.g., 5" {...field} />
                    </FormControl>
                    <FormDescription>
                      Max attempts for a processor to find another (Strategy 1).
                      Range: 1-N.
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <Button
              type="submit"
              disabled={isSimulating}
              className="w-full md:w-auto bg-primary hover:bg-primary/90 text-primary-foreground">
              <PlayCircle className="mr-2 h-5 w-5" />
              {isSimulating ? 'Simulating...' : 'Run Simulations'}
            </Button>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
}
