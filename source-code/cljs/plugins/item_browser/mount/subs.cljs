
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.mount.subs
    (:require [plugins.item-lister.mount.subs :as plugins.item-lister.mount.subs]
              [x.app-core.api                 :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.mount.subs
(def get-header-prop   plugins.item-lister.mount.subs/get-header-prop)
(def get-body-prop     plugins.item-lister.mount.subs/get-body-prop)
(def header-did-mount? plugins.item-lister.mount.subs/header-did-mount?)
(def body-did-mount?   plugins.item-lister.mount.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-browser/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-browser/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-browser/header-did-mount? header-did-mount?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-browser/body-did-mount? body-did-mount?)
