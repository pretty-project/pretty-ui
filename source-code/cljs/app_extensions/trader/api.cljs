
(ns app-extensions.trader.api
    (:require [app-extensions.trader.account]
              [app-extensions.trader.editor]
              [app-extensions.trader.engine]
              [app-extensions.trader.listener]
              [app-extensions.trader.main]
              [app-extensions.trader.monitor]
              [app-extensions.trader.router]
              [app-extensions.trader.settings]
              [app-extensions.trader.sync]
              [app-extensions.trader.wallet]))



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
