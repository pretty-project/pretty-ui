
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.body.prototypes
    (:require [engines.item-browser.core.helpers   :as core.helpers]
              [plugins.item-lister.body.prototypes :as body.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:item-path (vector)}
  [browser-id body-props]
  ; XXX#6177
  ; A body-props térképen az item-lister engine body-props-prototype függvénye
  ; is alkalmazva van, emiatt az ott beállított tulajdonságokat nem szükséges
  ; itt is beállítani!
  (merge {:item-path (core.helpers/default-item-path browser-id)}
         (body.prototypes/body-props-prototype browser-id body-props)))
