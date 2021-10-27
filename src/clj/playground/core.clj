
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.core
    (:require [mongo-db.api      :as mongo-db]
              ;[pathom.api        :as pathom]
              [x.server-core.api :as a]))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn playground
  ; @param (map) request
  [request])
  ;(println "Playground *A#?o%!_f"))

(defn debug!
  ; @param (map) request
  [request]
  (if (a/request->debug-mode? request)
      (playground             request)))
