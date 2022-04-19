
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.mount.subs
    (:require [plugins.plugin-handler.mount.subs :as mount.subs]
              [x.app-core.api                    :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.subs
(def get-header-prop      mount.subs/get-header-prop)
(def get-body-prop        mount.subs/get-body-prop)
(def header-props-stored? mount.subs/header-props-stored?)
(def body-props-stored?   mount.subs/body-props-stored?)
(def header-did-mount?    mount.subs/header-did-mount?)
(def body-did-mount?      mount.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/header-props-stored? header-props-stored?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/body-props-stored? body-props-stored?)
