
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.core.subs
    (:require [plugins.plugin-handler.core.subs :as core.subs]
              [x.app-core.api                   :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def plugin-synchronizing? core.subs/plugin-synchronizing?)
(def get-current-item-id   core.subs/get-current-item-id)
(def get-current-item      core.subs/get-current-item)
(def use-query-prop        core.subs/use-query-prop)
(def use-query-params      core.subs/use-query-params)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (boolean)
  [db [_ picker-id]]
  (r core.subs/get-request-id db picker-id :picker))

(defn picker-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (boolean)
  [db [_ picker-id]]
  (r plugin-synchronizing? db picker-id :picker))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (boolean)
  [db [_ picker-id]]
  ; XXX#5051
  ; Ha a body komponens picker-props paramétere megváltozik, akkor
  ; a ** komponens component-did-update életciklusa
  ; megtörténik viszont ebben az esetben nem szükséges újra letölteni az elemet,
  ; ezért kell vizsgálni, hogy a current-item értéke változott-e meg, vagy csak
  ; a picker-props térkép változása indította el a component-did-update életciklust!
  (let [requested-item-id (r get-meta-item       db picker-id :requested-item)
        current-item-id   (r get-current-item-id db picker-id)]
       (not= current-item-id requested-item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) picker-id
; @param (keyword) item-key
;
; @usage
;  [:item-picker/get-meta-item :my-picker :my-item]
(a/reg-sub :item-editor/get-meta-item get-meta-item)

; @param (keyword) picker-id
;
; @usage
;  [:item-picker/get-current-item-id :my-picker]
(a/reg-sub :item-picker/get-current-item-id get-current-item-id)

; @param (keyword) picker-id
;
; @usage
;  [:item-picker/get-current-item :my-picker]
(a/reg-sub :item-picker/get-current-item get-current-item)
