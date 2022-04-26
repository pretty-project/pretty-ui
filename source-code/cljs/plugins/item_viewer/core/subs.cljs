
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.core.subs
    (:require [plugins.item-viewer.download.subs :as download.subs]
              [plugins.plugin-handler.core.subs  :as core.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item                core.subs/get-meta-item)
(def plugin-synchronizing?        core.subs/plugin-synchronizing?)
(def get-current-item-id          core.subs/get-current-item-id)
(def get-current-item             core.subs/get-current-item)
(def export-current-item          core.subs/export-current-item)
(def get-current-item-label       core.subs/get-current-item-label)
(def get-current-item-modified-at core.subs/get-current-item-modified-at)
(def get-auto-title               core.subs/get-auto-title)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (boolean)
  [db [_ viewer-id]]
  (r core.subs/get-request-id db viewer-id :viewer))

(defn viewer-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (boolean)
  [db [_ viewer-id]]
  (r plugin-synchronizing? db viewer-id :viewer))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn viewing-item?
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-viewer/viewing-item? db :my-viewer "my-item")
  ;
  ; @return (boolean)
  [db [_ viewer-id item-id]]
  ; A viewing-item? függvény visszatérési értéke akkor TRUE, ...
  ; ... ha az item-viewer plugin body komponense a React-fába van csatolva.
  ; ... ha az item-id paraméterként átadott azonosítójú elem van megnyitva szerkesztésre.
  (r core.subs/current-item? db viewer-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn viewer-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (boolean)
  [db [_ viewer-id]]
  ; XXX#3219
  (let [data-received?        (r download.subs/data-received? db viewer-id)
        viewer-synchronizing? (r viewer-synchronizing?        db viewer-id)]
       (or viewer-synchronizing? (not data-received?))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) viewer-id
; @param (keyword) item-key
;
; @usage
;  [:item-viewer/get-meta-item :my-viewer :my-item]
(a/reg-sub :item-viewer/get-meta-item get-meta-item)

; @param (keyword) viewer-id
;
; @usage
;  [:item-viewer/get-current-item-id :my-viewer]
(a/reg-sub :item-viewer/get-current-item-id get-current-item-id)

; @param (keyword) viewer-id
;
; @usage
;  [:item-viewer/get-current-item :my-viewer]
(a/reg-sub :item-viewer/get-current-item get-current-item)

; @param (keyword) viewer-id
;
; @usage
;  [:item-viewer/get-current-item-modified-at :my-viewer]
(a/reg-sub :item-viewer/get-current-item-modified-at get-current-item-modified-at)

; @param (keyword) viewer-id
;
; @usage
;  [:item-viewer/viewer-disabled? :my-viewer]
(a/reg-sub :item-viewer/viewer-disabled? viewer-disabled?)
