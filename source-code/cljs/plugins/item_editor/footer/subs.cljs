
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.footer.subs
    (:require [plugins.plugin-handler.footer.subs :as footer.subs]
              [x.app-core.api                     :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.footer.subs
(def get-footer-prop footer.subs/get-footer-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-footer-prop get-footer-prop)
