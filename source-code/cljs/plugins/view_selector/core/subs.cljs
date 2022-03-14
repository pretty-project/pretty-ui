
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.subs
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selector-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (get-in db [extension-id :view-selector/meta-items]))

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id item-key]]
  (get-in db [extension-id :view-selector/meta-items item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (if-let [derived-view-id (r router/get-current-route-path-param db :view-id)]
          (keyword derived-view-id)))

(defn get-selected-view-id
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view-id db :my-extension)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (let [selected-view-id (r get-meta-item db extension-id :view-id)
        default-view-id  (r get-meta-item db extension-id :default-view-id)]
       (if-let [allowed-view-ids (r get-meta-item db extension-id :allowed-view-ids)]
               ; Ha az {:allowed-view-ids [...]} beállítás használatban van ...
               (or ; ... és a selected-view-id megtalálható az allowed-view-ids vektorban,
                   ;     akkor a visszatérési érték a selected-view-id.
                   (some #(if (= % selected-view-id) %) allowed-view-ids)
                   ; ... és a selected-view NEM található meg az allowed-view-ids vektorban,
                   ;     akkor a visszatérési érték a default-view-id.
                   (return default-view-id))
               ; Ha az {:allowed-view-ids [...]} beállítás NINCS használatban ...
               (or ; ... és a selected-view-id értéke NEM nil,
                   ;     akkor a visszatérési érték a selected-view-id.
                   (return selected-view-id)
                   ; ... és a selected-view-id értéke nil,
                   ;     akkor a visszatérési érték a default-view-id.
                   (return default-view-id)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-key
;
; @usage
;  [:view-selector/get-meta-item :my-extension :my-item]
(a/reg-sub :view-selector/get-meta-item get-meta-item)

; @usage
;  [:view-selector/get-selected-view-id :my-extension]
(a/reg-sub :view-selector/get-selected-view-id get-selected-view-id)
