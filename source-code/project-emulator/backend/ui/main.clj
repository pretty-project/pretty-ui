
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns backend.ui.main
    (:require [backend.ui.head :as head]
              [playground.api  :as playground]
              [x.server-ui.api :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  [request]
  ; DEBUG
  (playground/debug! request)
  (ui/html (ui/head  request (head/request->head-props request))
           (ui/body  request {})))
