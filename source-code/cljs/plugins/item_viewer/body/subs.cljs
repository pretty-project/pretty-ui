
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.subs
    (:require [plugins.engine-handler.body.subs :as body.subs]
              [re-frame.api                     :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.engine-handler.body.subs
(def get-body-prop   body.subs/get-body-prop)
(def body-did-mount? body.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :item-viewer/get-body-prop get-body-prop)
