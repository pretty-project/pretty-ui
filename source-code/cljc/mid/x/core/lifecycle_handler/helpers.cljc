
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.lifecycle-handler.helpers
    (:require [candy.api                          :refer [return]]
              [mid-fruits.random                  :as random]
              [mid.x.core.lifecycle-handler.state :as lifecycle-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-life-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (namespaced keyword)
  []
  (keyword (random/generate-string) "lifecycles"))

(defn life-id->namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword or namespaced keyword) life-id
  ;
  ; @return (string)
  [life-id]
  (if-let [namespace (namespace life-id)]
          (return namespace)
          (random/generate-string)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lifes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  []
  @lifecycle-handler.state/LIFES)

(defn get-period-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) period-id
  ;
  ; @return (vector)
  [period-id]
  (letfn [(f [period-events dex life] (if-let [period (get life period-id)]
                                              (conj   period-events period)
                                              (return period-events)))]
         (reduce-kv f [] (get-lifes))))
