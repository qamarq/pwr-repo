#!/usr/bin/env python3

import unittest
import datetime
from log_analyzer import *


class TestLogAnalyzer(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        with open("http_first_100k.log", "r") as f:
            cls.log = read_log(f)
        cls.sample_log_small = cls.log[:100]

    def test_01_read_log(self):
        self.assertIsInstance(self.log, list)
        self.assertGreater(len(self.log), 0)
        self.assertIsInstance(self.log[0], tuple)
        self.assertEqual(len(self.log[0]), 16)

    def test_02_sort_log(self):
        sorted_log = sort_log(self.sample_log_small, LogIndex.STATUS_CODE)
        self.assertEqual(len(sorted_log), len(self.sample_log_small))

        codes = [entry[LogIndex.STATUS_CODE] for entry in sorted_log]
        self.assertEqual(codes, sorted(codes))

        with self.assertRaises(IndexError):
            sort_log(self.sample_log_small, 999)

    def test_03_get_entries_by_code(self):
        code_404 = get_entries_by_code(self.log, 404)
        self.assertIsInstance(code_404, list)
        self.assertTrue(all(e[LogIndex.STATUS_CODE] == 404 for e in code_404))

        code_200 = get_entries_by_code(self.log, 200)
        self.assertTrue(all(e[LogIndex.STATUS_CODE] == 200 for e in code_200))

        with self.assertRaises(ValueError):
            get_entries_by_code(self.log, 999)

    def test_04_get_entries_by_addr(self):
        sample_ip = self.log[0][LogIndex.ORIG_IP]
        by_ip = get_entries_by_addr(self.log, sample_ip)
        self.assertIsInstance(by_ip, list)
        self.assertTrue(
            all(
                e[LogIndex.ORIG_IP] == sample_ip or e[LogIndex.HOST] == sample_ip
                for e in by_ip
            )
        )

        with self.assertRaises(ValueError):
            get_entries_by_addr(self.log, "999.999.999.999")

    def test_05_get_failed_reads(self):
        errors_4xx, errors_5xx = get_failed_reads(self.log, merge=False)
        self.assertIsInstance(errors_4xx, list)
        self.assertIsInstance(errors_5xx, list)
        self.assertTrue(all(400 <= e[LogIndex.STATUS_CODE] < 500 for e in errors_4xx))
        self.assertTrue(all(500 <= e[LogIndex.STATUS_CODE] < 600 for e in errors_5xx))

        all_errors = get_failed_reads(self.log, merge=True)
        self.assertEqual(len(all_errors), len(errors_4xx) + len(errors_5xx))

    def test_06_get_entries_by_extension(self):
        html_files = get_entries_by_extension(self.log, "html")
        self.assertIsInstance(html_files, list)
        self.assertTrue(
            all(".html" in e[LogIndex.URI].split("?")[0] for e in html_files)
        )

        jpg_files = get_entries_by_extension(self.log, ".jpg")
        self.assertIsInstance(jpg_files, list)

    def test_07_get_top_ips(self):
        top_5 = get_top_ips(self.log, 5)
        self.assertIsInstance(top_5, list)
        self.assertEqual(len(top_5), 5)
        self.assertIsInstance(top_5[0], tuple)
        self.assertEqual(len(top_5[0]), 2)

        counts = [count for ip, count in top_5]
        self.assertEqual(counts, sorted(counts, reverse=True))

    def test_08_get_unique_methods(self):
        methods = get_unique_methods(self.log)
        self.assertIsInstance(methods, list)
        self.assertGreater(len(methods), 0)
        self.assertEqual(len(methods), len(set(methods)))

    def test_09_get_entries_in_time_range(self):
        timestamps = [e[LogIndex.TS] for e in self.log]
        min_ts = min(timestamps)
        max_ts = max(timestamps)
        mid_ts = min_ts + (max_ts - min_ts) / 2

        first_half = get_entries_in_time_range(self.log, min_ts, mid_ts)
        self.assertIsInstance(first_half, list)
        self.assertTrue(all(min_ts <= e[LogIndex.TS] < mid_ts for e in first_half))

    def test_10_count_by_method(self):
        method_counts = count_by_method(self.log)
        self.assertIsInstance(method_counts, dict)
        self.assertGreater(len(method_counts), 0)
        self.assertEqual(sum(method_counts.values()), len(self.log))

    def test_11_get_top_uris(self):
        top_10 = get_top_uris(self.log, 10)
        self.assertIsInstance(top_10, list)
        self.assertLessEqual(len(top_10), 10)

        if top_10:
            self.assertIsInstance(top_10[0], tuple)
            self.assertEqual(len(top_10[0]), 2)

    def test_12_count_status_classes(self):
        classes = count_status_classes(self.log)
        self.assertIsInstance(classes, dict)
        self.assertIn("2xx", classes)
        self.assertIn("3xx", classes)
        self.assertIn("4xx", classes)
        self.assertIn("5xx", classes)

        total = sum(classes.values())
        self.assertLessEqual(total, len(self.log))

    def test_13_entry_to_dict(self):
        entry = self.log[0]
        entry_dict = entry_to_dict(entry)
        self.assertIsInstance(entry_dict, dict)
        self.assertIn("ts", entry_dict)
        self.assertIn("uid", entry_dict)
        self.assertIn("orig_ip", entry_dict)
        self.assertIn("method", entry_dict)
        self.assertIn("status_code", entry_dict)

    def test_14_log_to_dict(self):
        log_dict = log_to_dict(self.sample_log_small)
        self.assertIsInstance(log_dict, dict)
        self.assertGreater(len(log_dict), 0)

        for uid, entries in log_dict.items():
            self.assertIsInstance(entries, list)
            self.assertGreater(len(entries), 0)
            self.assertIsInstance(entries[0], dict)

    def test_16_get_most_active_session(self):
        uid, count = get_most_active_session(self.log)
        self.assertIsInstance(uid, str)
        self.assertIsInstance(count, int)
        self.assertGreater(count, 0)

    def test_17_get_session_paths(self):
        paths = get_session_paths(self.sample_log_small)
        self.assertIsInstance(paths, dict)

        for uid, uris in paths.items():
            self.assertIsInstance(uris, list)
            self.assertTrue(all(isinstance(uri, str) for uri in uris))

    def test_18_detect_sus(self):
        suspicious = detect_sus(self.log, threshold=1000)
        self.assertIsInstance(suspicious, list)

        for ip, count in suspicious:
            self.assertGreater(count, 1000)

    def test_19_get_extension_stats(self):
        extensions = get_extension_stats(self.log)
        self.assertIsInstance(extensions, dict)

        for ext, count in extensions.items():
            self.assertIsInstance(ext, str)
            self.assertIsInstance(count, int)
            self.assertGreater(count, 0)
            self.assertLessEqual(len(ext), 5)

    def test_20_analyze_log(self):
        analysis = analyze_log(self.sample_log_small)
        self.assertIsInstance(analysis, dict)

        self.assertIn("total_requests", analysis)
        self.assertIn("top_ips", analysis)
        self.assertIn("top_uris", analysis)
        self.assertIn("method_distribution", analysis)
        self.assertIn("status_code_classes", analysis)
        self.assertIn("errors", analysis)

        self.assertEqual(analysis["total_requests"], len(self.sample_log_small))


class TestEdgeCases(unittest.TestCase):

    def test_empty_log(self):
        empty_log = []

        self.assertEqual(sort_log(empty_log, 0), [])
        self.assertEqual(get_entries_by_code(empty_log, 200), [])
        self.assertEqual(get_top_ips(empty_log, 10), [])
        self.assertEqual(count_by_method(empty_log), {})

    def test_single_entry(self):
        with open("http_first_100k.log", "r") as f:
            log = read_log(f)
        single = log[:1]

        self.assertEqual(len(sort_log(single, 0)), 1)
        self.assertIsInstance(count_by_method(single), dict)

    def test_invalid_inputs(self):
        with open("http_first_100k.log", "r") as f:
            log = read_log(f)

        with self.assertRaises(ValueError):
            get_entries_by_code(log, -1)

        with self.assertRaises(ValueError):
            get_entries_by_code(log, 1000)

        with self.assertRaises(ValueError):
            get_entries_by_addr(log, "300.400.500.600")


def run_tests():
    loader = unittest.TestLoader()
    suite = unittest.TestSuite()

    suite.addTests(loader.loadTestsFromTestCase(TestLogAnalyzer))
    suite.addTests(loader.loadTestsFromTestCase(TestEdgeCases))

    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)

    print("\n" + "=" * 80)
    print("PODSUMOWANIE TESTÓW")
    print("=" * 80)
    print(f"Uruchomiono testów: {result.testsRun}")
    print(f"Sukcesy: {result.testsRun - len(result.failures) - len(result.errors)}")
    print(f"Błędy: {len(result.errors)}")
    print(f"Porażki: {len(result.failures)}")
    print("=" * 80)

    return result.wasSuccessful()


if __name__ == "__main__":
    success = run_tests()
    exit(0 if success else 1)
