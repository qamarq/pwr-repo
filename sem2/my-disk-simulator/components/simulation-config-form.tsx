// app/_components/simulation-config-form.tsx
'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { SimulationParams } from '../lib/types';
import { testCases } from '@/lib/tests';

interface Props {
  onSubmit: (params: SimulationParams) => void;
  isSimulating: boolean;
}

export function SimulationConfigForm({ onSubmit, isSimulating }: Props) {
  const [selectedTestCase, setSelectedTestCase] = useState<string>('custom');
  const [diskSize, setDiskSize] = useState<number>(200);
  const [initialHeadPosition, setInitialHeadPosition] = useState<number>(100);
  const [requestsString, setRequestsString] = useState<string>('');

  const handleTestCaseChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const testCase = testCases.find((tc) => tc.name === e.target.value);
    if (testCase) {
      setDiskSize(testCase.diskSize);
      setInitialHeadPosition(testCase.initialHead);
      setRequestsString(testCase.requests);
    } else {
      // Reset dla przypadku customowego
      setDiskSize(200);
      setInitialHeadPosition(100);
      setRequestsString('');
    }
    setSelectedTestCase(e.target.value);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({
      diskSize,
      initialHeadPosition,
      requestsString,
    });
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Konfiguracja Symulacji</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <Label htmlFor="testCase">Przykładowe testy wydajnościowe</Label>
            <select
              id="testCase"
              className="w-full p-2 border rounded-md"
              value={selectedTestCase}
              onChange={handleTestCaseChange}>
              <option value="custom">Niestandardowy</option>
              {testCases.map((tc) => (
                <option key={tc.name} value={tc.name}>
                  {tc.name}
                </option>
              ))}
            </select>
            {selectedTestCase !== 'custom' && (
              <p className="text-sm text-muted-foreground mt-2">
                {
                  testCases.find((tc) => tc.name === selectedTestCase)
                    ?.description
                }
              </p>
            )}
          </div>

          <div>
            <Label htmlFor="diskSize">Rozmiar Dysku (liczba cylindrów)</Label>
            <Input
              id="diskSize"
              type="number"
              value={diskSize}
              onChange={(e) => setDiskSize(parseInt(e.target.value, 10) || 0)}
              min="1"
              required
            />
          </div>

          <div>
            <Label htmlFor="initialHeadPosition">
              Pozycja Początkowa Głowicy
            </Label>
            <Input
              id="initialHeadPosition"
              type="number"
              value={initialHeadPosition}
              onChange={(e) =>
                setInitialHeadPosition(parseInt(e.target.value, 10) || 0)
              }
              min="0"
              max={diskSize > 0 ? diskSize - 1 : 0}
              required
            />
          </div>

          <div>
            <Label htmlFor="requests">
              Żądania (numery cylindrów oddzielone przecinkami)
            </Label>
            <Textarea
              id="requests"
              value={requestsString}
              onChange={(e) => setRequestsString(e.target.value)}
              placeholder="np. 98:true:10, 150:false, 37:true:5"
              required
              rows={3}
            />
            <p className="text-sm text-muted-foreground mt-1">
              Możesz dodać flagę RT i deadline: cylinder:rt:deadline (np.
              150:true:100, 40:false).
            </p>
          </div>

          <Button type="submit" disabled={isSimulating}>
            {isSimulating ? 'Symulowanie...' : 'Uruchom Symulację'}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
}
