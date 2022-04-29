
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.header.subs
    (:require [plugins.plugin-handler.header.subs :as header.subs]
              [x.app-core.api                     :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.header.subs
(def get-header-prop header.subs/get-header-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/get-header-prop get-header-prop)