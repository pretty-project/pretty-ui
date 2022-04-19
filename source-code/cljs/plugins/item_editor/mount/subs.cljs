
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.subs
    (:require [plugins.plugin-handler.mount.subs :as mount.subs]
              [x.app-core.api                    :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.subs
(def get-footer-prop      mount.subs/get-footer-prop)
(def get-body-prop        mount.subs/get-body-prop)
(def get-header-prop      mount.subs/get-header-prop)
(def footer-props-stored? mount.subs/footer-props-stored?)
(def body-props-stored?   mount.subs/body-props-stored?)
(def header-props-stored? mount.subs/header-props-stored?)
(def footer-did-mount?    mount.subs/footer-did-mount?)
(def body-did-mount?      mount.subs/body-did-mount?)
(def header-did-mount?    mount.subs/header-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-footer-prop get-footer-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/footer-props-stored? footer-props-stored?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/body-props-stored? body-props-stored?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/header-props-stored? header-props-stored?)
