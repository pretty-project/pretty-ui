
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-handler.engine
    (:require [mid-plugins.item-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-handler.engine
(def request-id    engine/request-id)
(def mutation-name engine/mutation-name)
(def resolver-id   engine/resolver-id)
(def dialog-id     engine/dialog-id)
