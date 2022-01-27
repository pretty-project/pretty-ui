
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.core
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.vector    :as vector]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-locales.api :as locales]
              [x.server-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn playground
  ; @param (map) request
  [request])
  ;(println "Playground *A#?o%!_f")

(defn debug!
  ; @param (map) request
  [request]
  (if (a/request->debug-mode request)
      (playground            request)))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:view-selector/initialize-selector! :playground {:default-view-id :anchors}]})
