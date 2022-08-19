
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.error-reports)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:error-report
           {:en "Error report"
            :hu "Hibajelentés"}
           :error-report-sent
           {:en "Error report sent"
            :hu "Hibajelentés elküldve"}
           :enable-sending-error-reports!
           {:en "Enable sending error reports"
            :hu "Hibajelentések küldésének engedélyezése"}
           :send-error-report!
           {:en "Send error report"
            :hu "Hibajelentés küldése"}
           :sending-error-reports
           {:en "Send error reports"
            :hu "Hibajelentések küldése"}})
