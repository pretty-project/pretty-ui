
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.name-handler.views)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn name-order
  ; @param (component, hiccup or string) first-name
  ; @param (component, hiccup or string) last-name
  ; @param (keyword) name-order
  ;  :normal, :reversed
  ;
  ; @usage
  ;  [name-order "First" "Last" :reversed]
  [first-name last-name name-order]
  (case name-order :reversed [:<> last-name  first-name]
                             [:<> first-name last-name]))
