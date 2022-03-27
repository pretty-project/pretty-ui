
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.mount.subs
    (:require [plugins.plugin-handler.mount.subs :as mount.subs]
              [x.app-core.api                    :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.subs
(def get-body-prop   mount.subs/get-body-prop)
(def body-did-mount? mount.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :view-selector/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :view-selector/body-did-mount? body-did-mount?)
