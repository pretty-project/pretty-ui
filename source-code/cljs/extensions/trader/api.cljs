
(ns extensions.trader.api
    (:require [extensions.trader.account]
              [extensions.trader.editor]
              [extensions.trader.engine]
              [extensions.trader.listener]
              [extensions.trader.main]
              [extensions.trader.monitor]
              [extensions.trader.position]
              [extensions.trader.router]
              [extensions.trader.settings]
              [extensions.trader.sync]
              [extensions.trader.wallet]))



;; -- Crypto Crane ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name Rocky Mountains pattern
;  - Az adott X kriptovaluta árfolyamának jelenlegi USD/X értéke legalább Y ideje nem volt
;    annyira alacsony, mint a jelenlegi érték
;  - Az eltelt Y idő alatti legmagasabb USD/X érték legalább Z mértékkel meghaladta
;    az árfolyam jelenlegi USD/X értékét
;
; @name Grand Canyon pattern
;  TODO ...
;
; @name Crypto Crane app
;  TODO ...
;
; @name Crypto Monster app
;  TODO ...
;
