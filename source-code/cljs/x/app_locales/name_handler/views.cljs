
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.4.4
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.name-handler.views)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn name-order
  ; @param (component, hiccup or string) first-name
  ; @param (component, hiccup or string) last-name
  ; @param (keyword) name-order
  ;  :normal, :reversed
  ;
  ; @usage
  ;  [locales/name-order "First" "Last" :reversed]
  ;
  ; @return (component)
  [first-name last-name name-order]
  (case name-order :reversed [:<> last-name  first-name]
                             [:<> first-name last-name]))
