
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.body.subs
    (:require [plugins.plugin-handler.body.subs :as body.subs]
              [x.app-core.api                   :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.subs
(def get-body-prop   body.subs/get-body-prop)
(def body-did-mount? body.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/get-body-prop get-body-prop)
