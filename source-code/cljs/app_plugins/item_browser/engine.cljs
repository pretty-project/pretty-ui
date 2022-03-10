
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.engine
    (:require [mid-plugins.item-browser.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def request-id      engine/request-id)
(def collection-name engine/collection-name)
(def transfer-id     engine/transfer-id)
(def route-id        engine/route-id)
(def route-template  engine/route-template)
(def base-route      engine/base-route)
(def component-id    engine/component-id)
(def dialog-id       engine/dialog-id)
