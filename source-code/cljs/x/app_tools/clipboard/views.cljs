

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.clipboard.views)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clipboard
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) text
  [text]
  [:input#x-app-clipboard {:defaultValue text}])
