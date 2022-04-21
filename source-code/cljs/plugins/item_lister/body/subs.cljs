
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.body.subs
    (:require [plugins.plugin-handler.body.subs :as body.subs]
              [x.app-core.api                   :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.subs
(def get-body-prop      body.subs/get-body-prop)
(def body-props-stored? body.subs/body-props-stored?)
(def body-did-mount?    body.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/body-props-stored? body-props-stored?)
