
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.20
; Description:
; Version: v0.4.2
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.name-handler)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def NAME-ORDERS {:hu :reversed
                  :en :normal})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn name->ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ; @param (keyword) locale-id
  ;
  ; @return (string)
  [first-name last-name locale-id]
  (let [name-order (get NAME-ORDERS locale-id)]
       (if (= name-order :reversed)
           (str last-name  " " first-name)
           (str first-name " " last-name))))
