
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.core.subs
    (:require [plugins.item-preview.body.subs   :as body.subs]
              [plugins.plugin-handler.core.subs :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def plugin-synchronizing? core.subs/plugin-synchronizing?)
(def get-current-item-id   core.subs/get-current-item-id)
(def no-item-id-passed?    core.subs/no-item-id-passed?)
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

(defn reload-preview?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (boolean)
  [db [_ preview-id]]
  ; XXX#5051
  ; Ha a body komponens valamelyik paramétere megváltozik, akkor a komponens
  ; component-did-update életciklusa megtörténik, ami a plugin újratöltését kezdeményezi.
  ; Csak abban az esetben szükséges újratölteni a plugint, ha a body komponens item-id
  ; tulajdonságának értéke megváltozik!
  (let [item-id         (r body.subs/get-body-prop db preview-id :item-id)
        current-item-id (r get-current-item-id     db preview-id)]
       (not= current-item-id item-id)))



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
;  [:item-preview/no-item-id-passed? :my-preview]
(r/reg-sub :item-preview/no-item-id-passed? no-item-id-passed?)

; @param (keyword) preview-id
;
; @usage
;  [:item-preview/get-current-item :my-preview]
(r/reg-sub :item-preview/get-current-item get-current-item)