
(ns plugins.view-selector.api
    (:require [plugins.view-selector.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.view-selector.engine
(def request-id        engine/request-id)
(def get-selected-view engine/get-selected-view)
(def get-header-props  engine/get-header-props)
(def get-body-props    engine/get-body-props)
(def change-view!      engine/change-view!)
