
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.components.querier.events
    (:require [candy.api    :refer [return]]
              [re-frame.api :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn receive-query!
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ;  {:value-path (vector)}
  ; @param (map) server-response
  [db [_ querier-id {:keys [value-path]} server-response]]
  (assoc-in db value-path server-response))
