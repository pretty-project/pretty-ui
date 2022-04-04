
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.mount.subs
    (:require [plugins.plugin-handler.mount.subs :as mount.subs]
              [x.app-core.api                    :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.subs
(def get-footer-prop   mount.subs/get-footer-prop)
(def get-body-prop     mount.subs/get-body-prop)
(def footer-did-mount? mount.subs/footer-did-mount?)
(def body-did-mount?   mount.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-footer-prop get-footer-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/footer-did-mount? footer-did-mount?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/body-did-mount? body-did-mount?)
