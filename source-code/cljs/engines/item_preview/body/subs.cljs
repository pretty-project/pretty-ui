
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.body.subs
    (:require [engines.plugin-handler.body.subs :as body.subs]
              [re-frame.api                     :as r]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.plugin-handler.body.subs
(def get-body-prop   body.subs/get-body-prop)
(def body-did-mount? body.subs/body-did-mount?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :item-preview/get-body-prop get-body-prop)
