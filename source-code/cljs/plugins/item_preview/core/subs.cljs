
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.core.subs
    (:require [plugins.plugin-handler.core.subs :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



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
  ; @param (keyword) preview-id
  ;
  ; @return (boolean)
  [db [_ preview-id]]
  (r core.subs/get-request-id db preview-id :preview))

(defn preview-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (boolean)
  [db [_ preview-id]]
  (r plugin-synchronizing? db preview-id :preview))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (boolean)
  [db [_ preview-id]]
  ; XXX#5051
  ; Ha a body komponens preview-props paramétere megváltozik, akkor
  ; a ** komponens component-did-update életciklusa
  ; megtörténik viszont ebben az esetben nem szükséges újra letölteni az elemet,
  ; ezért kell vizsgálni, hogy a current-item értéke változott-e meg, vagy csak
  ; a preview-props térkép változása indította el a component-did-update életciklust!
  (let [requested-item-id (r get-meta-item       db preview-id :requested-item)
        current-item-id   (r get-current-item-id db preview-id)]
       (not= current-item-id requested-item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) preview-id
; @param (keyword) item-key
;
; @usage
;  [:item-preview/get-meta-item :my-preview :my-item]
(r/reg-sub :item-preview/get-meta-item get-meta-item)

; @param (keyword) preview-id
;
; @usage
;  [:item-preview/get-current-item-id :my-preview]
(r/reg-sub :item-preview/get-current-item-id get-current-item-id)

; @param (keyword) preview-id
;
; @usage
;  [:item-preview/get-current-item :my-preview]
(r/reg-sub :item-preview/get-current-item get-current-item)
