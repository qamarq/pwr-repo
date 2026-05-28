#!/usr/bin/env python3

import sys
import os
from dataclasses import dataclass
from datetime import datetime, timedelta
from typing import List, Optional

sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'lab3'))
from log_analyzer import read_log, LogIndex


@dataclass
class LogEntry:
    timestamp: datetime
    uid: str
    orig_ip: str
    orig_port: int
    resp_ip: str
    resp_port: int
    trans_depth: int
    method: str
    host: str
    uri: str
    referrer: str
    user_agent: str
    request_len: int
    response_len: int
    status_code: int
    status_msg: str

    @classmethod
    def from_tuple(cls, t) -> 'LogEntry':
        return cls(
            timestamp=t[LogIndex.TS],
            uid=t[LogIndex.UID],
            orig_ip=t[LogIndex.ORIG_IP],
            orig_port=t[LogIndex.ORIG_PORT],
            resp_ip=t[LogIndex.RESP_IP],
            resp_port=t[LogIndex.RESP_PORT],
            trans_depth=t[LogIndex.TRANS_DEPTH],
            method=t[LogIndex.METHOD],
            host=t[LogIndex.HOST],
            uri=t[LogIndex.URI],
            referrer=t[LogIndex.REFERRER],
            user_agent=t[LogIndex.USER_AGENT],
            request_len=t[LogIndex.REQ_LEN],
            response_len=t[LogIndex.RESP_LEN],
            status_code=t[LogIndex.STATUS_CODE],
            status_msg=t[LogIndex.STATUS_MSG],
        )

    def master_text(self, max_len: int = 80) -> str:
        text = (
            f'{self.orig_ip} - - '
            f'[{self.timestamp.strftime("%d/%b/%Y:%H:%M:%S")}] '
            f'"{self.method} {self.uri}"'
        )
        if len(text) > max_len:
            return text[:max_len] + '...'
        return text


class LogModel:
    def __init__(self):
        self._all_entries: List[LogEntry] = []
        self._filtered_entries: List[LogEntry] = []
        self._current_index: int = -1

    def load_file(self, path: str) -> int:
        with open(path, 'r') as f:
            raw = read_log(f)
        self._all_entries = [LogEntry.from_tuple(t) for t in raw]
        self._filtered_entries = list(self._all_entries)
        self._current_index = 0 if self._filtered_entries else -1
        print(f'wczytano {len(self._all_entries)} wpisow z pliku')
        return len(self._all_entries)

    def apply_time_filter(self, start: datetime, end: datetime) -> None:
        self._filtered_entries = [
            e for e in self._all_entries
            if start <= e.timestamp < end
        ]
        self._current_index = 0 if self._filtered_entries else -1
        print(f'po filtrowaniu zostalo {len(self._filtered_entries)} wpisow')

    def clear_filter(self) -> None:
        self._filtered_entries = list(self._all_entries)
        self._current_index = 0 if self._filtered_entries else -1

    @property
    def entries(self) -> List[LogEntry]:
        return self._filtered_entries

    @property
    def current_index(self) -> int:
        return self._current_index

    @current_index.setter
    def current_index(self, value: int) -> None:
        if 0 <= value < len(self._filtered_entries):
            self._current_index = value

    @property
    def current_entry(self) -> Optional[LogEntry]:
        if self._current_index < 0 or not self._filtered_entries:
            return None
        return self._filtered_entries[self._current_index]

    def go_next(self) -> bool:
        if self._current_index < len(self._filtered_entries) - 1:
            self._current_index += 1
            return True
        return False

    def go_prev(self) -> bool:
        if self._current_index > 0:
            self._current_index -= 1
            return True
        return False

    def has_next(self) -> bool:
        return self._current_index < len(self._filtered_entries) - 1

    def has_prev(self) -> bool:
        return self._current_index > 0

    @property
    def min_date(self) -> Optional[datetime]:
        if not self._all_entries:
            return None
        return min(e.timestamp for e in self._all_entries)

    @property
    def max_date(self) -> Optional[datetime]:
        if not self._all_entries:
            return None
        return max(e.timestamp for e in self._all_entries)
