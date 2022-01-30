
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.backend.ui.main
    (:require [playground.api  :as playground]
              [x.server-ui.api :as ui]
              [project-emulator.backend.ui.head :as head]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  [request]
  ; DEBUG
  (playground/debug! request)
  (ui/html (ui/head  request (head/request->head-props request))
           (ui/body  request {})))
