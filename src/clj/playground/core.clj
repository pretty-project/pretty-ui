
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.core
    (:require [mongo-db.api      :as mongo-db]
              [x.server-core.api :as a]))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn playground
  ; @param (map) request
  [request]
  (println "Playground *A#?o%!_f"))
;  (mongo-db/update-document! "abc" {:def/test "Test" :def/keyword :bbbb}))
;                                    :def/my-date "2020-01-01"}))
;                                   {:ordered? true}))

(defn debug!
  ; @param (map) request
  [request]
  (if (a/request->debug-mode? request)
      (playground             request)))
