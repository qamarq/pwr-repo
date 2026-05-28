#!/usr/bin/env python3

import sys
from datetime import datetime, timedelta

from PyQt6.QtWidgets import (
    QApplication, QMainWindow, QWidget, QVBoxLayout, QHBoxLayout,
    QSplitter, QListWidget, QListWidgetItem, QPushButton, QLabel,
    QLineEdit, QDateEdit, QTimeEdit, QFormLayout, QGroupBox,
    QFileDialog, QSpinBox, QCheckBox,
)
from PyQt6.QtCore import Qt, QDate, QTime
from PyQt6.QtGui import QFont

from log_model import LogModel


class StatusBadge(QLabel):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setMinimumWidth(55)
        self.setAlignment(Qt.AlignmentFlag.AlignCenter)
        self.setFont(QFont('monospace', 11, QFont.Weight.Bold))
        self._set_style('#9e9e9e')

    def set_code(self, code: int):
        self.setText(str(code))
        if 200 <= code < 300:
            self._set_style('#4caf50')
        elif 300 <= code < 400:
            self._set_style('#2196f3')
        elif 400 <= code < 500:
            self._set_style('#ff9800')
        elif 500 <= code < 600:
            self._set_style('#f44336')
        else:
            self._set_style('#9e9e9e')

    def _set_style(self, bg: str):
        self.setStyleSheet(
            f'background-color: {bg}; color: white; '
            f'border-radius: 4px; padding: 3px 10px;'
        )


