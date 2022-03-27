
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.subs
    (:require [plugins.plugin-handler.mount.subs :as mount.subs]
              [x.app-core.api                    :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.subs
(def get-header-prop   mount.subs/get-header-prop)
(def get-body-prop     mount.subs/get-body-prop)
(def header-did-mount? mount.subs/header-did-mount?)
(def body-did-mount?   mount.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/header-did-mount? header-did-mount?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/body-did-mount? body-did-mount?)
