
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.events
    (:require [plugins.plugin-handler.routes.events :as routes.events]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.events
(def set-parent-route! routes.events/set-parent-route!)
