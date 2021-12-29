
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-ui.main
    (:require [playground.api  :as playground]
              [x.server-ui.api :as ui]
              [project-emulator.server-ui.head :as head]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  [request]
  ; DEBUG
  (playground/debug! request)
  (ui/html (ui/head  request (head/request->head-props request))
           (ui/body  request {})))
