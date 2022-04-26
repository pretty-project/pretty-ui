
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.subs
    (:require [mid-fruits.candy                 :refer [return]]
              [mid-fruits.vector                :as vector]
              [plugins.plugin-handler.core.subs :as core.subs]
              [plugins.view-selector.body.subs  :as body.subs]
              [x.app-core.api                   :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item core.subs/get-meta-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-view-id
  ; @param (keyword) selector-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view-id db :my-selector)
  ;
  ; @return (keyword)
  [db [_ selector-id]]
  (let [selected-view-id (r get-meta-item           db selector-id :view-id)
        default-view-id  (r body.subs/get-body-prop db selector-id :default-view-id)]
       (or selected-view-id default-view-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) selector-id
; @param (keyword) item-key
;
; @usage
;  [:view-selector/get-meta-item :my-selector :my-item]
(a/reg-sub :view-selector/get-meta-item get-meta-item)

; @param (keyword) selector-id
;
; @usage
;  [:view-selector/get-selected-view-id :my-selector]
(a/reg-sub :view-selector/get-selected-view-id get-selected-view-id)
