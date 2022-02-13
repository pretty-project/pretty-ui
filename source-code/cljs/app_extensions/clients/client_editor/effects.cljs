
(ns app-extensions.clients.client-editor.effects
    (:require [x.app-core.api :as a :refer [r]]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients.client-editor/load-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:clients.client-editor/render-editor!])
