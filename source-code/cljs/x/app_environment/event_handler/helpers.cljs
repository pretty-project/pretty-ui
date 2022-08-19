
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.event-handler.helpers
    (:require [dom.api          :as dom]
              [mid-fruits.candy :refer [return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-id->target
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) element-id
  ;
  ; @return (DOM-element)
  [element-id]
  (if element-id (dom/get-element-by-id element-id)
                 (return                js/window)))