class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.model = LogModel()
        self._updating = False
        self.setWindowTitle('Log browser')
        self.setMinimumSize(1100, 650)
        self._build_ui()
        self._update_nav_buttons()

    def _build_ui(self):
        central = QWidget()
        self.setCentralWidget(central)
        root = QVBoxLayout(central)
        root.setSpacing(8)
        root.setContentsMargins(10, 10, 10, 10)

        file_row = QHBoxLayout()
        self.path_edit = QLineEdit()
        self.path_edit.setPlaceholderText('Ścieżka do pliku z logami...')
        self.path_edit.setReadOnly(True)
        open_btn = QPushButton('Open')
        open_btn.setFixedWidth(80)
        open_btn.clicked.connect(self._open_file)
        file_row.addWidget(self.path_edit)
        file_row.addWidget(open_btn)
        root.addLayout(file_row)

        splitter = QSplitter(Qt.Orientation.Horizontal)

        left = QWidget()
        left_layout = QVBoxLayout(left)
        left_layout.setContentsMargins(0, 0, 4, 0)

        filter_row = QHBoxLayout()
        filter_row.addWidget(QLabel('From'))
        self.from_date = QDateEdit()
        self.from_date.setCalendarPopup(True)
        self.from_date.setDisplayFormat('yyyy-MM-dd')
        filter_row.addWidget(self.from_date)
        filter_row.addWidget(QLabel('To'))
        self.to_date = QDateEdit()
        self.to_date.setCalendarPopup(True)
        self.to_date.setDisplayFormat('yyyy-MM-dd')
        filter_row.addWidget(self.to_date)
        filter_btn = QPushButton('Filtruj')
        filter_btn.clicked.connect(self._apply_filter)
        filter_row.addWidget(filter_btn)
        clear_btn = QPushButton('Wyczyść')
        clear_btn.clicked.connect(self._clear_filter)
        filter_row.addWidget(clear_btn)
        self.only_2xx = QCheckBox('Tylko 2xx')
        self.only_2xx.stateChanged.connect(self._apply_filter)
        filter_row.addWidget(self.only_2xx)
        filter_row.addStretch()
        left_layout.addLayout(filter_row)

        self.log_list = QListWidget()
        self.log_list.setFont(QFont('monospace', 9))
        self.log_list.currentRowChanged.connect(self._on_list_selection)
        left_layout.addWidget(self.log_list)

        splitter.addWidget(left)

        right = QGroupBox('Szczegóły')
        detail = QFormLayout(right)
        detail.setLabelAlignment(Qt.AlignmentFlag.AlignRight)
        detail.setVerticalSpacing(10)

        self.detail_host = QLineEdit()
        self.detail_host.setReadOnly(True)
        detail.addRow('Remote host:', self.detail_host)

        self.detail_date = QDateEdit()
        self.detail_date.setReadOnly(True)
        self.detail_date.setDisplayFormat('yyyy-MM-dd')
        detail.addRow('Date:', self.detail_date)

        time_row = QHBoxLayout()
        self.detail_time = QTimeEdit()
        self.detail_time.setReadOnly(True)
        self.detail_time.setDisplayFormat('HH:mm:ss')
        time_row.addWidget(self.detail_time)
        time_row.addWidget(QLabel('Timezone:'))
        self.detail_tz = QLineEdit('UTC')
        self.detail_tz.setReadOnly(True)
        self.detail_tz.setMaximumWidth(130)
        time_row.addWidget(self.detail_tz)
        detail.addRow('Time:', time_row)

        status_row = QHBoxLayout()
        self.status_badge = StatusBadge()
        status_row.addWidget(self.status_badge)
        status_row.addSpacing(12)
        status_row.addWidget(QLabel('Method:'))
        self.detail_method = QLineEdit()
        self.detail_method.setReadOnly(True)
        self.detail_method.setMaximumWidth(90)
        status_row.addWidget(self.detail_method)
        status_row.addStretch()
        detail.addRow('Status code:', status_row)

        self.detail_uri = QLineEdit()
        self.detail_uri.setReadOnly(True)
        detail.addRow('Resource:', self.detail_uri)

        self.detail_size = QSpinBox()
        self.detail_size.setReadOnly(True)
        self.detail_size.setMaximum(2_147_483_647)
        self.detail_size.setSuffix(' Bytes')
        self.detail_size.setButtonSymbols(QSpinBox.ButtonSymbols.NoButtons)
        detail.addRow('Size:', self.detail_size)

        splitter.addWidget(right)
        splitter.setStretchFactor(0, 3)
        splitter.setStretchFactor(1, 2)

        root.addWidget(splitter, stretch=1)

        nav_row = QHBoxLayout()
        self.prev_btn = QPushButton('Previous')
        self.prev_btn.setFixedWidth(100)
        self.prev_btn.clicked.connect(self._go_prev)
        self.next_btn = QPushButton('Next')
        self.next_btn.setFixedWidth(100)
        self.next_btn.clicked.connect(self._go_next)
        nav_row.addWidget(self.prev_btn)
        nav_row.addStretch()
        nav_row.addWidget(self.next_btn)
        root.addLayout(nav_row)

    def _open_file(self):
        path, _ = QFileDialog.getOpenFileName(
            self, 'Otwórz plik z logami', '',
            'Log files (*.log);;All files (*)'
        )
        if not path:
            return
        print(f'otwieranie pliku: {path}')
        self.path_edit.setText(path)
        self.model.load_file(path)
        self._update_date_filters()
        self._populate_list()
        self._show_current_detail()

    def _populate_list(self):
        self._updating = True
        self.log_list.clear()
        for entry in self.model.entries:
            self.log_list.addItem(QListWidgetItem(entry.master_text(80)))
        if self.model.current_index >= 0:
            self.log_list.setCurrentRow(self.model.current_index)
        self._updating = False
        self._update_nav_buttons()
        print(f'lista odswiezona, {self.log_list.count()} elementow')

    def _update_date_filters(self):
        mn = self.model.min_date
        mx = self.model.max_date
        if mn and mx:
            self.from_date.setDate(QDate(mn.year, mn.month, mn.day))
            self.to_date.setDate(QDate(mx.year, mx.month, mx.day))

    def _apply_filter(self):
        fd = self.from_date.date()
        td = self.to_date.date()
        start = datetime(fd.year(), fd.month(), fd.day())
        end = datetime(td.year(), td.month(), td.day()) + timedelta(days=1)
        self.model.apply_time_filter(start, end, only_2xx=self.only_2xx.isChecked())
        self._populate_list()
        self._show_current_detail()

    def _clear_filter(self):
        self.only_2xx.setChecked(False)
        self.model.clear_filter()
        self._update_date_filters()
        self._populate_list()
        self._show_current_detail()

    def _on_list_selection(self, row: int):
        if self._updating or row < 0:
            return
        self.model.current_index = row
        self._show_current_detail()
        self._update_nav_buttons()

    def _show_current_detail(self):
        entry = self.model.current_entry
        if entry is None:
            return
        self.detail_host.setText(entry.orig_ip)
        dt = entry.timestamp
        self.detail_date.setDate(QDate(dt.year, dt.month, dt.day))
        self.detail_time.setTime(QTime(dt.hour, dt.minute, dt.second))
        self.status_badge.set_code(entry.status_code)
        self.detail_method.setText(entry.method)
        self.detail_uri.setText(entry.uri)
        self.detail_size.setValue(entry.response_len)

    def _go_prev(self):
        self.model.go_prev()
        self._sync_list_to_model()

    def _go_next(self):
        self.model.go_next()
        self._sync_list_to_model()

    def _sync_list_to_model(self):
        self._updating = True
        self.log_list.setCurrentRow(self.model.current_index)
        self._updating = False
        self._show_current_detail()
        self._update_nav_buttons()

    def _update_nav_buttons(self):
        self.prev_btn.setEnabled(self.model.has_prev())
        self.next_btn.setEnabled(self.model.has_next())


def main():
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec())


if __name__ == '__main__':
    main()
