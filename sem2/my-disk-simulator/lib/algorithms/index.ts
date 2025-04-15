import { AlgorithmFunction } from '../types';
import { runCSCAN } from './cscan';
import { runEDF } from './edf';
import { runFCFS } from './fcfs';
import { runFDSCAN } from './fdscan';
import { runSCAN } from './scan';
import { runSSTF } from './sstf';

export const algorithms: { [key: string]: AlgorithmFunction } = {
  FCFS: runFCFS,
  SSTF: runSSTF,
  SCAN: runSCAN,
  'C-SCAN': runCSCAN,
  EDF: runEDF,
  'FD-SCAN': runFDSCAN,
};
