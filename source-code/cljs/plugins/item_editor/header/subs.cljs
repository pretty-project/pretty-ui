
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.subs
    (:require [plugins.plugin-handler.header.subs :as header.subs]
              [x.app-core.api                     :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.header.subs
(def get-header-prop      header.subs/get-header-prop)
(def header-props-stored? header.subs/header-props-stored?)
(def header-did-mount?    header.subs/header-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/header-props-stored? header-props-stored?)
